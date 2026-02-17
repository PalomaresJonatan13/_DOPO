
/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Tower {
    private int width;
    private int height;
    private boolean visible;
    private boolean ok;
    private boolean active;
    private List<String> orderOfItems;
    private List<Integer> heightOfItems;
    private TowerFrame frame;
    private HashMap<Integer, Cup> cups;
    private static int MARGIN = 20;
    private static int BLOCKSIZE = 20;

    // width in terms of i's, maxHeight in terms of 2*i - 1
    public Tower(int width, int maxHeight) { // ------------------------------------------------------
        if ((width > 0) && (maxHeight > 0)) {
            this.width = width;
            this.height = maxHeight;
            this.visible = false;
            this.ok = true;
            this.active = true;
            this.orderOfItems = new ArrayList<>(); // {"c13_13", "l1_2", "c4_6"}
            this.heightOfItems = new ArrayList<>();
            this.frame = new TowerFrame(width, maxHeight, this.BLOCKSIZE, this.MARGIN);
            this.cups = new HashMap<>();
        } else {
            // ------------------------------------------------------ SHOW MESSAGE WITH JOptionPane
        }
    }

    public void pushCup(int i) { // ------------------------------------------------------
        if (this.active) {
            int totalItems = this.orderOfItems.size();
            String lastItem = this.orderOfItems.get(totalItems - 1);
            int lastSize = 2*Integer.parseInt(lastItem.substring(1)) - 1;

            if (this.orderOfItems.contains("C" + i) || i <= 0 || i > this.width){
                // ------------------------------------------------------ MESSAGE
                return;
            } else {
                int cupSize = 2*i - 1;
                int lastHeight = this.heightOfItems.get(totalItems - 1);
                int newHeight = lastHeight + (lastSize < cupSize ? cupSize : cupSize + 1 - lastSize);

                if ((newHeight > this.height) && this.visible) {
                    // ------------------------------------------------------ MESSAGE
                    this.ok = false;
                } else {
                    Cup newCup = new Cup(i, "black", this.BLOCKSIZE);
                    newCup.centerX(this.MARGIN + (this.BLOCKSIZE * this.width)/2);
                    if (this.visible) {
                        newCup.moveVerticallyTo(this.MARGIN);
                        newCup.makeVisible();
                    }
                    newCup.moveVerticallyTo(this.MARGIN + this.height - newHeight);

                    this.heightOfItems.add(newHeight);
                    this.orderOfItems.add("C" + i);
                    if (!this.cups.containsKey(i)) this.cups.put(i, newCup);
                    this.ok = true;
                }
            }
        } else {
            // ------------------------------------------------------ MESSAGE
        }
    }
    
    // if there are no cups, then display a message
    public popCup() {
        if (this.active) {
            int totalItems = this.orderOfItems.size();
            if (totalItems > 0) {
                String lastItem = this.orderOfItems.get(totalItems - 1);
                int lastI = Integer.parseInt(lastItem.substring(1));
                List<Integer> lids = new ArrayList<>();

                int j = totalItems - 1;
                String lastItem = this.orderOfItems.get(j);
                int lastI = Integer.parseInt(lastItem.substring(1));
                while ((j > 0) && (lastItem.chartAt(0) != "C")) { // --------------------------- HERE (JUST CALL REMOVE ONCE YOU FIND THE CUP)
                    lids.add(lastI);
                    this.pop();
                    totalItems = this.orderOfItems.size();
                    lastItem = this.orderOfItems.get(totalItems - 1);
                    lastI = Integer.parseInt(lastItem.substring(1));
                    j--;
                }

                /* while (lastItem.charAt(0) != "C") {
                    lids.add(lastI);
                    this.pop();
                    lastItem
                } */
                this.orderOfItems.removeLast(); // removing the cup

                int start = lids.size() - (this.isLided(lastI) ? 2 : 1); // 
                for (int i=start; i >= 0; i--) {
                    this.pushLid(Integer.parseInt(lids.get(i)));
                }
            } else {
                // ------------------------------------------------------ MESSAGE
            }
        } else {
            // ------------------------------------------------------ MESSAGE
        }
    }

    private boolean isLided(int i) {
        boolean isLided = false
        int cupIndex = this.orderOfItems.indexOf("C" + i);
        if (this.orderOfItems > cupIndex + 1) {
            isLided = this.orderOfItems.get(cupIndex+1).equals("L" + i);
        }
        return isLided;
    }

    private void pop() {
        int totalItems = this.orderOfItems.size();
        String lastItem = this.orderOfItems.get(totalItems - 1);
        int lastI = Integer.parseInt(lastItem.substring(1));
        if (!this.orderOfItems.containsAll({"C" + lastI, "L" + lastI})) { // - - -- - - - -  -- - - - - - 
            this.cups.remove(lastI);
        }
        this.orderOfItems.remove(totalItems - 1);
        this.heightOfItems.remove(totalItems - 1);
    }
    
    /**
     * maybe make pop until that cup, and save the item names. then push all cups and lids
     * above the cup removed, in order.
     */
    public removeCup() {
        // f
    }
    
    public pushLid () {
        // f
    }
    
    public popLid () {
        // f
    }
    
    public removeLid () {
        // f
    }
    
    public height() {
        return Collections.max(this.heightOfItems);
    }
    
    public void () {
        // f
    }
    
    public void () {
        // f
    }
    
    public void () {
        // f
    }
    
    public void () {
        // f
    }
    
}