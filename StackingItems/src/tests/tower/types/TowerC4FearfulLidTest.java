package tests.tower.types;
import tower.*;
import tests.tower.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC4FearfulLidTest {
    private Tower tower;
    private String cupType = TestUtils.NORMAL_CUP;
    private String lidType = TestUtils.FEARFUL_LID;

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
        tower.pushCup(2, cupType);
        tower.pushCup(3, cupType);
        tower.pushLid(2, lidType);
        tower.pushLid(2, lidType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}, {"lid", "2"}},
            new int[] {3, 8, 5},
            new int[] {},
            false
        );
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
    public void shouldPushValidLidAfterItemsWithLowerIndex() {
        tower.pushCup(3, cupType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(2, lidType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "2"}},
            new int[] {5, 4, 3, 5},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        tower.pushCup(2, cupType);
        tower.pushCup(3, cupType);
        tower.pushLid(2, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}, {"lid", "2"}},
            new int[] {3, 8, 5},
            new int[] {},
            true
        );
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        tower.pushCup(3);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushLid(3, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "4"}, {"cup", "2"}, {"lid", "1"}, {"lid", "3"}},
            new int[] {5, 6, 9, 8, 10},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCoverACupAfterPushingItsLidOnTopOfIt() {
        tower.pushLid(1);
        tower.pushCup(4, cupType);
        tower.pushLid(3);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"lid", "3"}, {"cup", "2"}, {"lid", "4"}},
            new int[] {1, 8, 3, 6, 9},
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
        tower.pushCup(3, cupType);
        tower.pushLid(4);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "4"}, {"cup", "2"}},
            new int[] {5, 6, 9},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(tower, cupType, lidType);
    }
}