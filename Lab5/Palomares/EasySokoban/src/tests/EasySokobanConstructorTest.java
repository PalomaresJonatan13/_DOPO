package tests;
import domain.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class EasySokobanConstructorTest {
    private EasySokoban sokoban;

    public static final int em = TestUtils.EMPTY;
    public static final int bl = TestUtils.BLOCK;
    public static final int bo = TestUtils.BOX;
    public static final int fi = TestUtils.FIXED_BOX;
    public static final int ta = TestUtils.TARGET;
    public static final int pl = TestUtils.PLAYER;

    @Test
    public void shouldCreateDefaultBoard() {
        sokoban = EasySokoban.createDefaultSokoban();
        TestUtils.assertJustCreatedBoardState(sokoban, sokoban.getWidth(), sokoban.getHeight(), 4);
    }

    @Test
    public void shouldCreateBoardWithDefaultSize() {
        sokoban = new EasySokoban();
        TestUtils.assertJustCreatedBoardState(sokoban, sokoban.getWidth(), sokoban.getHeight(), 0);
    }

    @Test
    public void shouldNotCreateBoardWithNegativeWidth() {
        try {
            new EasySokoban(-1, 8);
            fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldNotCreateBoardWithNegativeHeight() {
        try {
            new EasySokoban(8, -1);
            fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldNotCreateBoardWithWidthLessThan4() {
        try {
            new EasySokoban(3, 8);
            fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals(SokobanException.INVALID_DIMENSIONS, e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateBoardWithHeightLessThan4() {
        try {
            new EasySokoban(8, 3);
            fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals(SokobanException.INVALID_DIMENSIONS, e.getMessage());
        }
    }

    @Test
    public void shouldCreateBoardWithValidDimensions() {
        sokoban = new EasySokoban(4, 8);
        TestUtils.assertJustCreatedBoardState(sokoban, 4, 8, 0);
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheDimensionsAreNotValid() {
        // arange
        int[][] board = {
            {bl, bl, bl},
            {bl, pl, bl},
            {bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (IllegalArgumentException | SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_DIMENSIONS, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheBorderIsNotComplete() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl},
            {bl, pl, bl, bl},
            {em, bo, ta, bl},
            {bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_BOARD, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheNumberOfEmptyCellsOrPlayersIsNotCorrect() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl, bl},
            {bl, pl, bl, em, bl},
            {bl, bo, ta, pl, bl},
            {bl, bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_BOARD, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheNumberOfInnerBlockCellsOrTargetsIsNotCorrect() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl, bl},
            {bl, pl, ta, bl, bl},
            {bl, bo, ta, fi, bl},
            {bl, bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_BOARD, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheNumberOfBoxesIsNotCorrect() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl, bl},
            {bl, pl, bl, bl, bl},
            {bl, bo, ta, ta, bl},
            {bl, bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_BOARD, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAttemptingToCreateSokobanFromBoardIfTheNumberOfPlayersIsNotCorrect() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl, bl},
            {bl, bo, bl, bl, bl},
            {bl, bo, ta, ta, bl},
            {bl, bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban.createSokobanFromBoard(board);
            fail("Didn't throw an exception.");
        } catch (SokobanException e) {
            // assert
            assertEquals(SokobanException.INVALID_BOARD, e.getMessage());
        }
    }

    @Test
    public void shouldCreateSokobanFromBoard() {
        // arange
        int[][] board = {
            {bl, bl, bl, bl, bl},
            {bl, pl, bl, bl, bl},
            {bl, bo, ta, fi, bl},
            {bl, bl, bl, bl, bl}
        };

        try {
            // act
            EasySokoban sokoban = EasySokoban.createSokobanFromBoard(board);

            // assert
            TestUtils.assertJustCreatedBoardState(sokoban, sokoban.getWidth(), sokoban.getHeight(), 1);
        } catch (IllegalArgumentException | SokobanException e) {
            fail("Should not have thrown an exception.");
        }
    }
}
