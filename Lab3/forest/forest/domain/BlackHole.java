package domain;

import java.util.*;

public class BlackHole implements Thing {
    private Forest forest;
    private int row, column;
    private int tictac;

    private static Random RANDOM;
    static {
        RANDOM = new Random();
    }

    public BlackHole(Forest forest, int row, int column) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.tictac = 0;
        this.forest.setThing(row, column, (Thing) this);
    }

    public int shape() {
        return Thing.ROUND;
    }

    public void ticTac() {
        this.tictac++;
        if (this.tictac == 12) {
            this.forest.setThing(this.row, this.column, null);
        }
        List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column, 1);
        for (Integer[] neighborCell : neighborCells) {
            if (RANDOM.nextFloat() < 0.3) {
                int row = neighborCell[0];
                int column = neighborCell[1];
                Thing neighbor = this.forest.getThing(row, column);
                if ((neighbor != null) && (neighbor.shape() == Thing.SQUARE)) {
                    Thing newThing = (RANDOM.nextFloat() < 0.75 ? new BlackHole(this.forest, row, column) : null);
                    this.forest.setThing(row, column, newThing);
                    this.forest.setThing(this.row, this.column, null);
                }
            }
        }
    }
}
