package tests.contest;
import contest.*;
import tower.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TowerContestTest {
    private Tower tower;

    private void assertCorrectSolution(int n, int h, String solution, boolean possible) {
        tower = new Tower(n, h);
        tower.makeInvisible();

        assertEquals(possible, solution != "impossible");
        if (solution != "impossible") {
            for (String height : solution.split(" ")) {
                tower.pushCup((Integer.parseInt(height) + 1)/2);
            }
            assertEquals(h, tower.height());
        }
    }

    @Test
    public void shouldThrowAnExceptionIfNIsNotPositive() {
        int n = 0;
        int h = 1;

        try {
            TowerContest.solve(n, h);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowAnExceptionIfHIsNotPositive() {
        int n = 1;
        int h = 0;

        try {
            TowerContest.solve(n, h);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldSoveN4H8() {
        int n = 4;
        int h = 8;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveWithTheMinimumValuesOfNAndH() {
        int n = 1;
        int h = 1;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveWhenHIsTheMinimumToHaveASolution() {
        int n = 7;
        int h = 2*n - 1;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveForHBetweenTheMinimumToHaveSolutionAndTheHeightWhenNMinus1DoesNotHaveSolution() {
        int n = 7;
        int h = 2*n + 4;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveWhenHIsTheHeightWhenNMinus1DoesNotHaveSolution() {
        int n = 5;
        int h = n*(n-2);

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveForHBetweenTheHeightWhenNMinus1DoesNotHaveSolutionAndTheHeightWhenTheCupNNeedsToBeTheLastItem() {
        int n = 7;
        int h = n*(n-2) + 3;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveWhenHIsTheHeightWhenTheCupNNeedsToBeTheLastItem() {
        int n = 6;
        int h = (n-1)*(n-1) + 2;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveForHBetweenTheHeightWhenTheCupNNeedsToBeTheLastItemAndTheMaximumToHaveASolution() {
        int n = 7;
        int h = (n-1)*(n-1) + 5;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldSolveWhenHIsTheMaximumToHaveASolution() {
        int n = 8;
        int h = n*n;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, true);
    }

    @Test
    public void shouldReturnImpossibleWhenThereIsNoSolution() {
        int n = 9;
        int h = n*n - 2;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, false);
    }

    @Test
    public void shouldReturnImpossibleWhenHIsLessThanTheMinimumToHaveASolution() {
        int n = 10;
        int h = 2*n-2;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, false);
    }

    @Test
    public void shouldReturnImpossibleWhenHIsGreaterThanTheMaximumToHaveASolution() {
        int n = 11;
        int h = n*n + 1;

        String solution = TowerContest.solve(n, h);
        assertCorrectSolution(n, h, solution, false);
    }
}
