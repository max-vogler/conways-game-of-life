package de.maxvogler.life.grid;

import de.maxvogler.life.util.function.IntToBooleanFunction;

/**
 * A cell in Conway's Game of Life. A Cell does not know its position on the Grid, but provides a Function
 * to calculate its next state based on the number of alive neighbour cells (called a 'tick').
 */
public enum Cell {

    /**
     * An alive Cell. It stays alive iff exactly two or three neighbour cells are alive.
     */
    ALIVE(c -> c == 2 || c == 3),

    /**
     * A dead Cell. It turns alive iff exactly three neighbour cells are alive.
     */
    DEAD(c -> c == 3);

    private final IntToBooleanFunction tick;

    Cell(IntToBooleanFunction tick) {
        this.tick = tick;
    }

    /**
     * Calculates the next state of this Cell, based on the number of alive neighbour cells, following the basic
     * rules of Conways Game of Life.
     *
     * @param aliveNeighboursCount
     * @return
     */
    public Cell tick(int aliveNeighboursCount) {
        return tick.apply(aliveNeighboursCount) ? ALIVE : DEAD;
    }
}
