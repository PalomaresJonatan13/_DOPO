package tests.tower;
import tower.*;

import static org.junit.Assert.*;

class TestUtils {
    public static void assertTowerState(
        Tower tower, String[][] expectedItems, int[] expectedHeights, int[] expectedLiddedCups, boolean expectedOk
    ) {
        assertArrayEquals(expectedItems, tower.stackingItems());
        assertArrayEquals(expectedHeights, tower.heightReachedByItems());
        assertArrayEquals(expectedLiddedCups, tower.liddedCups());
        assertEquals(expectedOk, tower.ok());
    }
}
