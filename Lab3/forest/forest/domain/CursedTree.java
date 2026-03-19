package domain;
import java.awt.Color;
import java.util.List;

public class CursedTree extends Tree {
    private Forest forest;
    private int tictac = 0;
    private boolean dead = false;

    public CursedTree(Forest forest, int row, int column) {
        super(forest, row, column);
        this.forest = forest;
        this.tictac=0;
    }

    public void ticTac() {
        this.tictac++;
        // System.out.println(tictac);
        color=(tictac % 4==0? Color.PINK:
               tictac % 4==1? Color.GREEN:
               tictac % 4==2? Color.ORANGE:
               Color.GRAY);
        if (tictac % 4 == 1) {
            years+=1;
        }
        if (tictac % 4 == 3) {
            boolean OK=step();
            if (! OK) {
                die();
            }
        }
        if (!this.dead) {
            List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column, 1);
            for (Integer[] neighborCell : neighborCells) {
                Thing neighbor = this.forest.getThing(neighborCell[0], neighborCell[1]);
                if ((neighbor != null) && (neighbor.isLivingThing())) {
                    for (int j=0; j<50; j++) ((LivingThing) neighbor).step();
                }
            }
        }
    }

    public void die(){
        this.dead = true;
        forest.setThing(row, column,null);
    }
}
