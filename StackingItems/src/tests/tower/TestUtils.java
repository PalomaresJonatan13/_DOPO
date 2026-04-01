package tests.tower;
import tower.*;

import static org.junit.Assert.*;

public class TestUtils {
    public static final String NORMAL_CUP = "normal";
    public static final String OPENER_CUP = "opener";
    public static final String HIERARCHICAL_CUP = "hierarchical";
    public static final String NORMAL_LID = "normal";
    public static final String FEARFUL_LID = "fearful";
    public static final String CRAZY_LID = "crazy";

    public static void assertTowerState(
        Tower tower, String[][] expectedItems, int[] expectedHeights, int[] expectedLiddedCups, boolean expectedOk
    ) {
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
        assertArrayEquals(expectedLiddedCups, tower.liddedCups());
        assertEquals(expectedOk, tower.ok());
    }

    // pushCup, pushLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public static void shouldNotPushCupIfItAlreadyExists(Tower tower, String cupType, String lidType) {
        tower.pushCup(2, cupType);
        tower.pushCup(2, cupType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}},
            new int[] {3},
            new int[] {},
            false
        );
    }

    public static void shouldNotPushLidIfItAlreadyExists(Tower tower, String cupType, String lidType) {
        tower.pushLid(2, lidType);
        tower.pushLid(2, lidType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}},
            new int[] {1},
            new int[] {},
            false
        );
    }

    public static void shouldNotPushCupWithNonpositiveIndex(Tower tower, String cupType, String lidType) {
        tower.pushCup(0, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    public static void shouldNotPushLidWithNonpositiveIndex(Tower tower, String cupType, String lidType) {
        tower.pushLid(0, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    public static void shouldNotPushCupWithIndexGreaterThanWidth(Tower tower, String cupType, String lidType) {
        tower.pushCup(6, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    public static void shouldNotPushLidWithIndexGreaterThanWidth(Tower tower, String cupType, String lidType) {
        tower.pushLid(6, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            false
        );
    }

    public static void shouldPushValidFirstCup(Tower tower, String cupType, String lidType) {
        tower.pushCup(2, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}},
            new int[] {3},
            new int[] {},
            true
        );
    }

    public static void shouldPushValidFirstLid(Tower tower, String cupType, String lidType) {
        tower.pushLid(2, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}},
            new int[] {1},
            new int[] {},
            true
        );
    }

    public static void shouldPushValidCupAfterItemsWithLowerIndex(Tower tower, String cupType, String lidType) {
        tower.pushCup(4, cupType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushCup(3, cupType);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"cup", "1"}, {"cup", "3"}},
            new int[] {7, 4, 3, 9},
            new int[] {},
            true
        );
    }

    public static void shouldPushValidLidAfterItemsWithLowerIndex(Tower tower, String cupType, String lidType) {
        tower.pushCup(3, cupType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(4, lidType); // 2

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {5, 4, 3, 6},
            new int[] {},
            true
        );
    }

    public static void shouldPushCupAfterBiggerCup(Tower tower, String cupType, String lidType) {
        // last cup reached the towers height, but the new cup is smaller, so it should be pushed
        tower.pushLid(1, lidType);
        tower.pushCup(5, cupType);
        tower.pushCup(2, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "5"}, {"cup", "2"}},
            new int[] {1, 10, 5},
            new int[] {},
            true
        );
    }

    public static void shouldPushLidAfterBiggerCup(Tower tower, String cupType, String lidType) {
        // last cup reached the towers height, but the new lid is smaller, so it should be pushed
        tower.pushLid(1, lidType);
        tower.pushCup(5, cupType);
        tower.pushLid(2, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "5"}, {"lid", "2"}},
            new int[] {1, 10, 3},
            new int[] {},
            true
        );
    }
    
    public static void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(Tower tower, String cupType, String lidType) {
        tower.pushCup(2, cupType);
        tower.pushLid(1, lidType);
        tower.pushCup(4, cupType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "4"}},
            new int[] {3, 2, 10},
            new int[] {},
            true
        );
    }
    
    public static void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight(Tower tower, String cupType, String lidType) {
        tower.pushCup(2, cupType);
        tower.pushLid(1, lidType);
        tower.pushLid(4, lidType);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"lid", "4"}},
            new int[] {3, 2, 4},
            new int[] {},
            true
        );
    }

    public static void shouldCoverACupAfterPushingItsLidOnTopOfIt(Tower tower, String cupType, String lidType) {
        tower.pushLid(1, lidType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
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

    public static void shouldNotPopCupIfThereAreNoCups(Tower tower, String cupType, String lidType) {
        // add lids and try to pop a cup
        tower.pushLid(4, lidType);
        tower.pushLid(2, lidType);
        tower.pushLid(3, lidType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "4"}, {"lid", "2"}, {"lid", "3"}},
            new int[] {1, 2, 3},
            new int[] {},
            false
        );
    }

    public static void shouldNotPopLidIfThereAreNoLids(Tower tower, String cupType, String lidType) {
        // add cups and try to pop a lid
        tower.pushCup(4, cupType);
        tower.pushCup(2, cupType);
        tower.pushCup(3, cupType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"cup", "3"}},
            new int[] {7, 4, 9},
            new int[] {},
            false
        );
    }

    public static void shouldPopCupIfThereAreLidsAbove(Tower tower, String cupType, String lidType) {
        tower.pushCup(4, cupType);
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

    public static void shouldPopLidIfThereAreCupsAbove(Tower tower, String cupType, String lidType) {
        tower.pushCup(1, cupType);
        tower.pushLid(3, lidType);
        tower.pushLid(4, lidType);
        tower.pushCup(2, cupType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"lid", "3"}, {"cup", "2"}},
            new int[] {1, 2, 5},
            new int[] {},
            true
        );
    }

    public static void shouldPopCupIfItIsTheLastItem(Tower tower, String cupType, String lidType) {
        tower.pushLid(4, lidType);
        tower.pushLid(2, lidType);
        tower.pushLid(5, lidType);
        tower.pushCup(3, cupType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {1, 2, 3},
            new int[] {},
            true
        );
    }

    public static void shouldPopLidIfItIsTheLastItem(Tower tower, String cupType, String lidType) {
        tower.pushCup(1, cupType);
        tower.pushCup(3, cupType);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {},
            true
        );
    }

    public static void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushLid(2, lidType);
        tower.pushCup(3, cupType);
        tower.pushLid(3, lidType);
        tower.pushLid(4, lidType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}, {"lid", "4"}},
            new int[] {1, 2},
            new int[] {},
            true
        );
    }

    public static void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreNoElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushCup(5, cupType);
        tower.pushCup(3, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "2"}},
            new int[] {9, 4},
            new int[] {},
            true
        );
    }

    public static void shouldPopCupAndLidIfTheCupIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushCup(1, cupType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
        tower.pushLid(2, lidType);
        tower.pushLid(4, lidType);
        tower.pushLid(1, lidType);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"lid", "3"}, {"lid", "2"}, {"lid", "1"}},
            new int[] {1, 2, 3, 4},
            new int[] {},
            true
        );
    }

    public static void shouldPopCupAndLidIfTheLidIsPoppedWhenTheyAreAttachedAndThereAreElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushLid(1, lidType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        tower.pushCup(1, cupType);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"lid", "3"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {1, 2, 5, 4},
            new int[] {},
            true
        );
    }


    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public static void shouldNotRemoveCupIfTheCupIsNotInTheTower(Tower tower, String cupType, String lidType) {
        tower.pushCup(4, cupType);
        tower.pushLid(2, lidType);
        tower.pushLid(5, lidType);
        tower.removeCup(2);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "5"}},
            new int[] {7, 2, 8},
            new int[] {},
            false
        );
    }

    public static void shouldNotRemoveLidIfTheLidIsNotInTheTower(Tower tower, String cupType, String lidType) {
        tower.pushLid(1, lidType);
        tower.pushCup(3, cupType);
        tower.pushCup(2, cupType);
        tower.removeLid(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {},
            false
        );
    }

    public static void shouldRemoveCupIfTheCupIsNotTheLastItem(Tower tower, String cupType, String lidType) {
        // add c3, l1, c2, c1, l4, then remove c2
        tower.pushCup(3, cupType);
        tower.pushLid(1, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(4, lidType);
        tower.removeCup(2);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {5, 2, 3, 6},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveLidIfTheLidIsNotTheLastItem(Tower tower, String cupType, String lidType) {
        // add c3, l1, c2, c1, l4, then remove l1
        tower.pushCup(3, cupType);
        tower.pushLid(1, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(4, lidType);
        tower.removeLid(1);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "4"}},
            new int[] {5, 4, 3, 6},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveCupIfTheCupIsTheLastItem(Tower tower, String cupType, String lidType) {
        tower.pushCup(3, cupType);
        tower.pushLid(1, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.removeCup(1);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "2"}},
            new int[] {5, 2, 5},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveLidIfTheLidIsTheLastItem(Tower tower, String cupType, String lidType) {
        tower.pushCup(3, cupType);
        tower.pushLid(1, lidType);
        tower.pushCup(2, cupType);
        tower.pushCup(1, cupType);
        tower.pushLid(4, lidType);
        tower.removeLid(4);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {5, 2, 5, 4},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushLid(2, lidType);
        tower.pushCup(3, cupType);
        tower.pushLid(3, lidType);
        tower.pushLid(4, lidType);
        tower.pushCup(1, cupType);
        tower.removeCup(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}, {"lid", "4"}, {"cup", "1"}},
            new int[] {1, 2, 3},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreNoElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushCup(5, cupType);
        tower.pushCup(3, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
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

    public static void shouldRemoveCupAndLidIfTheCupIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushCup(5, cupType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
        tower.pushLid(2, lidType);
        tower.pushLid(4, lidType);
        tower.pushCup(1, cupType);
        tower.removeCup(4);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}},
            new int[] {9, 2, 3, 4},
            new int[] {},
            true
        );
    }

    public static void shouldRemoveCupAndLidIfTheLidIsRemovedWhenTheyAreAttachedAndThereAreElementsInBetween(Tower tower, String cupType, String lidType) {
        tower.pushCup(5, cupType);
        tower.pushCup(4, cupType);
        tower.pushLid(3, lidType);
        tower.pushCup(2, cupType);
        tower.pushLid(4, lidType);
        tower.pushCup(1, cupType);
        tower.removeLid(4);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"lid", "3"}, {"cup", "2"}, {"cup", "1"}},
            new int[] {9, 2, 5, 4},
            new int[] {},
            true
        );
    }
}
