package tests;
import domain.*;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlackHoleTest {
    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
    }

    @Test
    public void shouldNotAffectAnObjectWithAShapeDifferentFromSquare() {
        new BlackHole(forest, 0, 0);
        new Shadow(forest, 1, 0);
        
        forest.ticTac();
        assertNotNull(forest.getThing(0, 0));
        assertNotNull(forest.getThing(1, 0));
    }

    @Test
    public void shouldDissapearIfThereAreNoThingsToAffectAfter12TicTacs() {
        new BlackHole(forest, 0, 0);
        new BlackHole(forest, 0, 1);
        new Shadow(forest, 1, 1);
        
        for (int k=0; k<12; k++) forest.ticTac();
        assertNull(forest.getThing(0, 0));
        assertTrue(forest.getThing(0, 1) instanceof Shadow);
        assertNull(forest.getThing(1, 1));
    }

    @Test
    public void shouldDeleteItself30PercentOfTheTimeIfThereIsANeighborThatCanBeAffected() {
        int timesDeleted = 0;
        for (int j=0; j<1000; j++) {
            forest = new Forest();
            new BlackHole(forest, 0, 0);
            new Squirrel(forest, 0, 1);
            
            forest.ticTac();
            if (forest.getThing(0, 0) == null) timesDeleted++;
        }
        assertTrue(Math.abs(timesDeleted - 300) < 50);
    }

    @Test
    public void shouldDeleteItselfAndReplaceANeighborWithABlackHole22_5PercentOfTheTime() {
        int timesAffected = 0;
        for (int j=0; j<1000; j++) {
            new BlackHole(forest, 0, 0);
            new Squirrel(forest, 0, 1);
            
            forest.ticTac();
            if (forest.getThing(0, 1) instanceof BlackHole) timesAffected++;
        }
        assertTrue(Math.abs(timesAffected - 225) < 50);
    }

    @Test
    public void shouldDeleteItselfAndANeighbor7_5PercentOfTheTime() {
        int timesDeleted = 0;
        for (int j=0; j<1000; j++) {
            new BlackHole(forest, 0, 0);
            new Squirrel(forest, 0, 1);
            
            forest.ticTac();
            if (forest.getThing(0, 0) == null && forest.getThing(0, 1) == null) timesDeleted++;
        }
        assertTrue(Math.abs(timesDeleted - 75) < 50);
    }
}