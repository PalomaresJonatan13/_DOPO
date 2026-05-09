package domain;

/**
 * Sombra que intenta desplazarse hacia arriba en el bosque (con wrap en el borde superior).
 */
public class Shadow implements Thing {
    private Forest forest;
    private int row, column;
    private int tictac;

    /**
     * Coloca la sombra en la celda indicada.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public Shadow(Forest forest, int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.tictac = 0;
        this.forest.setThing(row, column, (Thing) this);
    }

    /**
     * @return {@link Thing#SHADOW}
     */
    public int shape() {
        return Thing.SHADOW;
    }

    /**
     * Si la celda superior está vacía, sube una fila.
     */
    public void ticTac() {
        if (this.tictac+1 > this.forest.getTictac()) return;

        this.tictac++;
        int previousRow = (this.row == 0) ? this.forest.getSize()-1 : this.row-1;
        Thing thingAbove = this.forest.getThing(previousRow, this.column);
        if (thingAbove == null) {
            this.forest.setThing(this.row, this.column, null);
            this.row = previousRow;
            this.forest.setThing(this.row, this.column, this);
        }
    }

    /**
     * @return representación {@code Shadow fila,columna}
     */
    @Override
    public String toString() {
        return String.format("%s %d,%d", this.getClass().getSimpleName(), this.row, this.column);
    }
}
