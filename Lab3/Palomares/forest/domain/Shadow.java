package domain;

public class Shadow implements Thing {
    private Forest forest;
    private int row, column;
    private int tictac;

    public Shadow(Forest forest, int row, int column){
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.tictac = 0;
        this.forest.setThing(row, column, (Thing) this);
    }

    public int shape() {
        return Thing.SHADOW;
    }

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
}