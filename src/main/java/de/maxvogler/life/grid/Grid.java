package de.maxvogler.life.grid;

import de.maxvogler.life.util.Parallelize;
import de.maxvogler.life.util.function.IndexedConsumer;
import de.maxvogler.life.util.function.IndexedFunction;
import org.uncommons.maths.binary.BitString;

import java.util.Random;

import static de.maxvogler.life.grid.Cell.ALIVE;
import static de.maxvogler.life.grid.Cell.DEAD;

/**
 * The Grid in Conway's Game of Life. The width and height of the grid are equal (called 'size'), so every
 * grid contains (size * size) Cells.
 */
public class Grid {

    private final BitString cells;

    protected final int size;

    /**
     * Creates an empty Grid, where all Cells are dead.
     *
     * @param size the width and height
     */
    public Grid(int size) {
        this.size = size;
        this.cells = new BitString(size * size);
    }

    /**
     * Copies a Grid.
     *
     * @param grid
     */
    public Grid(Grid grid) {
        this(grid, grid.size);
    }

    /**
     * Copies a part of a Grid.
     *
     * @param grid
     * @param size
     */
    public Grid(Grid grid, int size) {
        this(size);

        grid.forEach((cell, x, y) -> {
            if(x < size && y < size) {
                set(x, y, cell);
            }
        });
    }

    /**
     * Creates a Grid, where each cell is randomly chosen to be alive or dead.
     *
     * @param size the width and height
     * @return
     */
    public static Grid random(int size) {
        Grid grid = new Grid(size);
        grid.randomize();
        return grid;
    }

    /**
     * Creates a grid based on a String. Each row of the String has to be terminated with a newline ("\n"),
     * dead cells have to be marked with space (" ") and alive cells with any other character (e.g. "X")
     *
     * @param string
     * @return
     */
    public static Grid parseString(String string) {
        Grid grid = new Grid(string.split("\n").length);
        grid.parse(string);
        return grid;
    }

    /**
     * Returns the width/height of this Grid
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns the Cell at the given location.
     *
     * @param x the column index (ranging from 0 to size()-1)
     * @param y the row index (ranging form 0 to size()-1)
     * @return
     */
    public Cell get(int x, int y) {
        return getBit(x, y) ? ALIVE : DEAD;
    }

    private boolean getBit(int x, int y) {
        x = (x + size) % size;
        y = (y + size) % size;

        return uncheckedGetBit(x, y);
    }

    private boolean uncheckedGetBit(int x, int y) {
        return cells.getBit(y * size + x);
    }

    private int uncheckedGetNumber(int x, int y) {
        return uncheckedGetBit(x, y) ? 1 : 0;
    }

    /**
     * Sets the Cell at the given location.
     *
     * @param x    the column index (ranging from 0 to size()-1)
     * @param y    the row index (ranging form 0 to size()-1)
     * @param cell
     */
    protected void set(int x, int y, Cell cell) {
        x = (x + size) % size;
        y = (y + size) % size;

        cells.setBit(y * size + x, ALIVE == cell);
    }

    /**
     * Overwrites every Cell in the Grid.
     *
     * @param value
     */
    protected void setAll(Cell value) {
        forEach((cell, x, y) -> set(x, y, value));
    }

    public int countAliveNeighbours(int x, int y) {
        int xl = (x + size - 1) % size;
        int yt = (y + size - 1) % size;
        int xr = (x + 1) % size;
        int yb = (y + 1) % size;

        return uncheckedGetNumber(xl, y) +
                uncheckedGetNumber(xr, y) +
                uncheckedGetNumber(x, yt) +
                uncheckedGetNumber(x, yb) +
                uncheckedGetNumber(xr, yb) +
                uncheckedGetNumber(xl, yb) +
                uncheckedGetNumber(xr, yt) +
                uncheckedGetNumber(xl, yt);
    }

    protected Grid map(IndexedFunction<Cell, Cell> fun) {
        Grid grid = new Grid(size);
        Parallelize.processRange(size, (startInclusive, endExclusive) -> {
            for (int y = startInclusive; y < endExclusive; y++) {
                for (int x = 0; x < size; x++) {
                    grid.set(x, y, fun.apply(get(x, y), x, y));
                }
            }
        });
        return grid;
    }

    protected void forEach(IndexedConsumer<Cell> fun) {
        Parallelize.processRange(size, (startInclusive, endExclusive) -> {
            for (int y = startInclusive; y < endExclusive; y++) {
                for (int x = 0; x < size; x++) {
                    fun.apply(get(x, y), x, y);
                }
            }
        });
    }

    /**
     * Calculates the next state of all Cells in the Grid and returns the state as new Grid.
     *
     * @return
     */
    public Grid tick() {
        return map((cell, x, y) -> cell.tick(countAliveNeighbours(x, y)));
    }

    protected void randomize() {
        Random random = new Random();
        forEach((cell, x, y) -> set(x, y, random.nextBoolean() ? ALIVE : DEAD));
    }

    protected void parse(String grid) {
        String[] lines = grid.split("\n");
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                set(x, y, lines[y].charAt(x) == ' ' ? DEAD : ALIVE);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grid grid = (Grid) o;
        return size == grid.size && cells.equals(grid.cells);

    }

    @Override
    public int hashCode() {
        int result = cells.hashCode();
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return toString("\n");
    }

    public String toString(String rowSeparator) {
        StringBuilder str = new StringBuilder(size * size + size * rowSeparator.length());
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                str.append(get(x, y) == ALIVE ? 'X' : ' ');
            }
            str.append(rowSeparator);
        }
        return str.toString();
    }


}
