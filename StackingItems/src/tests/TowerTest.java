package tests;
import tower.*;

import static org.junit.Assert.*;
// import java.beans.Transient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerTest {
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

    // pushCup, pushLid
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPushCupIfItAlreadyExists() {
        tower.pushCup(2);
        tower.pushCup(2);

        assertTowerState(
            new String[][] {{"cup", "2"}},
            new int[] {3},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushLidIfItAlreadyExists() {
        tower.pushLid(2);
        tower.pushLid(2);

        assertTowerState(
            new String[][] {{"lid", "2"}},
            new int[] {1},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushCupWithNonpositiveIndex() {
        tower.pushCup(0);
        
        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushLidWithNonpositiveIndex() {
        tower.pushLid(0);
        
        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushCupWithIndexGreaterThanWidth() {
        tower.pushCup(6);
        
        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPushLidWithIndexGreaterThanWidth() {
        tower.pushLid(6);
        
        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    /* @Test
    public void shouldNotPushCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        tower.makeVisible();
        // message
        tower.pushCup(2);
        tower.pushCup(5); // this will reach height 12
        
        assertTowerState(
            new String[][] {{"cup", "2"}},
            new int[] {3},
            false
        );
    } */

    /* @Test
    public void shouldNotPushLidIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        tower.makeVisible();
        // A message will be shown after trying to push the lid
        tower.pushCup(2);
        tower.pushCup(4);
        tower.pushLid(5);
        
        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "4"}},
            new int[] {3, 10},
            false
        );
    } */

    @Test
    public void shouldPushValidFirstCup() {
        tower.pushCup(2);
        
        assertTowerState(
            new String[][] {{"cup", "2"}},
            new int[] {3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushValidFirstLid() {
        tower.pushLid(2);
        
        assertTowerState(
            new String[][] {{"lid", "2"}},
            new int[] {1},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushValidCupAfterItemsWithLowerIndex() {
        tower.pushCup(4);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);

        assertTowerState(
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {7, 4, 3, 9},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushValidLidAfterItemsWithLowerIndex() {
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(2);

        assertTowerState(
            new String[][] {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "2"}},
            new int[] {5, 4, 3, 5},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        // last cup reached the towers height, but the new cup is smaller, so it should be pushed
        tower.pushLid(1);
        tower.pushCup(5);
        tower.pushCup(2);
        
        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "5"}, {"cup", "2"}},
            new int[] {1, 10, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        // last cup reached the towers height, but the new lid is smaller, so it should be pushed
        tower.pushLid(1);
        tower.pushCup(5);
        tower.pushLid(2);
        
        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "5"}, {"lid", "2"}},
            new int[] {1, 10, 3},
            new int[] {},
            true
        );
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(4);
        
        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "4"}},
            new int[] {3, 2, 10},
            new int[] {},
            true
        );
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushLid(4);
        
        assertTowerState(
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"lid", "4"}},
            new int[] {3, 2, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCoverACupAfterPushingItsLidOnTopOfIt() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(4);
        
        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"lid", "3"}, {"cup", "2"}, {"lid", "4"}},
            new int[] {1, 8, 3, 6, 9},
            new int[] {4},
            true
        );
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopCupIfThereAreNoCups() {
        // add lids and try to pop a cup
        tower.pushLid(4);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.popCup();

        assertTowerState(
            new String[][] {{"lid", "4"}, {"lid", "2"}, {"lid", "3"}},
            new int[] {1, 2, 3},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotPopLidIfThereAreNoLids() {
        // add cups and try to pop a lid
        tower.pushCup(4);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.popLid();

        assertTowerState(
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"cup", "3"}},
            new int[] {7, 4, 9},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldPopCupIfThereAreLidsAbove() {
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.popCup();

        assertTowerState(
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {7, 2, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopLidIfThereAreCupsAbove() {
        tower.pushCup(1);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.popLid();

        assertTowerState(
            new String[][] {{"cup", "1"}, {"lid", "3"}, {"cup", "2"}},
            new int[] {1, 2, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupIfItIsTheLastItem() {
        tower.pushLid(4);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.pushCup(3);
        tower.popCup();

        assertTowerState(
            new String[][] {{"lid", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {1, 2, 3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopLidIfItIsTheLastItem() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(4);
        tower.popLid();

        assertTowerState(
            new String[][] {{"cup", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.popCup();

        assertTowerState(
            new String[][] {{"lid", "2"}, {"lid", "4"}},
            new int[] {1, 2},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.popLid();

        assertTowerState(
            new String[][] {{"cup", "5"}, {"cup", "2"}},
            new int[] {9, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        tower.pushCup(1);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushLid(1);
        tower.popCup();

        assertTowerState(
            new String[][] {{"cup", "1"}, {"lid", "3"}, {"lid", "2"}, {"lid", "1"}},
            new int[] {1, 2, 3, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushCup(1);
        tower.popLid();

        assertTowerState(
            new String[][] {{"lid", "1"}, {"lid", "3"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {1, 2, 5, 4},
            new int[] {},
            true
        );
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveCupIfTheCupIsNotInTheTower() {
        tower.pushCup(4);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.removeCup(2);

        assertTowerState(
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {7, 2, 8},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotRemoveLidIfTheLidIsNotInTheTower() {
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.removeLid(3);

        assertTowerState(
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldRemoveCupIfTheCupIsNotTheLastItem() {
        // add c3, l1, c2, c1, l4, then remove c2
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(4);
        tower.removeCup(2);

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {5, 2, 3, 6},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveLidIfTheLidIsNotTheLastItem() {
        // add c3, l1, c2, c1, l4, then remove l1
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(4);
        tower.removeLid(1);

        assertTowerState(
            new String[][] {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {5, 4, 3, 6},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveCupIfTheCupIsTheLastItem() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.removeCup(1);

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "2"}},
            new int[] {5, 2, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveLidIfTheLidIsTheLastItem() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(4);
        tower.removeLid(4);

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {5, 2, 5, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(1);
        tower.removeCup(3);

        assertTowerState(
            new String[][] {{"lid", "2"}, {"lid", "4"}, {"cup", "1"}},
            new int[] {1, 2, 3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.removeLid(3);

        assertTowerState(
            new String[][] {{"cup", "5"}, {"cup", "2"}, {"lid", "1"}},
            new int[] {9, 4, 3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        tower.pushCup(5);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushCup(1);
        tower.removeCup(4);

        assertTowerState(
            new String[][] {{"cup", "5"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}},
            new int[] {9, 2, 3, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        tower.pushCup(5);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushCup(1);
        tower.removeLid(4);

        assertTowerState(
            new String[][] {{"cup", "5"}, {"lid", "3"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {9, 2, 5, 4},
            new int[] {},
            true
        );
    }

    // height
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReturnCorrectHeightIfLastItemIsSmallerThanPrevious() {
        // c4, c2
        tower.pushCup(4);
        tower.pushCup(2);

        int expectedHeight = 7;
        assertEquals(expectedHeight, tower.height());
        assertEquals(true, tower.ok());
    }

    @Test
    public void shouldReturnCorrectHeightIfLastItemIsBiggerThanPreviousAndThereIsNoGap() {
        // c1, c2, c3
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);

        int expectedHeight = 9;
        assertEquals(expectedHeight, tower.height());
        assertEquals(true, tower.ok());
    }

    @Test
    public void shouldReturnCorrectHeightIfLastItemIsBiggerThanPreviousAndThereIsAGap() {
        // c2, l1, l4
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(4);

        int expectedHeight = 10;
        assertEquals(expectedHeight, tower.height());
        assertEquals(true, tower.ok());
    }

    @Test
    public void shouldReturnCorrectHeightIfTowerInvisibleAndHeightGreaterThanTowersHeight() {
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(4);

        int expectedHeight = 15;
        assertEquals(expectedHeight, tower.height());
        assertEquals(true, tower.ok());
    }

    // makeVisible, makeInvisible
    // ------------------------------------------------------------------------------------------------------------

    /* @ Test
    public void shouldNotMakeVisibleIfHeightisGreaterThanTowersHeight() {
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(4);
        tower.makeVisible();

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "3"}, {"cup", "4"}},
            new int[] {3, 8, 15},
            false
        );
        assertFalse(tower.isVisible());
    } */

    /* @Test
    public void shouldMakeVisibleIfHeightIsLessThanOrEqualToTowersHeight() {
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.makeVisible();

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            true
        );
        assertTrue(tower.isVisible());
    } */

    /* @Test
    public void shouldMakeInvisibleIfTowerIsVisible() {
        tower.makeVisible();
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.makeInvisible();

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            true
        );
        assertFalse(tower.isVisible());
    } */

    // reverseTower
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReverseTowerIfTheTowerIsEmpty() {
        tower.reverseTower();

        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerAndExcludeItemsIfTheyDontFit() {
        // c4, c3, c2, c1, reverse, should exclude c4
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.reverseTower();

        assertTowerState(
            new String[][] {{"cup", "1"}, {"cup", "2"}, {"cup", "3"}},
            new int[] {1, 4, 9},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerAndDontExcludeItemsIfTheyFit() {
        // c4, c1, c2, reverse
        tower.pushCup(4);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.reverseTower();

        assertTowerState(
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "4"}},
            new int[] {3, 2, 10},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerWithLiddedCups() {
        tower.pushCup(2);
        tower.pushCup(3); // 3 is lidded
        tower.pushCup(1);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.reverseTower();

        assertTowerState(
            new String[][] {{"lid", "4"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 2, 3, 4, 9, 8},
            new int[] {},
            true
        );
    }

    // orderTower
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldOrderTowerIfTheTowerIsEmpty() {
        tower.orderTower();

        assertTowerState(
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldOrderTowerAndExcludeItemsIfTheyDontFit() {
        // l1, c3, l2, l3, c2, where l1 is excluded
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.orderTower();

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {5, 6, 9, 10},
            new int[] {2, 3},
            true
        );
    }

    @Test
    public void shouldOrderTowerAndDontExcludeItemsIfTheyFit() {
        // width 6, height 23
        // c4, l1, l2, c3, c2, l3, l5, c6, c5
        Tower tower = new Tower(6, 23);
        tower.makeInvisible();
        tower.pushCup(4);
        tower.pushLid(1);
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3);
        tower.pushLid(5);
        tower.pushCup(6);
        tower.pushCup(5);
        tower.orderTower();

        String[][] expectedItems = {{"cup", "6"}, {"cup", "5"}, {"lid", "5"}, {"cup", "4"}, {"cup", "3"}, {"lid", "3"}, {"cup", "2"}, {"lid", "2"}, {"lid", "1"}};
        int[] expectedHeights = {11, 10, 11, 18, 17, 18, 21, 22, 23};
        int[] expectedLiddedCups = {2, 3, 5};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
        assertArrayEquals(expectedLiddedCups, tower.liddedCups());
        assertEquals(true, tower.ok());
    }

    @Test
    public void shouldOrderTowerCorrectlyIfPairsOfCupAndLidExist() {
        // c3, c2, l2, l3
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushCup(2);
        tower.pushLid(3);
        tower.orderTower();

        assertTowerState(
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {5, 6, 9, 10},
            new int[] {2, 3},
            true
        );
    }

    @Test
    public void shouldOrderTowerWithLiddedCups() {
        tower.pushLid(2);
        tower.pushCup(3); // 3 is lidded
        tower.pushCup(1); // 1 is lidded
        tower.pushLid(1);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.orderTower();

        assertTowerState(
            new String[][] {{"lid", "4"}, {"cup", "3"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}, {"lid", "1"}},
            new int[] {1, 6, 7, 8, 9, 10},
            new int[] {1, 3},
            true
        );
    }

    @After
    public void tearDown() {
        //
    }
}