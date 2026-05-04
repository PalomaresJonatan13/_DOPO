package tests;
import domain.*;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ForestTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SquirrelTest {
    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
    }

    @Test
    public void shouldReproduceTwoNeighborSquirrelsWithAnEmtpyIntermediateCell() {
        int[][] positions = {
            {1, 1}, {1, 2}, {1, 3},
            {2, 1}, {2, 3},
            {3, 1}, {3, 3},
            {4, 1}, {4, 3},
            {5, 1}, {5, 2}, {5, 3}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }

        Squirrel s1 = new Squirrel(forest, 2, 2);
        Squirrel s2 =new Squirrel(forest, 4, 2);

        forest.ticTac();
        Thing thingInIntermediatecell = forest.getThing(3, 2);
        assertNotNull(thingInIntermediatecell);
        assertNotEquals(s1, thingInIntermediatecell);
        assertNotEquals(s2, thingInIntermediatecell);
        assertTrue(thingInIntermediatecell instanceof Squirrel);
    }

    @Test
    public void shouldNotHaveASquirrelDoingTicTacTwice() {
        int[][] positions = {
            {0, 2}, {1, 0}, {1, 1}, {1, 2}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }

        new Squirrel(forest, 0, 0);
        forest.ticTac();
        assertNull(forest.getThing(0, 0));
        assertTrue(forest.getThing(0, 1) instanceof Squirrel);
    }

    @Test
    public void shouldMoveHorizontallyToTheOnlyAvailableCell() {
        int[][] positions = {
            {0, 2}, {1, 0}, {1, 1}, {1, 2}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }

        new Squirrel(forest, 0, 1);
        forest.ticTac();
        assertNull(forest.getThing(0, 1));
        assertTrue(forest.getThing(0, 0) instanceof Squirrel);
    }

    @Test
    public void shouldMoveVerticallyToTheOnlyAvailableCell() {
        int[][] positions = {
            {2, 0}, {0, 1}, {1, 1}, {2, 1}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }

        new Squirrel(forest, 0, 0);
        forest.ticTac();
        assertNull(forest.getThing(0, 0));
        assertTrue(forest.getThing(1, 0) instanceof Squirrel);
    }

    @Test
    public void shouldMoveDiagonallyToTheOnlyAvailableCell() {
        int[][] positions = {
            {0, 1}, {1, 0}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }

        new Squirrel(forest, 0, 0);
        forest.ticTac();
        assertNull(forest.getThing(0, 0));
        assertTrue(forest.getThing(1, 1) instanceof Squirrel);
    }

    @After
    public void tearDown() {
        //
    }
}