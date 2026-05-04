package domain;

import java.util.List;

public class CursedTree extends Tree {
    private Forest forest;
    private boolean dead = false;

    public CursedTree(Forest forest, int row, int column) {
        super(forest, row, column);
        this.forest = forest;
    }

    public void ticTac() {
        super.ticTac();

        if (!this.dead) {
            List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column);
            for (Integer[] neighborCell : neighborCells) {
                Thing neighbor = this.forest.getThing(neighborCell[0], neighborCell[1]);
                if ((neighbor != null) && (neighbor.isLivingThing())) {
                    LivingThing livingThing = (LivingThing) neighbor;
                    for (int j=0; j<30; j++) livingThing.step();
                    if (livingThing.getEnergy() <= 0) livingThing.die();
                }
            }
        }
    }

    public void die(){
        this.dead = true;
        forest.setThing(row, column,null);
    }
}
