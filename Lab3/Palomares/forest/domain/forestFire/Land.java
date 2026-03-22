package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

public class Land extends ForestFireThing {
    public Land(Forest forest,int row, int column){
        super(forest, row, column);  
        this.color =  new Color(117, 182, 106);
    }

    public void ticTac() {
        Random random = new Random();
        float randomFloat = random.nextFloat();

        if ((1-randomFloat) < 0.1) {
            new Fire(this.forest, this.row, this.column);
        } else if (randomFloat < 0.05) {
            new Water(this.forest, this.row, this.column);
        }
    }
}
