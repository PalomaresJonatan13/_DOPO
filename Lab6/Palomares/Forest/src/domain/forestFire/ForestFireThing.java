package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

public abstract class ForestFireThing implements Thing {
    protected Forest forest;
    protected int row,column;
    protected Color color;

    public ForestFireThing(Forest forest,int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.color = Color.BLACK;
        this.forest.setThing(row, column, (Thing) this);    
    }

    public Color getColor() {
        return this.color;
    }

    public void ticTac() {
        List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column);
        int landNeighbors = 0;

        int j = 0;
        while (j < neighborCells.size() && this.forest.getThing(this.row, this.column) == this) {
            Integer[] neighborCell = neighborCells.get(j);
            int row = neighborCell[0];
            int column = neighborCell[1];

            Thing neighbor = this.forest.getThing(row, column);
            if (neighbor instanceof Fire) {
                this.interactWithFire();
            } else if (neighbor instanceof Water) {
                this.interactWithWater(row, column);
            } else if (neighbor instanceof Land) {
                landNeighbors++;
            }
            j++;
        }
        
        if (landNeighbors == neighborCells.size()){
            this.interactWithLand(neighborCells);
        }
    }

    protected void interactWithFire() {};

    protected void interactWithWater(int waterRow, int waterColumn) {};

    protected void interactWithLand(List<Integer[]> neighborCells) {};
}
