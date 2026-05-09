package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

/**
 * Árbol del área de incendio con energía; puede arder o regenerarse según vecinos.
 */
public class CommonTree extends ForestFireThing {
    private int energy;

    /**
     * Árbol con energía inicial 100.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public CommonTree(Forest forest,int row, int column){
        super(forest, row, column);
        this.energy = 100;
        this.color = new Color(56, 118, 29);
    }

    /**
     * Reduce energía y aplica lógica de vecinos de la superclase.
     */
    public void ticTac() {
        this.energy--;
        super.ticTac();
    }

    /**
     * Si la energía es baja, el fuego vecino puede consumir el árbol y generar {@link Fire}.
     */
    protected void interactWithFire() {
        if (this.energy < 50) {
            this.energy -= 25;
            if (this.energy < 10) {
                new Fire(this.forest, this.row, this.column);
            }
        }
    }

    /**
     * El agua vecina restaura energía y convierte esa celda de agua en {@link Land}.
     * @param waterRow fila del agua
     * @param waterColumn columna del agua
     */
    protected void interactWithWater(int waterRow, int waterColumn) {
        this.energy = 100;
        new Land(this.forest, waterRow, waterColumn);
    }

    /**
     * Rodeado solo de tierra: planta otro {@link CommonTree} en un vecino al azar.
     * @param neighborCells celdas vecinas
     */
    protected void interactWithLand(List<Integer[]> neighborCells) {
        Random random = new Random();
        int randomIndex = random.nextInt(neighborCells.size());

        Integer[] newTreeCell = neighborCells.get(randomIndex);
        new CommonTree(this.forest, newTreeCell[0], newTreeCell[1]);
    }
}
