package tests.tower.types;
import tower.*;
import tests.tower.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC4OpenerCupTest {
    private Tower tower;
    private String cupType = TestUtils.OPENER_CUP;
    private String lidType = TestUtils.NORMAL_LID;

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
        TestUtils.shouldNotPushCupIfItAlreadyExists(tower, cupType, lidType);
    }

    @Test
    public void shouldNotPushCupWithNonpositiveIndex() {
        TestUtils.shouldNotPushCupWithNonpositiveIndex(tower, cupType, lidType);
    }

    @Test
    public void shouldNotPushCupWithIndexGreaterThanWidth() {
        TestUtils.shouldNotPushCupWithIndexGreaterThanWidth(tower, cupType, lidType);
    }

    @Test
    public void shouldPushValidFirstCup() {
        TestUtils.shouldPushValidFirstCup(tower, cupType, lidType);
    }

    @Test
    public void shouldPushValidCupAfterItemsWithLowerIndex() {
        TestUtils.shouldPushValidCupAfterItemsWithLowerIndex(tower, cupType, lidType);
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        TestUtils.shouldPushCupAfterBiggerCup(tower, cupType, lidType);
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        TestUtils.shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(tower, cupType, lidType);
    }

    @Test
    public void shouldCoverACupAfterPushingItsLidOnTopOfIt() {
        TestUtils.shouldCoverACupAfterPushingItsLidOnTopOfIt(tower, cupType, lidType);
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopCupIfThereAreNoCups() {
        TestUtils.shouldNotPopCupIfThereAreNoCups(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupIfThereAreLidsAbove() {
        TestUtils.shouldPopCupIfThereAreLidsAbove(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupIfItIsTheLastItem() {
        tower.pushCup(2, cupType);
        tower.pushCup(4, cupType);
        tower.pushCup(1, cupType);
        tower.pushCup(3, cupType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "4"}, {"cup", "1"}},
            new int[] {3, 10, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveCupIfTheCupIsNotInTheTower() {
        TestUtils.shouldNotRemoveCupIfTheCupIsNotInTheTower(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupIfTheCupIsNotTheLastItem() {
        tower.pushCup(4, cupType);
        tower.pushCup(3, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(4, lidType);
        tower.removeCup(2);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {7, 6, 3, 8},
            new int[] {4},
            true
        );
    }

    @Test
    public void shouldRemoveCupIfTheCupIsTheLastItem() {
        TestUtils.shouldRemoveCupIfTheCupIsTheLastItem(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }
}