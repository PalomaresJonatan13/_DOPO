package tests;
import domain.*;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CursedTreeTest {
    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
    }

    @Test
    public void shouldDecreaseEnergyOfOtherLivingThing() {
        Tree t1 = new Tree(forest, 0, 0);
        new CursedTree(forest, 0, 1);

        forest.ticTac();
        assertEquals(70, t1.getEnergy());
    }

    @Test
    public void shouldAffectLivingThingsInAnyDirection() {
        int[][] positions = {
            {0, 0}, {0, 1}, {0, 2},
            {1, 0}, {1, 2},
            {2, 0}, {2, 1}, {2, 2}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }
        new CursedTree(forest, 1, 1);

        forest.ticTac();
        for (int[] pos : positions) {
            Tree tree = (Tree) forest.getThing(pos[0], pos[1]);
            assertEquals(70, tree.getEnergy());
        }
    }

    @Test
    public void shouldKillStaticLivingThingsInAnyDirectionAfter4TicTacs() {
        int[][] positions = {
            {0, 0}, {0, 1}, {0, 2},
            {1, 0}, {1, 2},
            {2, 0}, {2, 1}, {2, 2}
        };
        for (int[] pos : positions) {
            new Tree(forest, pos[0], pos[1]);
        }
        new CursedTree(forest, 1, 1);

        for (int j=0; j<4; j++) forest.ticTac();
        for (int[] pos : positions) {
            Thing thing = forest.getThing(pos[0], pos[1]);
            assertNull(thing);
        }
    }
}
