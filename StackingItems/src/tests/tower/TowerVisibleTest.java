package tests.tower;
import tower.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerVisibleTest {
    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(5, 10);
    }

    @After
    public void tearDown() {
        tower.makeInvisible();
    }

    // pushCup, pushLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPushCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        // message
        tower.pushCup(2);
        tower.pushCup(5); // this will reach height 12
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}},
            new int[] {3},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushLidIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        // A message will be shown after trying to push the lid
        tower.pushCup(2);
        tower.pushCup(4);
        tower.pushLid(5);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "4"}},
            new int[] {3, 10},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushCoveredCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        tower.pushLid(1);
        tower.pushCup(5, TestUtils.COVERED_CUP);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}},
            new int[] {1},
            new int[] {},
            false
        );
    }

    // makeVisible, makeInvisible
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldMakeInvisibleIfTowerIsVisible() {
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.makeInvisible();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            new int[] {},
            true
        );
        assertFalse(tower.isVisible());
    }

    @ Test
    public void shouldNotMakeVisibleIfHeightisGreaterThanTowersHeight() {
        tower.makeInvisible();
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(4);
        tower.makeVisible(); // no message is displayed because the tower is invisible

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}, {"cup", "4"}},
            new int[] {3, 8, 15},
            new int[] {},
            false
        );
        assertFalse(tower.isVisible());
    }

    @Test
    public void shouldMakeVisibleIfHeightIsLessThanOrEqualToTowersHeight() {
        tower.makeInvisible();
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.makeVisible();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            new int[] {},
            true
        );
        assertTrue(tower.isVisible());
    }
    
    // swap
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotSwapIfVisibleAndNewHeightIsGreaterThanTowersHeight() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.swap(
            new String[] {"lid", "1"},
            new String[] {"cup", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {1, 8, 7},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldSwapDifferentItemsIfTheyAreInAVisibleTowerAndNewHeightIsLessThanOrEqualToTowersHeight() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.swap(
            new String[] {"lid", "1"},
            new String[] {"cup", "2"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "4"}, {"lid", "1"}},
            new int[] {3, 10, 5},
            new int[] {},
            true
        );
    }

    // cover    
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotCoverIfNewHeightIsGreaterThanTowersHeight() {
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "3"}, {"lid", "4"}, {"cup", "4"}, {"lid", "1"}, {"cup", "3"}},
            new int[] {1, 2, 9, 4, 9},
            new int[] {},
            false
        );
    }
}