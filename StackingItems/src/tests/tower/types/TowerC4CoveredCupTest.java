package tests.tower.types;
import tower.*;
import tests.tower.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TowerC4CoveredCupTest {
    private Tower tower;
    private String cupType = TestUtils.COVERED_CUP;
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
        tower.pushCup(2, cupType);
        tower.pushCup(2, cupType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "2"}},
            new int[] {3, 4},
            new int[] {2},
            false
        );
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
        tower.pushCup(2, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "2"}},
            new int[] {3, 4},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldPushValidCupAfterItemsWithLowerIndex() {
        tower.pushCup(4);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3, cupType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"cup", "1"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {7, 4, 3, 9, 10},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        tower.pushLid(1, lidType);
        tower.pushCup(5);
        tower.pushCup(2, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "5"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {1, 10, 5, 6},
            new int[] {2},
            true
        );
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        tower.pushCup(2);
        tower.pushLid(1, lidType);
        tower.pushCup(3, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {3, 2, 8, 9},
            new int[] {3},
            true
        );
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPopCupIfThereAreLidsAbove() {
        tower.pushCup(4);
        tower.pushCup(3, cupType);
        tower.pushLid(2, lidType);
        tower.pushLid(5, lidType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {7, 2, 8},
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
        tower.pushCup(5);
        tower.pushCup(3, cupType);
        tower.pushCup(2);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "2"}},
            new int[] {9, 4},
            new int[] {},
            true
        );
    }

    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldRemoveCupIfTheCupIsNotTheLastItem() {
        tower.pushCup(4);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1);
        tower.pushLid(4, lidType);
        tower.removeCup(2);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "3"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {7, 2, 3, 8},
            new int[] {4},
            true
        );
    }

    @Test
    public void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        TestUtils.shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(tower, cupType, lidType);
    }

    @Test
    public void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween() {
        tower.pushCup(5);
        tower.pushCup(3, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2);
        tower.pushLid(1, lidType);
        tower.removeLid(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "2"}, {"lid", "1"}},
            new int[] {9, 4, 3},
            new int[] {},
            true
        );
    }
}
