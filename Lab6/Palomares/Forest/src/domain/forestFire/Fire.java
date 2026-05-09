package domain.forestFire;
import domain.*;

import java.awt.Color;

/**
 * Fuego de forma cuadrada; el agua lo apaga y la tierra puede extinguirlo tras varios tic-tacs.
 */
public class Fire extends ForestFireThing {
    private int tictacsSurrounded;

    /**
     * Crea fuego en la celda indicada.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public Fire(Forest forest,int row, int column){
        super(forest, row, column);
        this.color = new Color(222, 14, 14);
        this.tictacsSurrounded = 0;
    }

    /**
     * @return {@link domain.Thing#SQUARE}
     */
    public int shape() {
        return Thing.SQUARE;
    }

    /**
     * El agua vecina convierte esta celda en {@link Land}.
     * @param row fila (no usada; API heredada)
     * @param column columna (no usada)
     */
    protected void interactWithWater(int row, int column) {
        new Land(this.forest, this.row, this.column);
    }

    /**
     * Tras varias llamadas rodeado solo por tierra, se apaga.
     */
    protected void interactWithLand() {
        if (tictacsSurrounded++ == 5) {
            new Land(this.forest, this.row, this.column);
        }
    }
}
