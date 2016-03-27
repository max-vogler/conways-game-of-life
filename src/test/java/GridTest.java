import de.maxvogler.life.grid.Grid;
import org.junit.Test;

import java.nio.ByteBuffer;

import static de.maxvogler.life.grid.Cell.ALIVE;
import static de.maxvogler.life.grid.Cell.DEAD;
import static org.junit.Assert.assertEquals;

public class GridTest {

    @Test
    public void getCorrect() {
        Grid grid = Grid.parseString("" +
                " X \n" +
                "X  \n" +
                " XX"
        );

        assertEquals(DEAD, grid.get(0, 0));
        assertEquals(ALIVE, grid.get(1, 0));
        assertEquals(DEAD, grid.get(2, 0));
        assertEquals(ALIVE, grid.get(0, 1));
        assertEquals(DEAD, grid.get(1, 1));
        assertEquals(DEAD, grid.get(2, 1));
        assertEquals(DEAD, grid.get(0, 2));
        assertEquals(ALIVE, grid.get(1, 2));
        assertEquals(ALIVE, grid.get(2, 2));
    }

    @Test
    public void countAliveNeighboursCorrect() {
        Grid grid = Grid.parseString("" +
                "     \n" +
                "  X  \n" +
                "  X  \n" +
                "  X  \n" +
                "     "
        );

        assertEquals(0, grid.countAliveNeighbours(0, 0));
        assertEquals(0, grid.countAliveNeighbours(0, 1));
        assertEquals(2, grid.countAliveNeighbours(1, 1));
        assertEquals(1, grid.countAliveNeighbours(2, 1));
        assertEquals(2, grid.countAliveNeighbours(2, 2));
    }

    @Test
    public void tickCorrectBlinker() {
        Grid grid1 = Grid.parseString("     \n  X  \n  X  \n  X  \n     ");
        Grid grid2 = Grid.parseString("     \n     \n XXX \n     \n     ");

        assertEquals(grid2, grid1.tick());
        assertEquals(grid1, grid2.tick());
    }

}
