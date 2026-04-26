package tests;
import domain.*;

import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class EasySokobanTest {
    private EasySokoban sokoban;
    
    @Before
    public void setup() throws SokobanException {
        sokoban = TestUtils.getTestSokoban();
    }

    // Not move up
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldThrowWhenMovesUpAndThereAreSeveralObjectsNotLettingItMove() {
        int playerRow = 6, playerColumn = 6;
        TestUtils.shouldThrowWhenMovesAndThereAreSeveralObjectsNotLettingItMove(sokoban, playerRow, playerColumn, EasySokoban.UP);
    }

    @Test
    public void shouldThrowAttemptingToMoveUpToAFixedBoxCell() {
        int playerRow = 5, playerColumn = 6;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.UP, TestUtils.FIXED_BOX);
    }

    @Test
    public void shouldThrowAttemptingToMoveUpToATargetCell() {
        int playerRow = 5, playerColumn = 1;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.UP, TestUtils.TARGET);
    }

    @Test
    public void shouldThrowAttemptingToMoveUpToABlockCell() {
        int playerRow = 1, playerColumn = 3;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.UP, TestUtils.BLOCK);
    }

    // Move up
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldMoveUpToAnEmptyCell() {
        int playerRow = 4, playerColumn = 4;
        TestUtils.shouldMoveToAnEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.UP);
    }

    @Test
    public void shouldMoveUpAndMoveABoxToAnEmptyCell() {
        int playerRow = 3, playerColumn = 5;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.UP, Map.of("old", TestUtils.EMPTY, "new", TestUtils.BOX));
    }

    @Test
    public void shouldMoveUpAndMoveABoxToATargetCellToMakeItFixed() {
        int playerRow = 6, playerColumn = 7;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.UP, Map.of("old", TestUtils.TARGET, "new", TestUtils.FIXED_BOX));
    }

    // Not move right
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldThrowWhenMovesRightAndThereAreSeveralObjectsNotLettingItMove() {
        int playerRow = 6, playerColumn = 2;
        TestUtils.shouldThrowWhenMovesAndThereAreSeveralObjectsNotLettingItMove(sokoban, playerRow, playerColumn, EasySokoban.RIGHT);
    }

    @Test
    public void shouldThrowAttemptingToMoveRightToAFixedBoxCell() {
        int playerRow = 6, playerColumn = 3;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.RIGHT, TestUtils.FIXED_BOX);
    }

    @Test
    public void shouldThrowAttemptingToMoveRightToATargetCell() {
        int playerRow = 1, playerColumn = 3;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.RIGHT, TestUtils.TARGET);
    }

    @Test
    public void shouldThrowAttemptingToMoveRightToABlockCell() {
        int playerRow = 3, playerColumn = 7;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.RIGHT, TestUtils.BLOCK);
    }

    // Move right
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldMoveRightToAnEmptyCell() {
        int playerRow = 4, playerColumn = 4;
        TestUtils.shouldMoveToAnEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.RIGHT);
    }

    @Test
    public void shouldMoveRightAndMoveABoxToAnEmptyCell() {
        int playerRow = 5, playerColumn = 5;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.RIGHT, Map.of("old", TestUtils.EMPTY, "new", TestUtils.BOX));
    }

    @Test
    public void shouldMoveRightAndMoveABoxToATargetCellToMakeItFixed() {
        int playerRow = 7, playerColumn = 2;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.RIGHT, Map.of("old", TestUtils.TARGET, "new", TestUtils.FIXED_BOX));
    }

    // Not move down
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldThrowWhenMovesDownAndThereAreSeveralObjectsNotLettingItMove() {
        int playerRow = 2, playerColumn = 2;
        TestUtils.shouldThrowWhenMovesAndThereAreSeveralObjectsNotLettingItMove(sokoban, playerRow, playerColumn, EasySokoban.DOWN);
    }

    @Test
    public void shouldThrowAttemptingToMoveDownToAFixedBoxCell() {
        int playerRow = 3, playerColumn = 2;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.DOWN, TestUtils.FIXED_BOX);
    }

    @Test
    public void shouldThrowAttemptingToMoveDownToATargetCell() {
        int playerRow = 3, playerColumn = 7;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.DOWN, TestUtils.TARGET);
    }

    @Test
    public void shouldThrowAttemptingToMoveDownToABlockCell() {
        int playerRow = 7, playerColumn = 5;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.DOWN, TestUtils.BLOCK);
    }

    // Move down
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldMoveDownToAnEmptyCell() {
        int playerRow = 4, playerColumn = 4;
        TestUtils.shouldMoveToAnEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.DOWN);
    }

    @Test
    public void shouldMoveDownAndMoveABoxToAnEmptyCell() {
        int playerRow = 5, playerColumn = 3;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.DOWN, Map.of("old", TestUtils.EMPTY, "new", TestUtils.BOX));
    }

    @Test
    public void shouldMoveDownAndMoveABoxToATargetCellToMakeItFixed() {
        int playerRow = 2, playerColumn = 1;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.DOWN, Map.of("old", TestUtils.TARGET, "new", TestUtils.FIXED_BOX));
    }

    // Not move left
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldThrowWhenMovesLeftAndThereAreSeveralObjectsNotLettingItMove() {
        int playerRow = 2, playerColumn = 6;
        TestUtils.shouldThrowWhenMovesAndThereAreSeveralObjectsNotLettingItMove(sokoban, playerRow, playerColumn, EasySokoban.LEFT);
    }

    @Test
    public void shouldThrowAttemptingToMoveLeftToAFixedBoxCell() {
        int playerRow = 2, playerColumn = 5;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.LEFT, TestUtils.FIXED_BOX);
    }

    @Test
    public void shouldThrowAttemptingToMoveLeftToATargetCell() {
        int playerRow = 7, playerColumn = 5;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.LEFT, TestUtils.TARGET);
    }

    @Test
    public void shouldThrowAttemptingToMoveLeftToABlockCell() {
        int playerRow = 5, playerColumn = 1;
        TestUtils.shouldThrowAttemptingToMoveToANonEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.LEFT, TestUtils.BLOCK);
    }

    // Move left
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldMoveLeftToAnEmptyCell() {
        int playerRow = 4, playerColumn = 4;
        TestUtils.shouldMoveToAnEmptyCell(sokoban, playerRow, playerColumn, EasySokoban.LEFT);
    }

    @Test
    public void shouldMoveLeftAndMoveABoxToAnEmptyCell() {
        int playerRow = 3, playerColumn = 3;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.LEFT, Map.of("old", TestUtils.EMPTY, "new", TestUtils.BOX));
    }

    @Test
    public void shouldMoveLeftAndMoveABoxToATargetCellToMakeItFixed() {
        int playerRow = 1, playerColumn = 6;
        TestUtils.shouldMoveBox(sokoban, playerRow, playerColumn, EasySokoban.LEFT, Map.of("old", TestUtils.TARGET, "new", TestUtils.FIXED_BOX));
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotMoveToAnInvalidDirection() {
        try {
            sokoban.move('o');
            fail("Didn't throw an exception.");
        } catch (IllegalArgumentException | SokobanException e) {
            assertEquals(SokobanException.INVALID_DIRECTION, e.getMessage());
        }
    }

    // tests for .reset()
}
