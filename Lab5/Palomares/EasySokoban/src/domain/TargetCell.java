package domain;

/**
 * Target cell that accepts boxes and turns them into fixed boxes.
 */
class TargetCell extends Cell {
    /**
     * Creates a target cell at the given position.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    public TargetCell(EasySokoban sokoban, int row, int column) {
        super(sokoban, row, column);
        this.type = TARGET;
        this.fixed = true;
        this.replaceable = true;
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Replaces the target only when a box enters it.
     *
     * @param newCell incoming cell that attempts to replace this target.
     * @throws SokobanException when the incoming cell is not a box.
     */
    @Override
    public void replaceWith(Cell newCell) throws SokobanException {
        if (newCell.getType() == BOX) {
            this.sokoban.setCell(Cell.getCell(this.sokoban, newCell.row, newCell.column, EMPTY));
            this.sokoban.setCell(Cell.getCell(this.sokoban, this.row, this.column, FIXED_BOX));
        } else {
            throw new SokobanException(SokobanException.INVALID_TARGET_REPLACEMENT);
        }
    }
}
