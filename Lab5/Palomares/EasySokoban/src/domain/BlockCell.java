package domain;

/**
 * Immutable wall cell that blocks movement and replacement.
 */
class BlockCell extends Cell {
    /**
     * Creates a blocking cell at the given board position.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    public BlockCell(EasySokoban sokoban, int row, int column) {
        super(sokoban, row, column);
        this.type = BLOCK;
        this.fixed = true;
        this.replaceable = false;
    }
}
