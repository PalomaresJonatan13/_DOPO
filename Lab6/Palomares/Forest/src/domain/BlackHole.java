package domain;

import java.util.*;

/**
 * Agujero negro que desaparece tras varios tic-tacs y puede convertir vecinos de forma cuadrada.
 */
public class BlackHole implements Thing {
    private Forest forest;
    private int row, column;
    private int tictac;

    private static Random RANDOM;
    static {
        RANDOM = new Random();
    }

    /**
     * Coloca el agujero negro en la celda dada.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public BlackHole(Forest forest, int row, int column) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.tictac = 0;
        this.forest.setThing(row, column, (Thing) this);
    }

    /**
     * @return {@link Thing#ROUND}
     */
    public int shape() {
        return Thing.ROUND;
    }

    /**
     * Tras 12 tic-tacs se elimina; con probabilidad afecta vecinos de forma {@link Thing#SQUARE}.
     */
    public void ticTac() {
        this.tictac++;
        if (this.tictac == 12) {
            this.forest.setThing(this.row, this.column, null);
        }
        List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column);
        for (Integer[] neighborCell : neighborCells) {
            if (RANDOM.nextFloat() < 0.3) {
                int row = neighborCell[0];
                int column = neighborCell[1];
                Thing neighbor = this.forest.getThing(row, column);
                if ((neighbor != null) && (neighbor.shape() == Thing.SQUARE)) {
                    Thing newThing = (RANDOM.nextFloat() < 0.75 ? new BlackHole(this.forest, row, column) : null);
                    this.forest.setThing(row, column, newThing);
                    this.forest.setThing(this.row, this.column, null);
                }
            }
        }
    }

    /**
     * @return representación {@code BlackHole fila,columna}
     */
    @Override
    public String toString() {
        return String.format("%s %d,%d", this.getClass().getSimpleName(), this.row, this.column);
    }
}
