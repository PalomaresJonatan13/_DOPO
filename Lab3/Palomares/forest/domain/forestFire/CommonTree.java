package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

public class CommonTree extends ForestFireThing {
    private int energy;

    public CommonTree(Forest forest,int row, int column){
        super(forest, row, column);
        this.energy = 100;
        this.color = new Color(56, 118, 29);
    }

    public void ticTac() {
        this.energy--;
        super.ticTac();
    }

    protected void interactWithFire() {
        if (this.energy < 50) {
            this.energy -= 25;
            if (this.energy < 10) {
                new Fire(this.forest, this.row, this.column);
            }
        }
    }

    protected void interactWithWater(int waterRow, int waterColumn) {
        this.energy = 100;
        new Land(this.forest, waterRow, waterColumn);
    }

    protected void interactWithLand(List<Integer[]> neighborCells) {
        Random random = new Random();
        int randomIndex = random.nextInt(neighborCells.size());

        Integer[] newTreeCell = neighborCells.get(randomIndex);
        new CommonTree(this.forest, newTreeCell[0], newTreeCell[1]);
    }
}
