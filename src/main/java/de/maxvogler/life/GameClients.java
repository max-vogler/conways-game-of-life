package de.maxvogler.life;

import de.maxvogler.life.grid.Cell;
import de.maxvogler.life.grid.Grid;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static de.maxvogler.life.grid.Cell.ALIVE;
import static de.maxvogler.life.grid.Cell.DEAD;

@WebSocket
public class GameClients {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        String[] lines = message.split("\n");
        Game game = Game.getInstance();

        game.change((grid) -> {
            for (String line : lines) {
                String[] parts = line.split(",");

                switch (parts[0]) {
                    // Sets a single cell (e.g. set,12,24,1)
                    case "set":
                        int x = Integer.valueOf(parts[1]);
                        int y = Integer.valueOf(parts[2]);
                        Cell value = "1".equals(parts[3]) ? ALIVE : DEAD;
                        grid.set(x, y, value);
                        break;

                    // Randomizes the whole Grid
                    case "randomize":
                        grid.randomize();
                        break;

                    // Sets all cells in the Grid dead
                    case "clear":
                        grid.setAll(DEAD);
                        break;

                    // Overwrites the Grid (e.g. parse,XXX\\nX X\\nXXX)
                    case "parse":
                        grid.parse(parts[1].replace("\\n", "\n"));
                        break;

                    // Changes the tick interval milliseconds (e.g. interval,500)
                    case "interval":
                        game.setInterval(Integer.valueOf(parts[1]));
                        break;

                    // (Re-)starts the Game
                    case "start":
                        game.start();
                        break;

                    // Pauses the Game
                    case "stop":
                        game.stop();
                        break;
                }
            }
        });
    }

    /**
     * Broadcast the current Grid state to all clients
     * @param grid
     */
    public static void broadcast(Grid grid) {
        String string = grid.toString("");
        sessions.forEach(session -> session.getRemote().sendStringByFuture(string));
    }

}
