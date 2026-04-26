package domain;

import java.util.*;

/**
 * Base type for every board cell, including movement and replacement behavior.
 */
class Cell {
    protected EasySokoban sokoban;
    protected int row;
    protected int column;
    protected int type;
    protected boolean fixed;
    protected boolean replaceable;
    
    public static final int EMPTY     =  0;
    public static final int BLOCK     =  1;
    public static final int BOX       =  2;
    public static final int FIXED_BOX =  3;
    public static final int TARGET    =  4;
    public static final int PLAYER    = -1;

    public static final Map<String, Integer> types = Map.of(
        "EMPTY", EMPTY,
        "BLOCK", BLOCK,
        "FIXED_BOX", FIXED_BOX,
        "BOX", BOX,
        "TARGET", TARGET,
        "PLAYER", PLAYER
    );

    /**
     * Creates a cell and places it on the board.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    protected Cell(EasySokoban sokoban, int row, int column) {
        this.sokoban = sokoban;
        this.row = row;
        this.column = column;
        sokoban.setCell(this);
    }

    /**
     * Factory method that returns the concrete cell implementation for a type id.
     *
     * @param sokoban game instance that owns the board.
     * @param row row index in the board.
     * @param column column index in the board.
     * @param type numeric type identifier.
     * @return concrete cell instance.
     */
    public static Cell getCell(EasySokoban sokoban, int row, int column, int type) {
        return switch (type) {
            case EMPTY      -> new EmptyCell(sokoban, row, column);
            case BLOCK      -> new BlockCell(sokoban, row, column);
            case BOX        -> new BoxCell(sokoban, row, column);
            case FIXED_BOX  -> new BoxCell(sokoban, row, column, true);
            case TARGET     -> new TargetCell(sokoban, row, column);
            case PLAYER     -> PlayerCell.getCell(sokoban, row, column);
            default         -> throw new IllegalArgumentException("Invalid cell type.");
        };
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns this cell row index.
     *
     * @return row index.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns this cell column index.
     *
     * @return column index.
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Returns this cell position as row and column entries.
     *
     * @return immutable map with {@code row} and {@code column}.
     */
    public Map<String, Integer> getPosition() {
        return Map.of("row", this.row, "column", this.column);
    }

    /**
     * Returns the numeric type of this cell.
     *
     * @return cell type id.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Indicates whether the cell is fixed in place.
     *
     * @return {@code true} when this cell cannot move.
     */
    public boolean isFixed() {
        return this.fixed;
    }

    /**
     * Indicates whether another cell can replace this one.
     *
     * @return {@code true} when this cell can be replaced.
     */
    public boolean isReplaceable() {
        return this.replaceable;
    }

    /**
     * Moves this cell one step in the given direction when game rules allow it.
     *
     * @param direction movement direction.
     * @throws SokobanException when the move is not possible.
     */
    public void move(char direction) throws SokobanException {
        EasySokoban.checkValidDirection(direction);
        if (this.fixed) throw new SokobanException(SokobanException.MOVE_FIXED_CELL);

        Cell adjacentCell = this.getAdjacentCell(direction);
        if (adjacentCell.isReplaceable() || !adjacentCell.isFixed()) try {
            adjacentCell.replaceWith(this); // if its type target, just by the box
        } catch (SokobanException e) {
            if (!adjacentCell.isFixed() && adjacentCell.isAdjacentReplaceable(direction)) {
                adjacentCell.move(direction);
                Map<String, Integer> position = this.getPositionFromDirection(direction);
                this.replace(position.get("row"), position.get("column"));
            } else throw new SokobanException(SokobanException.IMPOSSIBLE_MOVE);
        } else throw new SokobanException(SokobanException.IMPOSSIBLE_MOVE);
    }

    /**
     * Computes the coordinates one step away in the given direction.
     *
     * @param direction movement direction.
     * @return immutable map with target {@code row} and {@code column}.
     */
    protected Map<String, Integer> getPositionFromDirection(char direction) {
        EasySokoban.checkValidDirection(direction);

        HashMap<String, Integer> position = new HashMap<>();
        position.put("row", -1);
        position.put("column", -1);

        switch (direction) {
            case EasySokoban.UP -> {
                position.put("row", this.row-1);
                position.put("column", this.column);
            } case EasySokoban.RIGHT -> {
                position.put("row", this.row);
                position.put("column", this.column+1);
            } case EasySokoban.DOWN -> {
                position.put("row", this.row+1);
                position.put("column", this.column);
            } case EasySokoban.LEFT -> {
                position.put("row", this.row);
                position.put("column", this.column-1);
            }
        }
        return Map.copyOf(position);
    }

    /**
     * Returns the adjacent cell in the given direction.
     *
     * @param direction movement direction.
     * @return adjacent cell.
     */
    protected Cell getAdjacentCell(char direction) {
        EasySokoban.checkValidDirection(direction);
        Map<String, Integer> position = this.getPositionFromDirection(direction);
        int row = position.get("row");
        int column = position.get("column");
        return this.sokoban.getCell(row, column);
    }

    /**
     * Checks whether the adjacent cell can be replaced.
     *
     * @param direction direction to inspect.
     * @return {@code true} when the adjacent cell is replaceable.
     */
    public boolean isAdjacentReplaceable(char direction) {
        EasySokoban.checkValidDirection(direction);
        Cell adjacentCell = this.getAdjacentCell(direction);
        return adjacentCell.isReplaceable();
    }

    /**
     * Replaces the cell at the given position with this cell.
     *
     * @param row target row.
     * @param column target column.
     * @throws SokobanException when the target cell cannot be replaced.
     */
    protected void replace(int row, int column) throws SokobanException {
        Cell cellToReplace = this.sokoban.getCell(row, column);
        cellToReplace.replaceWith(this);
    }

    /**
     * Replaces this cell with another one when replacement is allowed.
     *
     * @param newCell incoming cell.
     * @throws SokobanException when this cell is not replaceable.
     */
    public void replaceWith(Cell newCell) throws SokobanException {
        if (this.isReplaceable()) {
            this.sokoban.setCell(Cell.getCell(this.sokoban, newCell.row, newCell.column, EMPTY));
            this.sokoban.setCell(Cell.getCell(this.sokoban, this.row, this.column, newCell.getType()));
        } else {
            throw new SokobanException(SokobanException.IRREPLACEABLE_CELL);
        }
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Checks whether a numeric id is a valid domain cell type.
     *
     * @param type numeric type id.
     * @return {@code true} when type is part of {@link #types}.
     */
    public static boolean isAValidType(int type) {
        return types.containsValue(type);
    }
}
