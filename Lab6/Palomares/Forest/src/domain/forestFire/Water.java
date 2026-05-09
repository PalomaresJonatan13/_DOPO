package domain.forestFire;
import domain.*;

import java.awt.Color;

/**
 * Agua; puede extenderse hacia abajo cuando interactúa con tierra.
 */
public class Water extends ForestFireThing {
    /**
     * Agua en la celda indicada.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public Water(Forest forest,int row, int column){
        super(forest, row, column);
        this.color = new Color(19, 147, 198);
    }

    /**
     * Si hay fila inferior, crea agua debajo y deja tierra en esta celda.
     */
    protected void interactWithLand() {
        int forestSize = this.forest.getSize();
        if (this.row+1 < forestSize) {
            new Water(this.forest, this.row+1, this.column);
            new Land(this.forest, this.row, this.column);
        }
    }
}
