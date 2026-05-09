package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

/**
 * Base para celdas del subsistema de incendio: interactúan con fuego, agua y tierra vecinos.
 */
public abstract class ForestFireThing implements Thing {
    protected Forest forest;
    protected int row,column;
    protected Color color;

    /**
     * Registra la celda en el bosque con color inicial negro.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public ForestFireThing(Forest forest,int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.color = Color.BLACK;
        this.forest.setThing(row, column, (Thing) this);    
    }

    /**
     * @return color de pintura
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Recorre vecinos: reacciona a fuego y agua; si todos son tierra, llama a {@link #interactWithLand(List)}.
     */
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

    /**
     * Llamado cuando hay fuego vecino; por defecto no hace nada.
     */
    protected void interactWithFire() {};

    /**
     * Llamado cuando hay agua en {@code (waterRow, waterColumn)}; por defecto no hace nada.
     * @param waterRow fila del agua
     * @param waterColumn columna del agua
     */
    protected void interactWithWater(int waterRow, int waterColumn) {};

    /**
     * Llamado cuando todos los vecinos son tierra; por defecto no hace nada.
     * @param neighborCells lista de coordenadas vecinas
     */
    protected void interactWithLand(List<Integer[]> neighborCells) {};

    /**
     * @return representación {@code NombreClase fila,columna}
     */
    @Override
    public String toString() {
        return String.format("%s %d,%d", this.getClass().getSimpleName(), this.row, this.column);
    }
}
