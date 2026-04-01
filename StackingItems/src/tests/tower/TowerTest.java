package tests.tower;
import tower.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerTest {
    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(5, 10);
        tower.makeInvisible();
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
    public void shouldNotPushCupIfItAlreadyExists() {
        TestUtils.shouldNotPushCupIfItAlreadyExists(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPushLidIfItAlreadyExists() {
        TestUtils.shouldNotPushLidIfItAlreadyExists(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPushCupWithNonpositiveIndex() {
        TestUtils.shouldNotPushCupWithNonpositiveIndex(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPushLidWithNonpositiveIndex() {
        TestUtils.shouldNotPushLidWithNonpositiveIndex(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPushCupWithIndexGreaterThanWidth() {
        TestUtils.shouldNotPushCupWithIndexGreaterThanWidth(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPushLidWithIndexGreaterThanWidth() {
        TestUtils.shouldNotPushLidWithIndexGreaterThanWidth(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushValidFirstCup() {
        TestUtils.shouldPushValidFirstCup(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushValidFirstLid() {
        TestUtils.shouldPushValidFirstLid(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushValidCupAfterItemsWithLowerIndex() {
        TestUtils.shouldPushValidCupAfterItemsWithLowerIndex(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushValidLidAfterItemsWithLowerIndex() {
        TestUtils.shouldPushValidLidAfterItemsWithLowerIndex(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        TestUtils.shouldPushCupAfterBiggerCup(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        TestUtils.shouldPushLidAfterBiggerCup(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        TestUtils.shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        TestUtils.shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldCoverACupAfterPushingItsLidOnTopOfIt() {
        TestUtils.shouldCoverACupAfterPushingItsLidOnTopOfIt(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopCupIfThereAreNoCups() {
        TestUtils.shouldNotPopCupIfThereAreNoCups(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotPopLidIfThereAreNoLids() {
        TestUtils.shouldNotPopLidIfThereAreNoLids(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupIfThereAreLidsAbove() {
        TestUtils.shouldPopCupIfThereAreLidsAbove(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopLidIfThereAreCupsAbove() {
        TestUtils.shouldPopLidIfThereAreCupsAbove(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupIfItIsTheLastItem() {
        TestUtils.shouldPopCupIfItIsTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopLidIfItIsTheLastItem() {
        TestUtils.shouldPopLidIfItIsTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveCupIfTheCupIsNotInTheTower() {
        TestUtils.shouldNotRemoveCupIfTheCupIsNotInTheTower(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldNotRemoveLidIfTheLidIsNotInTheTower() {
        TestUtils.shouldNotRemoveLidIfTheLidIsNotInTheTower(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupIfTheCupIsNotTheLastItem() {
        TestUtils.shouldRemoveCupIfTheCupIsNotTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveLidIfTheLidIsNotTheLastItem() {
        TestUtils.shouldRemoveLidIfTheLidIsNotTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupIfTheCupIsTheLastItem() {
        TestUtils.shouldRemoveCupIfTheCupIsTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveLidIfTheLidIsTheLastItem() {
        TestUtils.shouldRemoveLidIfTheLidIsTheLastItem(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, TestUtils.NORMAL_CUP, TestUtils.NORMAL_LID);
    }

    // height
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
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

    // reverseTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReverseTowerIfTheTowerIsEmpty() {
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "4"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 2, 3, 4, 9, 8},
            new int[] {},
            true
        );
    }

    // orderTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldOrderTowerIfTheTowerIsEmpty() {
        tower.orderTower();

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
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

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "4"}, {"cup", "3"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}, {"lid", "1"}},
            new int[] {1, 6, 7, 8, 9, 10},
            new int[] {1, 3},
            true
        );
    }
}