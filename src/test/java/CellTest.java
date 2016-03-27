import org.junit.Test;

import static de.maxvogler.life.grid.Cell.ALIVE;
import static de.maxvogler.life.grid.Cell.DEAD;
import static org.junit.Assert.assertEquals;

public class CellTest {

    @Test
    public void aliveCellStaysAlive() {
        assertEquals(ALIVE, ALIVE.tick(2));
        assertEquals(ALIVE, ALIVE.tick(3));
    }

    @Test
    public void aliveCellDies() {
        assertEquals(DEAD, ALIVE.tick(0));
        assertEquals(DEAD, ALIVE.tick(1));
        // 2 & 3 --> ALIVE
        assertEquals(DEAD, ALIVE.tick(4));
        assertEquals(DEAD, ALIVE.tick(5));
        assertEquals(DEAD, ALIVE.tick(6));
        assertEquals(DEAD, ALIVE.tick(7));
        assertEquals(DEAD, ALIVE.tick(8));
        assertEquals(DEAD, ALIVE.tick(9));
    }

    @Test
    public void deadCellComesAlive() {
        assertEquals(ALIVE, DEAD.tick(3));
    }

    @Test
    public void deadCellStaysDead() {
        assertEquals(DEAD, DEAD.tick(0));
        assertEquals(DEAD, DEAD.tick(1));
        assertEquals(DEAD, DEAD.tick(2));
        // 3 --> ALIVE
        assertEquals(DEAD, DEAD.tick(4));
        assertEquals(DEAD, DEAD.tick(5));
        assertEquals(DEAD, DEAD.tick(6));
        assertEquals(DEAD, DEAD.tick(7));
        assertEquals(DEAD, DEAD.tick(8));
        assertEquals(DEAD, DEAD.tick(9));
    }


}
