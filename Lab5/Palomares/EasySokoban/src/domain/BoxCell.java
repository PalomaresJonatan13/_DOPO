package domain;

/**
 * Movable box cell that can become fixed when placed on a target.
 */
class BoxCell extends Cell {
    /**
     * Creates a movable box at the given board position.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    public BoxCell(EasySokoban sokoban, int row, int column) {
        super(sokoban, row, column);
        this.type = BOX;
        this.fixed = false;
        this.replaceable = false;
    }

    /**
     * Creates a box with explicit fixed state, used for boxes already in target cells.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     * @param fixed whether the box is fixed and cannot be moved.
     */
    public BoxCell(EasySokoban sokoban, int row, int column, boolean fixed) {
        this(sokoban, row, column);
        this.type = FIXED_BOX;
        this.fixed = fixed;
    }
}
