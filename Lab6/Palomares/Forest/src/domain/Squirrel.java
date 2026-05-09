package domain;
import java.awt.Color;
import java.util.*;

/**
 * Ardilla que se mueve al azar, reproduce si hay otra ardilla a distancia 2 con celda intermedia libre,
 * y muere si no puede mantener energía.
 */
public class Squirrel extends LivingThing implements Thing {
    private Forest forest;
    private int row, column;
    private Color color;
    private int tictac;
    private boolean dead;

    private static Random RANDOM;
    static {
        RANDOM = new Random();
    }

    /**
     * Crea una ardilla en la celda dada y la coloca en el bosque.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public Squirrel(Forest forest, int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column; 
        this.color = new Color(150, 75, 0);
        this.tictac = 0;
        this.dead = false;
        this.forest.setThing(row, column, (Thing) this);
    }

    /**
     * Interpola el color entre marrón y tono más claro según el paso de simulación.
     * @param totalIterations pasos totales de referencia
     * @param iteration índice actual (acotado)
     */
    private void changeColor(int totalIterations, int iteration) {
        float relativeIteration = ((float) iteration) / ((float) totalIterations);
        this.color = new Color(
            (int) (150 + 105*relativeIteration),
            (int) (75 + 150*relativeIteration),
            0
        );
    }

    /**
     * @return fila actual
     */
    public final int getRow(){
        return this.row;
    }

    /**
     * @return columna actual
     */
    public final int getColumn(){
        return this.column;
    }

    /**
     * @return color de dibujo
     */
    public final Color getColor(){
        return this.color;
    }

    /**
     * Ejecuta movimiento, reproducción y envejecimiento si sigue viva.
     */
    public void ticTac() {
        if (this.tictac+1 > this.forest.getTictac()) return;
        this.tictac++;
        
        boolean ok = !this.dead;
        if (ok) {
            this.changeColor(40, Math.min(tictac, 40));
            int steps = (int) Math.floor(2.5 + (this.tictac%2)*0.5);
            for (int j=0; j<steps; j++) ok = this.step();
            if (this.tictac % 4 == 0){
                this.years+=1;
            }
        }
        if (!ok) this.die();
        else {
            this.reproduce();
            this.move();
        }
    }

    /**
     * Intenta crear una ardilla hija en una celda intermedia entre esta y otra ardilla vecina a distancia 2.
     * @return número de crías creadas en este tic-tac
     */
    private int reproduce() {
        int reproduced = 0;
        List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column, 2);
        for (Integer[] neighborCell : neighborCells) {
            int r = neighborCell[0];
            int c = neighborCell[1];
            int midRow = (this.row + r)/2;
            int midColumn = (this.column + c)/2;
            if (
                (this.forest.getThing(r, c) != null) &&
                (this.forest.getThing(r, c).getClass() == this.getClass()) &&
                (this.forest.getThing(midRow, midColumn) == null)
            ) {
                new Squirrel(this.forest, midRow, midColumn);
                reproduced++;
            }
        }
        return reproduced;
    }

    /**
     * Elige una celda vecina libre al azar y se desplaza si existe.
     */
    private void move() {
        List<Integer[]> neighborCells = Forest.neighborCells(this.forest, this.row, this.column);
        Thing cellThing = this;
        Integer[] newPosition = new Integer[]{-1, -1};
        while (cellThing != null && !neighborCells.isEmpty()) {
            int randomIndex = RANDOM.nextInt(neighborCells.size());
            newPosition = neighborCells.get(randomIndex);
            cellThing = this.forest.getThing(newPosition[0], newPosition[1]);
            neighborCells.remove(randomIndex);
        }
        if (cellThing == null) {
            this.forest.setThing(this.row, this.column, null);

            this.row = newPosition[0];
            this.column = newPosition[1];
            this.forest.setThing(this.row, this.column, this);
        }
    }

    /**
     * Marca la ardilla como muerta y vacía su celda.
     */
    public void die(){
        this.dead = true;
        this.forest.setThing(this.row, this.column,null);
    }

    /**
     * @return representación {@code Squirrel fila,columna}
     */
    @Override
    public String toString() {
        return String.format("%s %d,%d", this.getClass().getSimpleName(), this.row, this.column);
    }
}
