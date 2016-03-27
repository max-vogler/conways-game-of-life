package de.maxvogler.life;

import de.maxvogler.life.grid.Grid;
import de.maxvogler.life.grid.MutableGrid;
import de.maxvogler.life.util.ScheduledApplication;
import spark.servlet.SparkApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;

/**
 * An implementation of Conway's Game of Life.
 */
public final class Game extends ScheduledApplication<MutableGrid> implements SparkApplication {

    private static Game instance = null;

    private static int port = 8080;

    private static int defaultSize = 100;

    private Grid grid;

    private MutableGrid changes;

    public Game() {
        this(defaultSize, 200);
    }

    private Game(int size, int interval) {
        super(interval);
        this.grid = Grid.random(size);
        this.changes = new MutableGrid(grid);

        if (instance == null) {
            instance = this;
        }
    }

    public Grid getGrid() {
        return grid;
    }

    @Override
    public void init() {
        port(port);

        // Provide client HTML, JavaScript and CSS
        staticFileLocation("public/");

        // Listen to WebSockets, which transfer game changes
        webSocket("/api/v1/connect", GameClients.class);

        // Provide clients with the current Grid size
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        get("/api/v1/size", (req, res) -> grid.size());

        // Start the game!
        start();
    }

    @Override
    public void tick() {
        // Apply all changes, commited by clients and calculate the next state of all cells
        grid = changes.tick();

        // Copy the new state, to enable clients to make changes again
        changes = new MutableGrid(grid);

        // Broadcast the new state to all clients
        GameClients.broadcast(grid);
    }

    @Override
    protected MutableGrid getChanges() {
        return changes;
    }

    protected void setSize(int size) {
        change(old -> this.changes = new MutableGrid(old, size));
    }

    public static void main(String[] args) {
        String size = args.length > 0 ? args[0] : "" + defaultSize;

        try {
            defaultSize = Integer.valueOf(size);
        } catch (NumberFormatException e) {
            // do nothing, use default size
        }

        new Game().init();
        awaitInitialization();
        System.out.println("To start Conway's Game of Life, visit http://localhost:" + port + " in your browser.");

        try {
            System.setProperty("apple.awt.UIElement", "true");
            Desktop.getDesktop().browse(new URI("http://localhost:" + port));
        } catch (NullPointerException | IOException | URISyntaxException e) {
            // do nothing
        }
    }

    public static Game getInstance() {
        return instance;
    }
}
