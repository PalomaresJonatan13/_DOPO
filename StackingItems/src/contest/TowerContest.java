package contest;
import tower.*;

import java.util.*;
import javax.swing.JOptionPane;

public class TowerContest {
    private static HashMap<String, List<Integer>> memory = new HashMap<>();
    static {
        memory.put("1_1", List.of(1));
        memory.put("4_8", List.of(1, 4, 3, 2));
    };

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public static String solve(int n, int h) {
        String solution = "";
        if (n > 0 && h > 0) {
            List<Integer> indexes = getIndexes(n, h);
            if (indexes.isEmpty()) {
                solution = "impossible";
            } else {
                for (int j=0; j<indexes.size(); j++) {
                    int height = 2*indexes.get(j) - 1;
                    solution += height;
                    if (j != indexes.size() - 1) {
                        solution += " ";
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("n and h must be positive integers");
        }
        return solution;
    }

    public static void simulate(int n, int h) {
        if (n > 0 && h > 0) {
            String solution = solve(n, h);
            if (solution != "impossible") {
                Tower tower = new Tower(n, h);
                for (String height : solution.split(" ")) {
                    tower.pushCup((Integer.parseInt(height) + 1)/2);
                }
            } else {
                JOptionPane.showMessageDialog(null, String.format("No solution for n=%d, h=%d", n, h));
            }
        } else {
            JOptionPane.showMessageDialog(null, "n and h must be positive integers");
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private static List<Integer> getIndexes(int n, int h) {
        List<Integer> indexes = new ArrayList<>();
        List<Integer> storedValue = memory.get(n + "_" + h);
        
        if (storedValue != null) {
            indexes = storedValue;
        } else if (n>0 && 2*n-1 <= h && h <= n*n && h != n*n-2) {
            int specialCase1 = n*(n-2);
            int specialCase2 = (n-1)*(n-1) + 2;

            if (h == specialCase1) { // the switch didnt work
                List<Integer> firstIndexes = getIndexes(n-2, (n-2)*(n-2) - 3);
                indexes.addAll(firstIndexes);
                indexes.addAll(Arrays.asList(new Integer[] {n, n-1}));
            } else if (h == specialCase2) {
                List<Integer> firstIndexes = getIndexes(n-2, (n-2)*(n-2));
                indexes.addAll(firstIndexes);
                indexes.addAll(Arrays.asList(new Integer[] {n, n-1}));
            } else if (h < specialCase2) {
                List<Integer> lastIndexes = getIndexes(n-1, h-1);
                indexes.addAll(Arrays.asList(new Integer[] {n}));
                indexes.addAll(lastIndexes);
            } else { // h > specialCase2
                List<Integer> firstIndexes = getIndexes(n-1, h - (2*n-1));
                indexes.addAll(firstIndexes);
                indexes.addAll(Arrays.asList(new Integer[] {n}));
            }

            memory.put(n + "_" + h, indexes);
        }
        return indexes;
    }
}
