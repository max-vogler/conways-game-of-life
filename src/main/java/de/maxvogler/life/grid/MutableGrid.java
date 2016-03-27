package de.maxvogler.life.grid;

public class MutableGrid extends Grid {

    public MutableGrid(int size) {
        super(size);
    }

    public MutableGrid(Grid grid) {
        super(grid);
    }

    public MutableGrid(Grid grid, int size) {
        super(grid ,size);
    }

    @Override
    public void set(int x, int y, Cell cell) {
        super.set(x, y, cell);
    }

    @Override
    public void setAll(Cell value) {
        super.setAll(value);
    }

    @Override
    public void randomize() {
        super.randomize();
    }

    @Override
    public void parse(String grid) {
        super.parse(grid);
    }

}
