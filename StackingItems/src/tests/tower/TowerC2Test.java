package tests.tower;
import tower.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerC2Test {
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

    // swap
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotSwapIfItemsHaveIncorrectFormat() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.swap(
            new String[] {"c", "2"},
            new String[] {"l", "3", "whatever"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}, {"cup", "3"}},
            new int[] {1, 6},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldNotSwapIfItemsIfTheyAreNotInTower() {
        tower.pushLid(2);
        tower.pushCup(3);
        tower.swap(
            new String[] {"cup", "4"},
            new String[] {"lid", "2"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "2"}, {"cup", "3"}},
            new int[] {1, 6},
            new int[] {},
            false
        );
    }

    @Test
    public void shouldSwapItemsIfTheyAreTheSame() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.swap(
            new String[] {"cup", "3"},
            new String[] {"cup", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "4"}, {"cup", "3"}},
            new int[] {1, 8, 7},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapIfTheOrderOfTheItemsIsNotTheSameAsInTheTower() {
        tower.pushLid(1);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.swap(
            new String[] {"cup", "2"},
            new String[] {"lid", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "4"}, {"lid", "1"}},
            new int[] {3, 10, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapALiddedCupWithAnotherItemBySeparatingTheLiddedItemsInvolved() {
        tower.pushLid(1);
        tower.pushLid(4);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(3);
        tower.swap(
            new String[] {"cup", "2"},
            new String[] {"lid", "1"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"lid", "4"}, {"lid", "1"}, {"lid", "2"}, {"lid", "3"}},
            new int[] {3, 4, 5, 6, 7},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldSwapALidCoveringACupWithAnotherItemBySeparatingTheLiddedItemsInvolved() {
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushLid(4);
        tower.pushLid(3);
        tower.swap(
            new String[] {"lid", "2"},
            new String[] {"lid", "3"}
        );

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "2"}, {"lid", "3"}, {"lid", "4"}, {"lid", "2"}},
            new int[] {1, 4, 5, 6, 7},
            new int[] {},
            true
        );
    }

    // swapToReduce  
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReturnEmptyArrayWhenAttemptingToSwapToReduceTheHeightIfTheTowerIsEmpty() {
        String[][] result = tower.swapToReduce();

        assertEquals(0, result.length);
        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReturnEmptyArrayWhenAttemptingToSwapToReduceTheHeightIfThereIsNoPairOfItemsToDoIt() {
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushCup(3);
        String[][] result = tower.swapToReduce();

        assertEquals(0, result.length);
        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "1"}, {"cup", "5"}, {"cup", "2"}, {"cup", "3"}},
            new int[] {1, 10, 5, 10},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldReturnTheItemsToSwapToReduceTheHeight() {
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushLid(1);
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

    // cover    
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldCoverIfTheTowerIsEmpty() {
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {},
            new int[] {},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCoverIfThereAreNoCupsWithTheirLids() {
        tower.pushLid(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"lid", "1"}, {"cup", "3"}, {"cup", "2"}},
            new int[] {1, 6, 5},
            new int[] {},
            true
        );
    }

    @Test
    public void shouldCover_() {
        // lid covers cup without elements in between
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(5);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "1"}, {"cup", "3"}, {"lid", "3"}, {"lid", "5"}},
            new int[] {7, 2, 7, 8, 9},
            new int[] {3},
            true
        );
   }

   @Test
    public void shouldCover__() {
        // lid covers cup with a single element in between
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.pushLid(5);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "1"}, {"cup", "3"}, {"lid", "2"}, {"lid", "3"}, {"lid", "5"}},
            new int[] {7, 2, 7, 4, 8, 9},
            new int[] {3},
            true
        );
   }

   @Test
    public void shouldCover___() {
        // lid covers cup with several elements in between
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "1"}, {"cup", "2"}, {"lid", "4"}, {"cup", "3"}},
            new int[] {7, 2, 5, 8, 13},
            new int[] {4},
            true
        );
   }

   @Test
    public void shouldCover____() {
        // lid covers cup with a lidded cup in between
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushLid(5);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"lid", "2"}, {"lid", "4"}, {"cup", "3"}, {"lid", "3"}, {"lid", "5"}},
            new int[] {7, 2, 8, 13, 14, 15},
            new int[] {3, 4},
            true
        );
   }

   @Test
    public void shouldCover______() {
        // lid covers cup with an enclosed item 
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(5);
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
    public void shouldCover_______() { 
        tower.pushLid(3);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushLid(4);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "4"}, {"cup", "3"}, {"lid", "1"}, {"cup", "2"}, {"lid", "3"}, {"lid", "4"}},
            new int[] {7, 6, 3, 6, 7, 8},
            new int[] {3, 4},
            true
        );
   }

   @Test
    public void shouldCover_____() {
        // lid covers cup if the cup is the last element
        tower.pushLid(3);
        tower.pushCup(3);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "3"}, {"lid", "3"}},
            new int[] {5, 6},
            new int[] {3},
            true
        );
   }

   @Test
    public void shouldCoverIfALidIsAfterItsCup() {
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(2);
        tower.cover();

        TestUtils.assertTowerState(
            tower,
            new String[][] {{"cup", "2"}, {"cup", "1"}, {"lid", "2"}, {"cup", "3"}},
            new int[] {3, 2, 4, 9},
            new int[] {2},
            true
        );
   }
}