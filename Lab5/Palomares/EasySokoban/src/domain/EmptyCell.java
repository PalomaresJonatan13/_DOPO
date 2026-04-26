package domain;

/**
 * Empty floor cell that can be replaced by movable entities.
 */
class EmptyCell extends Cell {
    /**
     * Creates an empty and replaceable cell at the given position.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    public EmptyCell(EasySokoban sokoban, int row, int column) {
        super(sokoban, row, column);
        this.type = EMPTY;
        this.fixed = true;
        this.replaceable = true;
    }
}
