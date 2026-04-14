package tests.tower.types;
import tower.*;
import tests.tower.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC4CrazyLidTest {
    private Tower tower;
    private String cupType = TestUtils.NORMAL_CUP;
    private String lidType = TestUtils.CRAZY_LID;

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
    public void shouldNotPushLidIfItAlreadyExists() {
        TestUtils.shouldNotPushLidIfItAlreadyExists(tower, cupType, lidType);
    }

    @Test
    public void shouldNotPushLidWithNonpositiveIndex() {
        TestUtils.shouldNotPushLidWithNonpositiveIndex(tower, cupType, lidType);
    }

    @Test
    public void shouldNotPushLidWithIndexGreaterThanWidth() {
        TestUtils.shouldNotPushLidWithIndexGreaterThanWidth(tower, cupType, lidType);
    }

    @Test
    public void shouldPushValidFirstLid() {
        TestUtils.shouldPushValidFirstLid(tower, cupType, lidType);
    }

    @Test
    public void shouldPushValidLidAfterItemsWithLowerIndex() {
        TestUtils.shouldPushValidLidAfterItemsWithLowerIndex(tower, cupType, lidType);
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        TestUtils.shouldPushLidAfterBiggerCup(tower, cupType, lidType);
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        TestUtils.shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(tower, cupType, lidType);
    }

    @Test
    public void shouldCoverACupAfterPushingItsLidOnTopOfIt() {
        tower.pushLid(1, lidType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"lid", "4"}, {"cup", "4"}, {"lid", "3"}, {"cup", "2"}},
            new int[] {1, 2, 9, 4, 7},
            new int[] {4},
            true
        );
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPopLidIfThereAreCupsAbove() {
        TestUtils.shouldPopLidIfThereAreCupsAbove(tower, cupType, lidType);
    }

    @Test
    public void shouldPopLidIfItIsTheLastItem() {
        TestUtils.shouldPopLidIfItIsTheLastItem(tower, cupType, lidType);
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
        tower.pushLid(5);
        tower.pushCup(4, cupType);
        tower.pushCup(3);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        tower.pushCup(1);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"cup", "3"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {1, 6, 5, 4},
            new int[] {},
            true
        );
    }

    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldRemoveLidIfTheLidIsNotTheLastItem() {
        TestUtils.shouldRemoveLidIfTheLidIsNotTheLastItem(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveLidIfTheLidIsTheLastItem() {
        TestUtils.shouldRemoveLidIfTheLidIsTheLastItem(tower, cupType, lidType);
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