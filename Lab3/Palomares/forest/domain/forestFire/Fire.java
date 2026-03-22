package domain.forestFire;
import domain.*;

import java.awt.Color;

public class Fire extends ForestFireThing {
    private int tictacsSurrounded;

    public Fire(Forest forest,int row, int column){
        super(forest, row, column);
        this.color = new Color(222, 14, 14);
        this.tictacsSurrounded = 0;
    }

    public int shape() {
        return Thing.SQUARE;
    }

    protected void interactWithWater(int row, int column) {
        new Land(this.forest, this.row, this.column);
    }

    protected void interactWithLand() {
        if (tictacsSurrounded++ == 5) {
            new Land(this.forest, this.row, this.column);
        }
    }
}
