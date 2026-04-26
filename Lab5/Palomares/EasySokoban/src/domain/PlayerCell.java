package domain;

import java.util.*;

/**
 * Player cell singleton per game instance.
 */
class PlayerCell extends Cell {
    private static HashMap<EasySokoban, PlayerCell> playerCells = new HashMap<>();

    /**
     * Creates a player cell at the given position.
     *
     * @param sokoban game instance that owns the player.
     * @param row row index in the board.
     * @param column column index in the board.
     */
    private PlayerCell(EasySokoban sokoban, int row, int column) {
        super(sokoban, row, column);
        this.type = PLAYER;
        this.fixed = false;
        this.replaceable = false;
    }

    /**
     * Returns the player cell for the game, creating it when missing and updating its position otherwise.
     *
     * @param sokoban game instance that owns the player.
     * @param row target row for the player.
     * @param column target column for the player.
     * @return player cell for the provided game instance.
     */
    public static Cell getCell(EasySokoban sokoban, int row, int column) {
        PlayerCell playerCell = playerCells.get(sokoban);
        if (playerCell == null) {
            playerCell = new PlayerCell(sokoban, row, column);
            playerCells.put(sokoban, playerCell);
        } else {
            playerCell.row = row;
            playerCell.column = column;
        }
        return playerCell;
    }

    /**
     * Returns the existing player cell for a game instance.
     *
     * @param sokoban game instance that owns the player.
     * @return player cell, or {@code null} if not created yet.
     */
    public static Cell getCell(EasySokoban sokoban) {
        return playerCells.get(sokoban);
    }
}
