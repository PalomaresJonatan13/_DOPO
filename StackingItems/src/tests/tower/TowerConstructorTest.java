package tests.tower;
import tower.*;

import static org.junit.Assert.*;
// import java.beans.Transient;
import org.junit.Test;


public class TowerConstructorTest {
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

    // TowerC2 constructor
    // ------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldNotCreateTowerWithNonpositiveNumberOfCups() {
        try {
            new Tower(0);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldCreateTowerWithSeveralCups() {
        Tower tower = new Tower(5);
        assertTrue(tower.ok());
    }
}
