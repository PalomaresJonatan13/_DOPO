package domain.forestFire;
import domain.*;

import java.awt.Color;

public class Water extends ForestFireThing {
    public Water(Forest forest,int row, int column){
        super(forest, row, column);
        this.color = new Color(19, 147, 198);
    }

    protected void interactWithLand() {
        int forestSize = this.forest.getSize();
        if (this.row+1 < forestSize) {
            new Water(this.forest, this.row+1, this.column);
            new Land(this.forest, this.row, this.column);
        }
    }
}
