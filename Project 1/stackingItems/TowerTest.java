import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TowerTest {
    @Before
    public void setUp() {
        //
    }

    // Tower constructor
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotCreateTowerWithNonpositiveDimensions() {
        try {
            new Tower(0, 0);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldCreateTowerWithPositiveDimensions() {
        Tower tower = new Tower(5, 10);
        assertTrue(tower.ok());
    }

    // pushCup, pushLid
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotPushCupIfItAlreadyExists() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushCup(2);
        tower.pushCup(2);

        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushLidIfItAlreadyExists() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushLid(2);
        tower.pushLid(2);

        String [][] expectedItems = {{"lid", "2"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushCupWithNonpositiveIndex() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushCup(0);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushLidWithNonpositiveIndex() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushLid(0);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushCupWithIndexGreaterThanWidth() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushCup(6);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushLidWithIndexGreaterThanWidth() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushLid(6);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushFirstCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 1);
        tower.makeInvisible();
        tower.pushCup(2);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushLidIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 1);
        tower.makeInvisible();
        tower.pushCup(1);
        tower.pushLid(1);
        
        String[][] expectedItems = {{"cup", "1"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldNotPushCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 10);
        tower.makeInvisible();
        tower.pushCup(2);
        tower.pushCup(5); // this will reach height 12
        
        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushValidFirstCup() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(2);
        
        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushValidFirstLid() {
        Tower tower = new Tower(5, 10);
        tower.pushLid(2);
        
        String[][] expectedItems = {{"lid", "2"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushValidCupAfterItemsWithLowerIndex() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushCup(3);

        String[][] expectedItems = {{"cup", "4"}, {"cup", "2"}, {"cup", "1"}, {"cup", "3"}};
        int[] expectedHeights = {7, 4, 3, 9};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushValidLidAfterItemsWithLowerIndex() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
        tower.pushLid(2);

        String[][] expectedItems = {{"cup", "3"}, {"cup", "2"}, {"cup", "1"}, {"lid", "2"}};
        int[] expectedHeights = {5, 4, 3, 5};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        // last cup reached the towers height, but the new cup is smaller, so it should be pushed
        Tower tower = new Tower(5, 9);
        tower.pushCup(5);
        tower.pushCup(2);
        
        String[][] expectedItems = {{"cup", "5"}, {"cup", "2"}};
        int[] expectedHeights = {9, 4};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        // last cup reached the towers height, but the new lid is smaller, so it should be pushed
        Tower tower = new Tower(5, 9);
        tower.pushCup(5);
        tower.pushLid(2);
        
        String[][] expectedItems = {{"cup", "5"}, {"lid", "2"}};
        int[] expectedHeights = {9, 2};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushCup(4);
        
        String[][] expectedItems = {{"cup", "2"}, {"lid", "1"}, {"cup", "4"}};
        int[] expectedHeights = {3, 2, 10};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushLid(4);
        
        String[][] expectedItems = {{"cup", "2"}, {"lid", "1"}, {"lid", "4"}};
        int[] expectedHeights = {3, 2, 4};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
    }
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldNotPopCupIfThereAreNoCups() {
        // add lids and try to pop a cup
    }
    
    @Test
    public void shouldNotPopLidIfThereAreNoLids() {
        // add cups and try to pop a lid
    }
    
    @Test
    public void shouldPopCupIfThereAreLidsAbove() {
        //
    }
    
    @Test
    public void shouldPopLidIfThereAreCupsAbove() {
        //
    }

    @Test
    public void shouldPopCupIfItIsTheLastItem() {
        //
    }

    @Test
    public void shouldPopLidIfItIsTheLastItem() {
        //
    }
*/

    // removeCup, removeLid
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldNotRemoveCupIfTheCupIsNotInTheTower() {
        //
    }

    @Test
    public void shouldNotRemoveLidIfTheLidIsNotInTheTower() {
        //
    }

    @Test
    public void shouldRemoveCupIfTheCupIsNotTheLastItem() {
        // add c3, l1, c2, c1, l4, then remove c2
    }

    @Test
    public void shouldRemoveLidIfTheLidIsNotTheLastItem() {
        // add c3, l1, c2, c1, l4, then remove l1
    }

    @Test
    public void shouldRemoveCupIfTheCupIsTheLastItem() {
        // 
    }

    @Test
    public void shouldRemoveLidIfTheLidIsTheLastItem() {
        // 
    }
*/

    // height
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldReturnCorrectHeightIfLastItemIsSmallerThanPrevious() {
        // c2, c4
    }

    @Test
    public void shouldReturnCorrectHeightIfLastItemIsBiggerThanPreviousAndThereIsNoGap() {
        // c1, c2, c3
    }

    @Test
    public void shouldReturnCorrectHeightIfLastItemIsBiggerThanPreviousAndThereIsAGap() {
        // c3, l2, l4
    }
*/

    // makeVisible, makeInvisible
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldNotMakeVisibleIfHeightisGreaterThanTowersHeight() {
        //
    }

    @Test
    public void shouldMakeVisibleIfHeightIsLessThanOrEqualToTowersHeight() {
        //
    }

    @Test
    public void shouldMakeInvisibleIfTowerIsVisible() {
        //
    }
*/

    // reverseTower
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldReverseTowerIfTheTowerIsEmpty() {
        //
    }

    @Test
    public void shouldReverseTowerAndExcludeItemsIfTheyDontFit() {
        // c4, c3, c2, c1, reverse, should exclude c4
    }

    @Test
    public void shouldReverseTowerAndDontExcludeItemsIfTheyFit() {
        // c4, c1, c2, reverse
    }
*/

    // orderTower
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldOrderTowerIfTheTowerIsEmpty() {
        //
    }

    @Test
    public void shouldOrderTowerAndExcludeItemsIfTheyDontFit() {
        // l1, c3, l2, l3, c2, where l1 is excluded
    }

    @Test
    public void shouldOrderTowerAndDontExcludeItemsIfTheyFit() {
        // width 6, height 23
        // c4, l1, l2, c3, c2, l3, l5, c6, c5
    }

    @Test
    public void shouldOrderTowerCorrectlyIfPairsOfCupAndLidExist() {
        // c3, c2, l2, l3
    }
*/

    @After
    public void tearDown() {
        //
    }
}