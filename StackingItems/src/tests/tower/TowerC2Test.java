package tests.tower;
import tower.*;

import static org.junit.Assert.*;
// import java.beans.Transient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC2Test {
    private Tower tower;

    private void assertTowerState(String[][] expectedItems, int[] expectedHeights,
                                    int[] expectedLiddedCups, boolean expectedOk) {
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
        assertArrayEquals(expectedLiddedCups, tower.liddedCups());
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
            new int[] {},
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
            new int[] {},
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
            new int[] {},
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
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapALiddedCupWithAnotherItem() {
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"cup", "2"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "2"}, {"lid", "4"}, {"lid", "1"}, {"lid", "3"}},
            new int[] {3, 4, 5, 6, 7},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldSwapALidCoveringACupWithAnotherItem() {
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"lid", "2"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "2"}, {"lid", "4"}, {"lid", "1"}, {"lid", "3"}},
            new int[] {3, 4, 5, 6, 7},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldSwapALiddedCupWithItemsInsideTheCupWithAnotherItem() {
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"cup", "2"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "2"}, {"lid", "4"}, {"lid", "1"}, {"cup", "1"}, {"lid", "3"}},
            new int[] {3, 4, 5, 6, 7, 8},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldSwapALidCoveringACupWithItemsInsideTheCupWithAnotherItem() {
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"lid", "2"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "2"}, {"lid", "4"}, {"cup", "1"}, {"lid", "1"}, {"lid", "3"}},
            new int[] {3, 4, 5, 6, 7, 8},
            new int[] {1, 2},
            true
        );
    }

    @Test
    public void shouldSwapALiddedItemWithAnotherLiddedItem() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.swap(
            new String[] {"lid", "3"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"lid", "4"}, {"cup", "1"}, {"lid", "1"}},
            new int[] {5, 6, 7, 8, 9},
            new int[] {1, 3},
            true
        );
    }

    @Test
    public void shouldSwapALiddedItemWithItemsInsideWithAnotherLiddedItem() {
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"lid", "3"},
            new String[] {"lid", "1"}
        );

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"lid", "4"}, {"cup", "2"}, {"cup", "1"}, {"lid", "1"}},
            new int[] {5, 6, 7, 10, 9, 10},
            new int[] {1, 3},
            true
        );
    }

    // swapToReduce  
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReturnEmptyArrayWhenAttemptingToSwapToReduceTheHeightIfTheTowerIsEmpty() {
        String[][] result = tower.swapToReduce();

        assertEquals(0, result.length);
        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReturnEmptyArrayWhenAttemptingToSwapToReduceTheHeightIfThereIsNoPairOfItemsToDoIt() {
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushCup(3);
        String[][] result = tower.swapToReduce();

        assertEquals(0, result.length);
        assertTowerState(
            new String[][] {{"cup", "1"}, {"cup", "5"}, {"cup", "2"}, {"cup", "3"}},
            new int[] {1, 10, 5, 10},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReturnTheItemsToSwapToReduceTheHeight() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(2);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();

        assertTowerState(
            new String[][] {{"cup", "4"}, {"lid", "3"}, {"cup", "1"}, {"lid", "1"}, {"lid", "2"}, {"cup", "2"}},
            new int[] {7, 2, 3, 4, 5, 8},
            new int[] {1},
            true
        );
        
        assertTrue(result.length > 0);
        if (result.length > 0) {
            int lastHeight = tower.height();
            tower.swap(result[0], result[1]);
            assertTrue(tower.height() < lastHeight);
        }
    }

    // cover    
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldCoverIfTheTowerIsEmpty() {
        tower.cover();

        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

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
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCoverIfALidIsBeforeItsCup() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.cover();

        assertTowerState(
            new String[][] {{"cup", "4"}, {"cup", "1"}, {"cup", "3"}, {"lid", "3"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {7, 2, 7, 8, 9, 10},
            new int[] {3},
            true
        );
   }

   @Test
    public void shouldCoverIfALidIsAfterItsCup() {
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.cover();

        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {3, 4, 5, 10},
            new int[] {2},
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