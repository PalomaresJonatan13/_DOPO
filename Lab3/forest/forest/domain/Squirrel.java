package domain;
import java.awt.Color;
import java.util.*;

public class Squirrel extends LivingThing implements Thing {
    private Forest forest;
    private int row, column;
    private Color color;
    private int tictac;
    private boolean dead;

    private static Random RANDOM;
    static {
        RANDOM = new Random();
    }

    public Squirrel(Forest forest, int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column; 
        this.color = new Color(150, 75, 0);
        this.tictac = 0;
        this.dead = false;
        this.forest.setThing(row, column, (Thing) this);
    }

    private void changeColor(int totalIterations, int iteration) {
        float relativeIteration = ((float) iteration) / ((float) totalIterations);
        this.color = new Color(
            (int) (150 + 105*relativeIteration),
            (int) (75 + 150*relativeIteration),
            0
        );
    }

    public final int getRow(){
        return this.row;
    }

    public final int getColumn(){
        return this.column;
    }

    public final Color getColor(){
        return this.color;
    }

    public void ticTac() {
        if (this.tictac+1 > this.forest.getTictac()) return;
        this.tictac++;

        if (!this.dead) {
            this.changeColor(40, tictac);
            if (this.tictac % 2 == 0) {
                for (int j=0; j<5; j++) this.step();
            }
            if (this.tictac % 4 == 0){
                this.years+=1;
            }
        }
        if (this.years == 10) this.die();
        else {
            this.reproduce();
            this.move();
        }
    }

    // it is not possible that two squirrels reproduce twice, because after the first squirrel moves
    // they have no longer one intermediate empty cell
    private int reproduce() {
        int reproduced = 0;
        List<Integer[]> neighborCells = this.neighborCells(2);
        for (Integer[] neighborCell : neighborCells) {
            int r = neighborCell[0];
            int c = neighborCell[1];
            int midRow = (this.row + r)/2;
            int midColumn = (this.column + c)/2;
            if (
                (this.forest.getThing(r, c) != null) &&
                (this.forest.getThing(r, c).getClass() == this.getClass()) &&
                (this.forest.getThing(midRow, midColumn) == null)
            ) {
                new Squirrel(this.forest, midRow, midColumn);
                reproduced++;
            }
        }
        return reproduced;
    }

    private void move() {
        List<Integer[]> neighborCells = this.neighborCells(1);
        Thing cellThing = this;
        Integer[] newPosition = new Integer[]{-1, -1};
        while (cellThing != null && !neighborCells.isEmpty()) {
            int randomIndex = RANDOM.nextInt(neighborCells.size());
            newPosition = neighborCells.get(randomIndex);
            cellThing = this.forest.getThing(newPosition[0], newPosition[1]);
            neighborCells.remove(randomIndex);
        }
        if (cellThing == null) {
            this.forest.setThing(this.row, this.column, null);

            this.row = newPosition[0];
            this.column = newPosition[1];
            this.forest.setThing(this.row, this.column, this);
        }
    }

    private List<Integer[]> neighborCells(int distance) {
        List<Integer[]> neighborCells = new ArrayList<>();
        int forestSize = this.forest.getSize();

        for(int dr=-1; dr<2; dr++){
            for (int dc=-1; dc<2; dc++){
                int dr_ = dr * distance;
                int dc_ = dc * distance;
                if (
                    (dr_!=0 || dc_!=0) &&
                    ((0<=this.row+dr_) && (this.row+dr_<forestSize)) &&
                    ((0<=this.column+dc_) && (this.column+dc_<forestSize))
                    // (this.forest.getThing(this.row+dr, this.column+dc) == null)
                ) {
                    neighborCells.add(new Integer[] {this.row+dr_, this.column+dc_});
                };
            }
        }
        return neighborCells;
    }

    public void die(){
        this.dead = true;
        this.forest.setThing(this.row, this.column,null);
    }
}