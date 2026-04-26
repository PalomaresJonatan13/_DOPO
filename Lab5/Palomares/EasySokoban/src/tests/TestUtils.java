package tests;
import domain.*;

import java.util.*;
import static org.junit.Assert.*;

/**
 * Helper assertions and movement setup utilities for Sokoban tests.
 */
public class TestUtils {
    public static final int EMPTY     = EasySokoban.EMPTY;
    public static final int BLOCK     = EasySokoban.BLOCK;
    public static final int BOX       = EasySokoban.BOX;
    public static final int FIXED_BOX = EasySokoban.FIXED_BOX;
    public static final int TARGET    = EasySokoban.TARGET;
    public static final int PLAYER    = EasySokoban.PLAYER;

    private static final int[][] testBoard;
    static {
        int em = EMPTY;
        int bl = BLOCK;
        int bo = BOX;
        int fi = FIXED_BOX;
        int ta = TARGET;
        int pl = PLAYER;
        testBoard = new int[][] {
            {bl, bl, bl, bl, bl, bl, bl, bl, bl},
            {bl, bl, bl, em, ta, em, em, bl, bl},
            {bl, em, em, em, fi, bo, em, bl, bl},
            {bl, em, bo, em, em, em, em, em, bl},
            {bl, ta, fi, em, pl, em, fi, ta, bl},
            {bl, em, em, em, em, em, bo, em, bl},
            {bl, bl, em, bo, fi, em, em, em, bl},
            {bl, bl, em, em, ta, em, bl, bl, bl},
            {bl, bl, bl, bl, bl, bl, bl, bl, bl}
        };
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Asserts that a board has valid structure and expected fixed-box count.
     *
     * @param sokoban game under test.
     * @param boxesInTarget expected number of fixed boxes.
     */
    private static void assertElementsInBoard(EasySokoban sokoban, int boxesInTarget) {
        int[][] board = sokoban.getBoard();
        try {
            int fixedBoxes = EasySokoban.checkHasBorderAndCorrectNumberOfObjects(board);
            assertEquals(boxesInTarget, fixedBoxes);
        } catch (SokobanException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Asserts the full state right after creating a board.
     *
     * @param sokoban game under test.
     * @param width expected board width.
     * @param height expected board height.
     * @param boxesInTarget expected number of fixed boxes.
     */
    public static void assertJustCreatedBoardState(EasySokoban sokoban, int width, int height, int boxesInTarget) {
        assertEquals(width, sokoban.getWidth());
        assertEquals(height, sokoban.getHeight());
        assertEquals((int) (0.1f*width*height), sokoban.getCellsPerType());
        assertBoardState(sokoban, boxesInTarget);
    }

    /**
     * Asserts that current board counters and composition match expectations.
     *
     * @param sokoban game under test.
     * @param boxesInTarget expected number of fixed boxes.
     */
    public static void assertBoardState(EasySokoban sokoban, int boxesInTarget) {
        assertEquals(boxesInTarget, sokoban.boxesInTarget());
        assertElementsInBoard(sokoban, boxesInTarget);
    }

    /**
     * Asserts player position coordinates.
     *
     * @param sokoban game under test.
     * @param row expected row.
     * @param column expected column.
     */
    public static void assertPlayerInPosition(EasySokoban sokoban, int row, int column) {
        Map<String, Integer> playerPosition = sokoban.getPlayerPosition();
        assertEquals(row, (int) playerPosition.get("row"));
        assertEquals(column, (int) playerPosition.get("column"));
    }

    /**
     * Asserts cell type at a specific board position.
     *
     * @param sokoban game under test.
     * @param row board row.
     * @param column board column.
     * @param type expected cell type.
     */
    public static void assertTypeInPosition(EasySokoban sokoban, int row, int column, int type) {
        int[][] board = sokoban.getBoard();
        assertEquals(type, board[row][column]);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Verifies that moving into a chain of blocked objects throws an impossible-move exception.
     *
     * @param sokoban game under test.
     * @param playerRow expected player row before and after failed move.
     * @param playerColumn expected player column before and after failed move.
     * @param direction attempted direction.
     */
    public static void shouldThrowWhenMovesAndThereAreSeveralObjectsNotLettingItMove(EasySokoban sokoban, int playerRow, int playerColumn, char direction) {
        EasySokoban.checkValidDirection(direction);
        Map<String, Integer> d = getDifferenceInDirection(direction);
        try {
            // arange
            sokoban.move(movementRotation(direction, 1));
            sokoban.move(movementRotation(direction, 2));
            sokoban.move(movementRotation(direction, 2));
            sokoban.move(movementRotation(direction, 1));
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"), BOX);
            assertTypeInPosition(sokoban, playerRow+2*d.get("row"), playerColumn+2*d.get("column"), FIXED_BOX);

            // act
            sokoban.move(direction);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.IMPOSSIBLE_MOVE, e.getMessage());
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"), BOX);
            assertTypeInPosition(sokoban, playerRow+2*d.get("row"), playerColumn+2*d.get("column"), FIXED_BOX);
            assertBoardState(sokoban, 4);
        }
    }

    /**
     * Verifies that attempting to move into a non-empty target cell throws an exception.
     *
     * @param sokoban game under test.
     * @param playerRow expected player row before and after failed move.
     * @param playerColumn expected player column before and after failed move.
     * @param direction attempted direction.
     * @param cellType blocking cell type expected in front of the player.
     */
    public static void shouldThrowAttemptingToMoveToANonEmptyCell(EasySokoban sokoban, int playerRow, int playerColumn, char direction, int cellType) {
        EasySokoban.checkValidDirection(direction);
        Map<String, Integer> d = getDifferenceInDirection(direction);
        try {
            // arange
            moveBeforeMainMove(sokoban, direction, cellType);
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"), cellType);
            
            // act
            sokoban.move(direction);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.IMPOSSIBLE_MOVE, e.getMessage());
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"), cellType);
            assertBoardState(sokoban, 4);
        }
    }

    /**
     * Verifies that the player successfully moves into an adjacent empty cell.
     *
     * @param sokoban game under test.
     * @param playerRow initial player row.
     * @param playerColumn initial player column.
     * @param direction movement direction.
     */
    public static void shouldMoveToAnEmptyCell(EasySokoban sokoban, int playerRow, int playerColumn, char direction) {
        EasySokoban.checkValidDirection(direction);
        Map<String, Integer> d = getDifferenceInDirection(direction);
        try {
            // arange
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban,  playerRow+d.get("row"), playerColumn+d.get("column"), EMPTY);

            // act
            sokoban.move(direction);

            // assert
            assertPlayerInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"));
            assertTypeInPosition(sokoban, playerRow, playerColumn, EMPTY);
            assertBoardState(sokoban, 4);
        } catch (SokobanException e) {
            fail("Should not have thrown an exception.");
        }
    }

    /**
     * Verifies that a box push updates board and counters as expected.
     *
     * @param sokoban game under test.
     * @param playerRow initial player row.
     * @param playerColumn initial player column.
     * @param direction movement direction.
     * @param expectedTypes map with keys {@code old} and {@code new} for destination cell state.
     */
    public static void shouldMoveBox(EasySokoban sokoban, int playerRow, int playerColumn, char direction, Map<String, Integer> expectedTypes) {
        EasySokoban.checkValidDirection(direction);
        Map<String, Integer> d = getDifferenceInDirection(direction);
        try {
            // arange
            moveBeforeMovingBox(sokoban, direction, expectedTypes.get("old")==EMPTY);
            assertPlayerInPosition(sokoban, playerRow, playerColumn);
            assertTypeInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"), BOX);
            assertTypeInPosition(sokoban, playerRow+2*d.get("row"), playerColumn+2*d.get("column"), expectedTypes.get("old"));

            // act
            sokoban.move(direction);

            // assert
            assertPlayerInPosition(sokoban, playerRow+d.get("row"), playerColumn+d.get("column"));
            assertTypeInPosition(sokoban, playerRow, playerColumn, EMPTY);
            assertTypeInPosition(sokoban, playerRow+2*d.get("row"), playerColumn+2*d.get("column"),  expectedTypes.get("new"));
            assertBoardState(sokoban, (expectedTypes.get("old")==EMPTY ? 4 : 5));
        } catch (SokobanException e) {
            fail("Should not have thrown an exception.");
        }
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Executes setup moves so that the next main move faces a specific non-empty cell type.
     *
     * @param sokoban game under test.
     * @param direction target direction for the main move.
     * @param cellType desired blocking cell type.
     * @throws SokobanException when any setup move is invalid.
     */
    private static void moveBeforeMainMove(EasySokoban sokoban, char direction, int cellType) throws SokobanException {
        EasySokoban.checkValidDirection(direction);
        switch (cellType) {
            case FIXED_BOX -> {
                sokoban.move(movementRotation(direction, 1));
                sokoban.move(movementRotation(direction, 2));
                sokoban.move(movementRotation(direction, 1));
            } case TARGET -> {
                sokoban.move(movementRotation(direction, 3));
                sokoban.move(movementRotation(direction, 2));
                sokoban.move(movementRotation(direction, 3));
                sokoban.move(movementRotation(direction, 3));
            } case BLOCK -> {
                sokoban.move(movementRotation(direction, 3));
                sokoban.move(movementRotation(direction, 0));
                sokoban.move(movementRotation(direction, 0));
                sokoban.move(movementRotation(direction, 0));
            }
        }
    }

    /**
     * Executes setup moves so that the next push targets an empty or target cell.
     *
     * @param sokoban game under test.
     * @param direction target push direction.
     * @param toEmptyCell {@code true} to push into empty, {@code false} to push into target.
     * @throws SokobanException when any setup move is invalid.
     */
    private static void moveBeforeMovingBox(EasySokoban sokoban, char direction, boolean toEmptyCell) throws SokobanException {
        EasySokoban.checkValidDirection(direction);
        if (toEmptyCell) {
            sokoban.move(direction);
            sokoban.move(movementRotation(direction, 1));
        } else {
            sokoban.move(movementRotation(direction, 1));
            sokoban.move(movementRotation(direction, 2));
            sokoban.move(movementRotation(direction, 1));
            sokoban.move(movementRotation(direction, 2));
            sokoban.move(movementRotation(direction, 1));
        }
    }

    /**
     * Rotates a direction clockwise a number of quarter turns.
     *
     * @param direction base direction.
     * @param rotations number of clockwise rotations (0 to 3).
     * @return rotated direction.
     */
    private static char movementRotation(char direction, int rotations) {
        if (rotations < 0 || rotations > 3) throw new IllegalArgumentException("The number of rotations should be between 0 and 3");
        if (rotations == 0) return direction;
        return switch (direction) {
            case EasySokoban.UP -> movementRotation(EasySokoban.RIGHT, rotations-1);
            case EasySokoban.RIGHT -> movementRotation(EasySokoban.DOWN, rotations-1);
            case EasySokoban.DOWN -> movementRotation(EasySokoban.LEFT, rotations-1);
            case EasySokoban.LEFT -> movementRotation(EasySokoban.UP, rotations-1);
            default -> throw new IllegalArgumentException(SokobanException.INVALID_DIRECTION);
        };
    }

    /**
     * Returns row/column delta for a movement direction.
     *
     * @param direction movement direction.
     * @return map with keys {@code row} and {@code column}.
     */
    private static Map<String, Integer> getDifferenceInDirection(char direction) {
        return switch (direction) {
            case EasySokoban.UP -> Map.of("row", -1, "column", 0);
            case EasySokoban.RIGHT -> Map.of("row", 0, "column", 1);
            case EasySokoban.DOWN -> Map.of("row", 1, "column", 0);
            case EasySokoban.LEFT -> Map.of("row", 0, "column", -1);
            default -> throw new IllegalArgumentException(SokobanException.INVALID_DIRECTION);
        };
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns the static board used by tests.
     *
     * @return test board matrix.
     */
    public static int[][] getTestBoard() {
        return testBoard;
    }

    /**
     * Creates a Sokoban instance from the shared test board.
     *
     * @return test Sokoban instance.
     * @throws SokobanException when board validation fails.
     */
    public static EasySokoban getTestSokoban() throws SokobanException {
        return EasySokoban.createSokobanFromBoard(getTestBoard());
    }
}
