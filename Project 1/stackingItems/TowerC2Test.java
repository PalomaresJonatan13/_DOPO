import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC2Test {
    private Tower tower;

    private void assertTowerState(String[][] expectedItems, int[] expectedHeights, boolean expectedOk) {
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
        assertEquals(expectedOk, tower.ok());
    }

    @Before
    public void setUp() {
        tower = new Tower(5, 10);
        tower.makeInvisible();
    }

    // swap
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotSwapIfItemsHaveIncorrectFormat() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.swap(
            new String[] {"c", "2"},
            new String[] {"l", "3", "whatever"}
        );

        assertTowerState(
            new String[][] {{"lid", "2"}, {"cup", "3"}},
            new int[] {1, 6},
            false
        );
    }

    @Test
    public void shouldNotSwapIfItemsAreNotInTower() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.swap(
            new String[] {"cup", "4"},
            new String[] {"lid", "2"}
        );

        assertTowerState(
            new String[][] {{"lid", "2"}, {"cup", "3"}},
            new int[] {1, 6},
            false
        );
    }

    /* @Test
    public void shouldNotSwapIfVisibleAndNewHeightIsGreaterThanTowersHeight() {
        tower.makeVisible();
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.swap(
            new String[] {"lid", "1"},
            new String[] {"cup", "3"}
        );

        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {1, 8, 7},
            false
        );
    } */

    @Test
    public void shouldSwapItemsIfTheyAreTheSame() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.swap(
            new String[] {"cup", "3"},
            new String[] {"cup", "3"}
        );

        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {1, 8, 7},
            true
        );
    }

    /* @Test
    public void shouldSwapDifferentItemsIfTheyAreInAVisibleTowerAndNewHeightIsLessThanOrEqualToTowersHeight() {
        tower.makeVisible();
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.swap(
            new String[] {"lid", "1"},
            new String[] {"cup", "2"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "4"}, {"lid", "1"}},
            new int[] {3, 10, 5},
            true
        );
    } */

    @Test
    public void shouldSwapIfTheOrderOfTheItemsIsNotTheSameAsInTheTower() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.swap(
            new String[] {"cup", "2"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "4"}, {"lid", "1"}},
            new int[] {3, 10, 5},
            true
        );
    }

    // cover    
    // ------------------------------------------------------------------------------------------------------------

    /* @Test
    public void shouldNotCoverIfVisibleAndNewHeightIsGreaterThanTowersHeight() {
        tower.makeVisible();
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.cover();

        assertTowerState(
            new String[][] {{"lid", "3"}, {"lid", "4"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {1, 2, 9, 8},
            false
        );
    } */

    @Test
    public void shouldCoverIfThereAreNoCupsWithTheirLids() {
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.cover();

        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            true
        );
    }

    @Test
    public void shouldCoverIfThereAreCupsWithTheirLids() {
        tower.pushLid(2);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(3);
        tower.cover();

        assertTowerState(
            new String[][] {{"lid", "2"}, {"cup", "3"}, {"lid", "3"}, {"cup", "1"}, {"lid", "1"}},
            new int[] {1, 6, 7, 8, 9},
            true
        );
    }


/*
    @Test
    public void () {
        //
    }
*/

    @After
    public void tearDown() {
        //
    }
}