package tests.tower;
import tower.*;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC4Test {
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

    // General - Push
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPushNormalLidIfAnotherTypeOfLidWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushLid(2, TestUtils.CRAZY_LID);
        tower.popLid();
        tower.pushLid(2, TestUtils.NORMAL_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"lid", "2"}},
            new int[] {3, 4, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushFearfulLidIfAnotherTypeOfLidWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushLid(2);
        tower.popLid();
        tower.pushLid(2, TestUtils.FEARFUL_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"lid", "2"}},
            new int[] {3, 4, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushCrazyLidIfAnotherTypeOfLidWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushLid(2);
        tower.popLid();
        tower.pushLid(2, TestUtils.CRAZY_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"lid", "2"}},
            new int[] {3, 4, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushNormalCupIfAnotherTypeOfCupWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.popCup();
        tower.pushCup(3, TestUtils.NORMAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushOpenerCupIfAnotherTypeOfCupWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.popCup();
        tower.pushCup(3, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "3"}},
            new int[] {3, 2, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushHierarchicalCupIfAnotherTypeOfCupWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushCup(3);
        tower.popCup();
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"cup", "3"}},
            new int[] {3, 4, 9},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushCoveredCupIfAnotherTypeOfCupWasUsedAndRemoved() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushCup(3);
        tower.popCup();
        tower.pushCup(3, TestUtils.COVERED_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {3, 4, 9, 10},
            new int[] {3},
            true
        );
    }

    // General - ReverseTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReverseTowerContainingAnOpenerCup() {
        tower.pushCup(5, TestUtils.OPENER_CUP);
        tower.pushLid(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"cup", "5"}},
            new int[] {1, 10},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerContainingAHierarchicalCup() {
        tower.pushCup(5, TestUtils.HIERARCHICAL_CUP);
        tower.pushLid(4);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "1"}, {"cup", "3"}, {"lid", "4"}},
            new int[] {9, 2, 7, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerContainingACoveredCup() {
        tower.pushCup(3);
        tower.pushCup(2, TestUtils.COVERED_CUP);
        tower.pushLid(5);
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"cup", "2"}, {"lid", "2"}, {"cup", "3"}},
            new int[] {1, 4, 5, 10},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldReverseTowerContainingAFearfulLid() {
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}},
            new int[] {3, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReverseTowerContainingACrazyLid() {
        tower.pushLid(3, TestUtils.CRAZY_LID);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.reverseTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "3"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {3},
            true
        );
    }

    // General - OrderTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldOrderTowerContainingAnOpenerCup() {
        tower.pushCup(4, TestUtils.OPENER_CUP);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.orderTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {7, 4, 5},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldOrderTowerContainingAHierarchicalCup() {
        tower.pushCup(4, TestUtils.HIERARCHICAL_CUP);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.orderTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"cup", "4"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {1, 8, 5, 6},
            new int[] {2},
            true
        );
    }

    @Test
    public void shouldOrderTowerContainingACoveredCup() {
        tower.pushCup(3);
        tower.pushCup(2, TestUtils.COVERED_CUP);
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
    public void shouldOrderTowerContainingAFearfulLid() {
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(5);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.pushLid(4);
        tower.orderTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"lid", "4"}, {"cup", "3"}, {"lid", "3"}, {"cup", "1"}},
            new int[] {1, 2, 7, 8, 9},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldOrderTowerContainingACrazyLid() {
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(5);
        tower.pushLid(3, TestUtils.CRAZY_LID);
        tower.pushLid(4);
        tower.orderTower();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"lid", "4"}, {"lid", "3"}, {"cup", "3"}, {"cup", "1"}},
            new int[] {1, 2, 3, 8, 5},
            new int[] {3},
            true
        );
    }

    // General - Swap
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldSwapContainingAnOpenerCup() {
        tower.pushCup(5, TestUtils.OPENER_CUP);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.swap(
            new String[]{"cup", "5"}, 
            new String[]{"lid", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "3"}},
            new int[] {9, 6},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapContainingAHierarchicalCup() {
        tower.pushCup(5, TestUtils.HIERARCHICAL_CUP);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.swap(
            new String[]{"cup", "5"}, 
            new String[]{"cup", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "1"}, {"lid", "2"}, {"lid", "4"}, {"cup", "3"}},
            new int[] {9, 2, 3, 4, 9},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapContainingACoveredCup1() {
        tower.pushLid(2);
        tower.pushCup(3, TestUtils.COVERED_CUP);
        tower.pushCup(1);
        tower.swap(
            new String[]{"lid", "2"},
            new String[]{"lid", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}},
            new int[] {5, 6, 7, 8},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldSwapContainingACoveredCup2() {
        tower.pushLid(2);
        tower.pushCup(3, TestUtils.COVERED_CUP);
        tower.pushCup(1);
        tower.swap(
            new String[]{"lid", "2"},
            new String[]{"cup", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "3"}, {"lid", "2"}, {"cup", "1"}},
            new int[] {5, 6, 7, 8},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldSwapContainingACoveredCup3() {
        tower.pushLid(2);
        tower.pushCup(3, TestUtils.COVERED_CUP);
        tower.pushCup(1);
        tower.swap(
            new String[]{"lid", "3"},
            new String[]{"cup", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}, {"cup", "3"}, {"lid", "3"}, {"cup", "1"}},
            new int[] {1, 6, 7, 8},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldNotSwapContainingAFearfulLidThatCannotBeAddedDuringTheOperation() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(5);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.pushLid(4);
        tower.swap(
            new String[]{"lid", "3"}, 
            new String[]{"cup", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"cup", "3"}, {"lid", "5"}, {"lid", "3"}, {"lid", "4"}},
            new int[] {1, 6, 7, 8, 9},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldSwapContainingACrazyLid() {
        tower.pushLid(4, TestUtils.CRAZY_LID);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.pushLid(3);
        tower.pushLid(1);
        tower.swap(
            new String[]{"lid", "4"}, 
            new String[]{"lid", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"lid", "4"}, {"cup", "4"}, {"cup", "2"}, {"lid", "3"}},
            new int[] {1, 2, 9, 6, 7},
            new int[] {4},
            true
        );
    }

    // General - SwapToReduce
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldSwapToReduceContainingAnOpenerCup() {
        tower.pushCup(1);
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.pushLid(1);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"cup", "3"}, {"lid", "1"}, {"cup", "2"}},
            new int[] {1, 6, 3, 6},
            new int[] {},
            true
        );
        
        assertTrue(result.length > 0);
        if (result.length > 0) {
            int lastHeight = tower.height();
            tower.swap(result[0], result[1]);
            assertTrue(tower.height() < lastHeight);
        }
    }

    @Test
    public void shouldSwapToReduceContainingAHierarchicalCup() {
        tower.pushCup(2);
        tower.pushLid(5);
        tower.pushCup(4, TestUtils.HIERARCHICAL_CUP);
        tower.pushCup(3);
        tower.swapToReduce();

        String[][] result = tower.swapToReduce();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "5"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {3, 4, 11, 10},
            new int[] {},
            true
        );
        
        assertTrue(result.length > 0);
        if (result.length > 0) {
            int lastHeight = tower.height();
            tower.swap(result[0], result[1]);
            assertTrue(tower.height() < lastHeight);
        }
    }

    @Test
    public void shouldSwapToReduceContainingACoveredCup() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1, TestUtils.COVERED_CUP);
        tower.pushLid(2);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();

        TestUtils.assertTowerState(
            tower,
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

    @Test
    public void shouldSwapToReduceContainingAFearfulLid() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushLid(1, TestUtils.FEARFUL_LID);
        tower.pushLid(2);
        tower.pushCup(2);
        String[][] result = tower.swapToReduce();

        TestUtils.assertTowerState(
            tower,
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

    @Test
    public void shouldSwapToReduceContainingACrazyLid() {
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushLid(3, TestUtils.CRAZY_LID);
        String[][] result = tower.swapToReduce();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "1"}, {"cup", "2"}, {"lid", "4"}, {"lid", "3"}},
            new int[] {5, 2, 5, 6, 7},
            new int[] {},
            true
        );
        
        assertTrue(result.length > 0);
        if (result.length > 0) {
            int lastHeight = tower.height();
            tower.swap(result[0], result[1]);
            assertTrue(tower.height() < lastHeight);
        }
    }

    // General - Cover
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldCoverContainingAnOpenerCup() {
        tower.pushCup(2);
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.pushLid(2);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}},
            new int[] {3, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCoverContainingAHierarchicalCup1() {
        tower.pushCup(1);
        tower.pushLid(3);
        tower.pushCup(2, TestUtils.HIERARCHICAL_CUP);
        tower.pushCup(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {3, 2, 8, 9},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldCoverContainingAHierarchicalCup2() {
        tower = new Tower(6, 30);
        tower.makeInvisible();

        tower.pushLid(4);
        tower.pushCup(6);
        tower.pushLid(1);
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushLid(6);
        tower.pushCup(4, TestUtils.HIERARCHICAL_CUP);
        tower.pushCup(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "6"}, {"cup", "1"}, {"lid", "1"}, {"lid", "6"}, {"cup", "5"}, {"cup", "4"}, {"cup", "2"}, {"lid", "4"}, {"cup", "3"}},
            new int[] {11, 2, 3, 12, 21, 20, 17, 21, 26},
            new int[] {1, 4, 6},
            true
        );
    }

    @Test
    public void shouldCoverContainingACoveredCup() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2, TestUtils.COVERED_CUP);
        tower.pushLid(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"lid", "3"}, {"cup", "2"}, {"lid", "2"}},
            new int[] {5, 2, 6, 9, 10},
            new int[] {2, 3},
            true
        );
    }

    @Test
    public void shouldCoverContainingAFearfulLid() {
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(5);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "4"}, {"cup", "3"}, {"cup", "1"}, {"lid", "3"}, {"lid", "5"}},
            new int[] {7, 2, 8, 13, 10, 14, 15},
            new int[] {3, 4},
            true
        );
    }

    @Test
    public void shouldCoverContainingACrazyLid() {
        tower.pushLid(3, TestUtils.CRAZY_LID);
        tower.pushCup(5);
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.pushLid(1);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "5"}, {"cup", "3"}, {"cup", "1"}, {"lid", "1"}, {"cup", "2"}},
            new int[] {9, 6, 3, 4, 7},
            new int[] {1},
            true
        );
    }

    // Opener Cup
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // Push 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPushOpenerCupAndRemoveRemovableLidsThatDontLetItMove() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushCup(3, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}},
            new int[] {3, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushOpenerCupAndRemoveItsAssociatedLidIfItDontLetItMove() {
        tower.pushCup(2);
        tower.pushLid(3);
        tower.pushCup(3, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "3"}},
            new int[] {3, 8},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushOpenerCupAndRemoveRemovableLidsThatDontLetItMoveEvenIfTheyAreCoveringCups() {
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(1, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"cup", "1"}},
            new int[] {7, 6, 3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushOpenerCupAndStopRemovingLidsIfANonRemovableLidIsFound() {
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.pushLid(4);
        tower.pushCup(1, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"cup", "2"}, {"lid", "2"}, {"lid", "3"}, {"cup", "1"}},
            new int[] {7, 6, 5, 6, 7, 8},
            new int[] {2, 3},
            true
        );
    }

    @Test
    public void shouldPushOpenerCupAndDontRemoveALidIfItLetsTheCupMove() {
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(4, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "1"}, {"cup", "4"}},
            new int[] {3, 2, 10},
            new int[] {},
            true
        );
    }

    // Hierarchical Cup
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // Push 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPushHierarchicalCupAndMoveSmallerElements() {
        tower.pushCup(1);
        tower.pushLid(2);
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "1"}, {"lid", "2"}, {"lid", "1"}},
            new int[] {5, 2, 3, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushHierarchicalCupAndDontMoveBiggerElements() {
        tower.pushCup(4);
        tower.pushLid(2);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"lid", "2"}},
            new int[] {7, 6, 3},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushHierarchicalCupAndMoveElementOfSameIndex() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {7, 6, 7},
            new int[] {3},
            true
        );
    }

    // Pop
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopHierarchicalCupIfItIsAtTheBottomOfTheTower() {
        tower.pushLid(2);
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "2"}, {"lid", "1"}},
            new int[] {5, 2, 3},
            new int[] {},
            false
        );
    }

    // Remove
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveHierarchicalCupIfItIsAtTheBottomOfTheTower() {
        tower.pushCup(1);
        tower.pushLid(2);
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);
        tower.removeCup(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "1"}, {"lid", "2"}, {"lid", "1"}},
            new int[] {5, 2, 3, 4},
            new int[] {},
            false
        );
    }

    // Covered Cup
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // Push 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPushCoveredCupIfItsLidDoesNotExist() {
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.COVERED_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {1, 6, 7},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldPushCoveredCupIfItsLidDoesExist() {
        tower.pushLid(3);
        tower.pushLid(1);
        tower.pushCup(3, TestUtils.COVERED_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"lid", "3"}},
            new int[] {1, 6, 7},
            new int[] {3},
            true
        );
    }

    // Fearful Lid
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // Push 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPushFearfulLidIfCupIsNotInTower() {
        tower.pushCup(3);
        tower.pushLid(2, TestUtils.FEARFUL_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}},
            new int[] {5},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldPushFearfulLidIfCupIsInTower() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.pushLid(2, TestUtils.FEARFUL_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"lid", "2"}},
            new int[] {3, 4, 5},
            new int[] {},
            true
        );
    }

    // Pop
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPopFearfulLidIfItIsCoveringTheCup() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"lid", "3"}},
            new int[] {5, 2, 6},
            new int[] {3},
            false
        );
    }

    @Test
    public void shouldPopFearfulLidIfItIsNotCoveringTheCup() {
        tower.pushCup(3);
        tower.pushLid(4);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.popLid();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "4"}},
            new int[] {5, 6},
            new int[] {},
            true
        );
    }

    // Remove 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotRemoveFearfulLidIfItIsCoveringTheCup() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.pushLid(5);
        tower.removeLid(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "1"}, {"lid", "3"}, {"lid", "5"}},
            new int[] {5, 2, 6, 7},
            new int[] {3},
            false
        );
    }

    @Test
    public void shouldRemoveFearfulLidIfItIsNotCoveringTheCup() {
        tower.pushCup(3);
        tower.pushLid(4);
        tower.pushLid(3, TestUtils.FEARFUL_LID);
        tower.pushLid(5);
        tower.removeLid(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "4"}, {"lid", "5"}},
            new int[] {5, 6, 7},
            new int[] {},
            true
        );
    }

    // Crazy Lid 
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // Push 
    // -----------------------------------------------------------------------------------------------------------

    @Test
    public void shouldPushCrazyLidAndPlaceItUnderItsCupIfItCoversItWhenPushing() {
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushLid(3, TestUtils.CRAZY_LID);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "3"}, {"cup", "3"}, {"lid", "1"}},
            new int[] {1, 6, 3},
            new int[] {3},
            true
        );
    }

    @Test
    public void shouldPushCrazyLidAndBeDeletedIfItIsCoveringAnOpenerCup() {
        tower.pushCup(3, TestUtils.OPENER_CUP);
        tower.pushCup(2);
        tower.pushLid(3, TestUtils.CRAZY_LID);
        
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"cup", "2"}},
            new int[] {5, 4},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldPushCupOnTopOfCrazyLidWithSameIndexAndChangeTheirStateToLidded() {
        tower.pushCup(4);
        tower.pushLid(3, TestUtils.CRAZY_LID);
        tower.pushCup(3);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "3"}, {"cup", "3"}},
            new int[] {7, 2, 7},
            new int[] {3},
            true
        );
    }

    // Pop
    // -----------------------------------------------------------------------------------------------------------

    /* @Test
    public void shouldPop_________________________________________() {
        tower.pushCup(5);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(2);
        tower.pushLid(4, TestUtils.CRAZY_LID);
        tower.pushLid(5, TestUtils.CRAZY_LID);
        tower.popCup();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "5"}, {"cup", "5"}, {"lid", "3"}, {"lid", "2"}},
            new int[] {1, 10, 3, 4},
            new int[] {5},
            true
        );
    } */
}