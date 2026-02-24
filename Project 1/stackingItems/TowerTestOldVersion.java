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
        tower.setShowMessages(false);
        tower.pushCup(2);
        tower.pushCup(2);

        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushLidIfItAlreadyExists() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushLid(2);
        tower.pushLid(2);

        String [][] expectedItems = {{"lid", "2"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushCupWithNonpositiveIndex() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushCup(0);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushLidWithNonpositiveIndex() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushLid(0);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushCupWithIndexGreaterThanWidth() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushCup(6);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushLidWithIndexGreaterThanWidth() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushLid(6);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushFirstCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 1);
        tower.setShowMessages(false);
        tower.pushCup(2);
        
        String[][] expectedItems = {};
        int[] expectedHeights = {};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushLidIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 1);
        tower.setShowMessages(false);
        tower.pushCup(1);
        tower.pushLid(1);
        
        String[][] expectedItems = {{"cup", "1"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldNotPushCupIfTowerVisibleAndHeightReachedIsGreaterThanTowersHeight() {
        Tower tower = new Tower(5, 10);
        tower.setShowMessages(false);
        tower.pushCup(2);
        tower.pushCup(5); // this will reach height 12
        
        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldPushValidFirstCup() {
        Tower tower = new Tower(5, 10);
        tower.pushCup(2);
        
        String[][] expectedItems = {{"cup", "2"}};
        int[] expectedHeights = {3};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    @Test
    public void shouldPushValidFirstLid() {
        Tower tower = new Tower(5, 10);
        tower.pushLid(2);
        
        String[][] expectedItems = {{"lid", "2"}};
        int[] expectedHeights = {1};
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
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
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
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
        assertArrayEquals(expectedHeights, tower.getHeightOfItems());
    }

    
/*
    @Test
    public void shouldPushCupAfterBiggerCup() {
        // last cup reached the towers height, but the new cup is smaller, so it should be pushed
    }

    @Test
    public void shouldPushCupAfterBiggerCup() {
        // last cup reached the towers height, but the new cup is smaller, so it should be pushed
    }

    @Test
    public void shouldPushLidAfterBiggerCup() {
        // last cup reached the towers height, but the new lid is smaller, so it should be pushed
    }
    
    @Test
    public void shouldPushCupAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        // .
    }
    
    @Test
    public void shouldPushLidAfterLidOrSmallerCupIfNewHeightIsLessThanTowersHeight() {
        // .
    }
*/
    
    // popCup, popLid
    // ------------------------------------------------------------------------------------------------------------
/*
    @Test
    public void shouldNot() {
        //
    }
    
    @Test
    public void f() {
        //
    }
    
    @Test
    public void f() {
        //
    }
    
    @Test
    public void f() {
        //
    }
*/
    @After
    public void conclude() {
        //
    }
}