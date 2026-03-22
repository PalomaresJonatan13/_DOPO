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
public class ShadowTest {
    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
    }

    @Test
    public void shouldMoveUpwardsIfShadowIsNotInRow0() {
        new Shadow(forest, 1, 0);
        forest.ticTac();

        assertNull(forest.getThing(1, 0));
        assertTrue(forest.getThing(0, 0) instanceof Shadow);
    }

    @Test
    public void shouldMoveToLastRowIfShadowIsInRow0() {
        new Shadow(forest, 0, 0);
        forest.ticTac();

        assertNull(forest.getThing(0, 0));
        assertTrue(forest.getThing(forest.getSize()-1, 0) instanceof Shadow);
    }

    @Test
    public void shouldNotMoveIfTheShadowIsNotInTheFirstRowThereIsAnotherThingInTheCellAbove() {
        new Tree(forest, 0, 0);
        new Shadow(forest, 1, 0);
        forest.ticTac();

        assertTrue(forest.getThing(1, 0) instanceof Shadow);
    }

    @Test
    public void shouldNotMoveIfTheShadowIsInTheFirstRowThereIsAnotherThingInTheLastRow() {
        int lastRow = forest.getSize()-1;
        new Tree(forest, lastRow, 0);
        new Shadow(forest, 0, 0);
        forest.ticTac();

        assertTrue(forest.getThing(0, 0) instanceof Shadow);
    }

    @After
    public void tearDown() {
        //
    }
}