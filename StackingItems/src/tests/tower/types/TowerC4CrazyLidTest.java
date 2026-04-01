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
        TestUtils.shouldCoverACupAfterPushingItsLidOnTopOfIt(tower, cupType, lidType);
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopLidIfThereAreNoLids() {
        TestUtils.shouldNotPopLidIfThereAreNoLids(tower, cupType, lidType);
    }

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
        TestUtils.shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveLidIfTheLidIsNotInTheTower() {
        TestUtils.shouldNotRemoveLidIfTheLidIsNotInTheTower(tower, cupType, lidType);
    }

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