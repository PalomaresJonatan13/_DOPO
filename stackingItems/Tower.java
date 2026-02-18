
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
import java.util.Arrays;

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

    // need to add this.ok = true when everything is ok
    // width in terms of i's, maxHeight in terms of 2*i - 1
    // stacking items is similar to orderOfItems, maybe changes this last one
    public Tower(int width, int maxHeight) { // ------------------------------------------------------
        if ((width > 0) && (maxHeight > 0)) {
            this.width = width;
            this.height = maxHeight;
            this.visible = false;
            this.ok = true;
            this.active = true;
            this.orderOfItems = new ArrayList<>(); // {"c13_13", "l1_2", "c4_6"}
            this.heightOfItems = new ArrayList<>();
            this.frame = new TowerFrame(width, maxHeight, BLOCKSIZE, MARGIN);
            this.cups = new HashMap<>();
        } else {
            // ------------------------------------------------------ SHOW MESSAGE WITH JOptionPane
            this.ok = false;
        }
    }

    public void pushCup(int i) { // ------------------------------------------------------
        this.push(i, true);
    }

    public popCup() { // ------------------------------------------------------
        boolean existsCup = false;
        int j = this.orderOfItems.size() - 1;
        while (!existsCup && (j >= 0)) {
            existsCup = this.isCup(this.orderOfItems.get(j));
            j--;
        }
        
        if (existsCup) this.removeCup(++j);
        else {
            // ------------------------------------------------------ MESSAGE
            this.ok = false;
        }
    }

    private boolean isLided(int i) { // ------------------------------------------------------
        boolean isLided = false;
        int cupIndex = this.orderOfItems.indexOf("C" + i);
        if (this.orderOfItems.size() > cupIndex + 1) {
            isLided = this.orderOfItems.get(cupIndex+1).equals("L" + i);
        }
        return isLided;
    }

    private void pop() { // ------------------------------------------------------
        int totalItems = this.orderOfItems.size();
        if (totalItems > 0) {
            String lastItem = this.orderOfItems.getLast();
            int lastI = Integer.parseInt(lastItem.substring(1));
            // in case there is not the cup or lid to keep saving the cup associated
            if (!this.orderOfItems.containsAll(Arrays.asList("C" + lastI, "L" + lastI))) {
                this.cups.remove(lastI);
            }
            this.orderOfItems.removeLast();
            this.heightOfItems.removeLast();
        } else {
            // ------------------------------------------------------ MESSAGE
            this.ok = false;
        }
    }

    private boolean hasItem(String item) {
        return this.orderOfItems.contains(item);
    }

    public removeCup(int i) { // ------------------------------------------------------
        this.remove(i, true);
    }

    private boolean isCup(String item) {
        return item.charAt(0) == "C";
    }
    
    public pushLid (int i) {
        this.push(i, false);
    }

    private int calcNewItemReachedHeight(int i, boolean isCup) { // ------------------------------------------------------
        String lastItem = this.orderOfItems.getLast();
        // 1 if the last item was a lid
        int lastHeight = (lastItem.charAt(0) == "C" ? 2*Integer.parseInt(lastItem.substring(1)) - 1 : 1);

        int itemWidth = 2*i - 1;
        int itemHeight = (isCup ? itemWidth : 1);
        int lastHeightReached = this.heightOfItems.getLast();
        // this is the height the new item will reach
        return lastHeightReached + (lastHeight < itemWidth ? itemHeight : itemHeight + 1 - lastHeight);
    }

    private void push(int i, boolean isCup) { // ------------------------------------------------------
        if (!this.orderOfItems.contains(stringCL + i) && (i > 0) && (i <= this.width)) {
            String stringCL = (isCup ? "C" : "L");
            // this is the height the new item will reach
            int newHeight = this.calcNewItemReachedHeight(i, isCup);

            if (!((newHeight > this.height) && this.visible)) {
                Cup associatedCup;
                if (this.cups.containsKey(i)) {associatedCup = this.cups.get(i);}
                else {
                    associatedCup = new Cup(i, "black", BLOCKSIZE);
                    this.cups.put(i, associatedCup);
                }
                if (isCup) {
                    if (this.visible) {
                        associatedCup.moveVerticallyTo(MARGIN);
                        associatedCup.makeVisible();
                    }
                    associatedCup.moveVerticallyTo(MARGIN + this.height - newHeight);
                } else {
                    Lid newLid = associatedCup.getLid();
                    if (this.visible) {
                        newLid.moveVerticallyTo(MARGIN);
                        newLid.makeVisible();
                    }
                    newLid.moveVerticallyTo(MARGIN + this.height - newHeight);
                }
                this.heightOfItems.add(newHeight);
                this.orderOfItems.add(stringCL + i);
                this.ok = true;
            } else {
                // ------------------------------------------------------ MESSAGE
                this.ok = false;
            }
        } else {
            // ------------------------------------------------------ MESSAGE
            this.ok = false;
        }
    }
    
    public void popLid () {
        // f
    }
    
    public void removeLid (int i) { // ------------------------------------------------------
        this.remove(i, false);
    }

    private void remove(int i, boolean isCup) { // ------------------------------------------------------
        String stringCL = (isCup ? "C" : "L");
        if (this.hasItem(stringCL + i)) {
            boolean itemFound = false;
            String lastItem; int lastI;
            List<String> items = new ArrayList<>();
            boolean cupLided = this.isLided(i);
            do {
                lastItem = this.orderOfItems.getLast();
                lastI = Integer.parseInt(lastItem.substring(1));
                if (lastItem.equals(stringCL + i)) {itemFound = true;}
                else {items.add(lastItem);}
                // this is why the while is used instead of .subList and .indexOf
                this.pop();
            } while (!itemFound);

            int limit = 0;
            if (cupLided) {
                if (isCup) {limit = 1;} // dont reinsert the last lid removed if the cup removed was lided
                else {this.pop();} // remove the cup associated
            }
            while (items.size() > limit) {
                lastItem = items.getLast();
                lastI = Integer.parseInt(lastItem.substring(1));

                if (this.isCup(lastItem)) {this.pushCup(lastI);}
                else {this.pushLid(lastI);}
                items.removeLast();
            }
        } else {
            // ------------------------------------------------------ MESSAGE
            this.ok = false;
        }
    }
    
    public int height() {
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