package tests.tower;
import tower.*;

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

    // General - ReverseTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // General - OrderTower
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // General - Swap
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // General - SwapToReduce
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // General - Cover
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

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
    public void shouldPushOpenerCupAndDontRemoveALidIfItLetsTheCupMoves() {
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushCup(1, TestUtils.OPENER_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"lid", "2"}, {"cup", "1"}},
            new int[] {7, 6, 3, 4},
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
    public void shouldPushHierarchicalCupAndDontMoveElementOfSameIndex() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(3, TestUtils.HIERARCHICAL_CUP);

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "3"}, {"cup", "3"}},
            new int[] {7, 2, 7},
            new int[] {},
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
}