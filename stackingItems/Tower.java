
/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------

import java.util.*;
import javax.swing.JOptionPane;


// THE COLORS ARE MISSING
// MAKE THE CANVAS BIGGER
// ORGANIZE THE METHODS, HERE IN THE CODE (SOME ORDER)
public class Tower {
    private int width;
    private int height;
    private boolean visible;
    private boolean ok;
    private List<String> orderOfItems;
    private List<Integer> heightOfItems;
    private TowerFrame frame;
    private HashMap<Integer, Cup> cups;
    private static int MARGIN = 20;
    private static int BLOCKSIZE = 20;

    // width in terms of i, this.width in terms of 2i-1, height and maxHeight in terms of i
    public Tower(int width, int maxHeight) {
        if ((width > 0) && (maxHeight > 0)) {
            this.width = 2*width - 1;
            this.height = maxHeight;
            this.visible = false;
            this.ok = true;
            this.active = true;
            this.orderOfItems = new ArrayList<>(); // {"c13", "l1", "c4"}
            this.heightOfItems = new ArrayList<>();
            this.frame = new TowerFrame(this.width*BLOCKSIZE, this.height*BLOCKSIZE, BLOCKSIZE, MARGIN);
            this.cups = new HashMap<>();
        } else {
            this.showMessage("`width` and `height` must be positive natural numbers.");
        }
    }

    public void pushCup(int index) {
        this.pushItem(index, true);
    }

    public void popCup() {
        this.popItem(true);
    }

    private boolean isLided(int index) {
        boolean isLided = false;
        int cupIndex = this.orderOfItems.indexOf("C" + index);
        if (this.orderOfItems.size() > cupIndex + 1) {
            isLided = this.orderOfItems.get(cupIndex+1).equals("L" + index);
        }
        return isLided;
    }

    private void pop() {
        int totalItems = this.orderOfItems.size();
        if (totalItems > 0) {
            String lastItem = this.orderOfItems.getLast();
            int lastIndex = this.getI(lastItem);
            // in case there is not the cup or lid to keep saving the cup associated
            if (!this.orderOfItems.containsAll(Arrays.asList("C" + lastIndex, "L" + lastIndex))) {
                this.cups.remove(lastIndex);
            }
            this.orderOfItems.removeLast();
            this.heightOfItems.removeLast();
            this.ok = true;
        } else {
            this.showMessage("Cannot pop items from an empty Tower.");
        }
    }

    private boolean hasItem(String item) {
        return this.orderOfItems.contains(item);
    }

    public void removeCup(int index) {
        this.removeItem(index, true);
    }

    private boolean isCup(String item) {
        return item.charAt(0) == "C";
    }
    
    public void pushLid (int index) {
        this.pushItem(index, false);
    }

    private int heightReachedByNewItem(int index, boolean isCup) {
        String lastItem = this.orderOfItems.getLast();
        // 1 if the last item was a lid
        int lastHeight = (this.isCup(lastItem) ? 2*this.getI(lastItem) - 1 : 1);

        int itemWidth = 2*index - 1;
        int itemHeight = (isCup ? itemWidth : 1);
        int lastHeightReached = this.heightOfItems.getLast();
        // this is the height the new item will reach
        return lastHeightReached + (lastHeight < itemWidth ? itemHeight : itemHeight + 1 - lastHeight);
    }

    private void pushItem(int index, boolean isCup) {
        this.ok = false; String errorMessage;
        String stringCL = (isCup ? "C" : "L");
        if (!this.orderOfItems.contains(stringCL + index) && (index > 0) && (2*index-1 <= this.width)) {
            // this is the height the new item will reach
            int newHeight = this.heightReachedByNewItem(index, isCup);
            if (!((newHeight > this.height) && this.visible)) {
                Cup associatedCup;
                if (this.cups.containsKey(index)) {associatedCup = this.cups.get(index);}
                else {
                    associatedCup = new Cup(index, "black", BLOCKSIZE);
                    this.cups.put(index, associatedCup);
                }
                if (isCup) {
                    associatedCup.centerX(MARGIN, this.width*BLOCKSIZE);
                    associatedCup.moveVerticallyFromTo(MARGIN, MARGIN + this.height - newHeight*BLOCKSIZE, this.visible);
                } else {
                    Lid newLid = associatedCup.getLid();
                    newLid.centerX(MARGIN, this.width*BLOCKSIZE);
                    newLid.moveVerticallyFromTo(MARGIN, MARGIN + this.height - newHeight*BLOCKSIZE, this.visible);
                }
                this.heightOfItems.add(newHeight);
                this.orderOfItems.add(stringCL + i);
                this.ok = true;
            } else {errorMessage = "Adding the new item will result in an overflow of the tower's frame.";}
        } else {errorMessage = "Either the cup already exists, the given size for the item is nonpositive or it is greater than the current allowed width.";}
        if (!this.ok) {
            this.showMessage(errorMessage);
        }
    }
    
    public void popLid () {
        this.popItem(false);
    }

    private void popItem(boolean isCup) {
        boolean existsItem = false;
        int j = this.orderOfItems.size() - 1;
        while (!existsItem && (j >= 0)) {
            existsItem = this.isCup(this.orderOfItems.get(j)) == isCup;
            j--;
        }
        
        if (existsItem) this.removeItem(++j, isCup);
        else {
            String itemType = (isCup ? "cup" : "lid");
            this.showMessage("There are no" + itemType + "s to pop.");
        }
    }
    
    public void removeLid (int index) {
        this.removeItem(index, false);
    }

    private void removeItem(int index, boolean isCup) {
        String stringCL = (isCup ? "C" : "L");
        if (this.hasItem(stringCL + index)) {
            boolean itemFound = false;
            String lastItem; int lastIndex;
            List<String> items = new ArrayList<>();
            boolean cupLided = this.isLided(index);
            do {
                lastItem = this.orderOfItems.getLast();
                lastIndex = this.getI(lastItem);
                if (lastItem.equals(stringCL + index)) {itemFound = true;}
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
                lastIndex = this.getI(lastItem);

                if (this.isCup(lastItem)) {this.pushCup(lastIndex);}
                else {this.pushLid(lastIndex);}
                items.removeLast();
            }
            this.ok = true;
        } else {
            this.showMessage("The desired item to be removed does not belong to the Tower.");
        }
    }
    
    public int height() {
        return Collections.max(this.heightOfItems);
    }
    
    public boolean ok() {
        return this.ok;
    }
    
    public void exit() {
        System.exit(0);
    }

    public int getI(String item) {
        return Integer.parseInt(item.substring(1));
    }
    
    public void makeVisible() {
        if (this.height() > this.height) {
            this.showMessage("Cannot make visible because the tower is overflowed.");
        } else if (!this.visible) {
            this.frame.makeVisible();
            for (String item : this.orderOfItems) {
                int itemIndex = this.getI(item);
                Cup associatedCup = this.cups.get(itemIndex);
                if (this.isCup(item)) {
                    associatedCup.makeVisible();
                } else {
                    associatedCup.makeLidVisible();
                }
            }
            this.ok = true;
        }
    }

    public void makeInvisible() {
        this.frame.makeInvisible();
        for (String item : this.orderOfItems) {
            int itemIndex = this.getI(item);
            Cup associatedCup = this.cups.get(itemIndex);
            if (this.isCup(item)) {
                associatedCup.makeInvisible();
            } else {
                associatedCup.makeLidInvisible();
            }
        }
        this.ok = true;
    }
    
    public String[][] stackingItems() {
        int totalItems = this.orderOfItems.size();
        String[][] stackingItems = new String[totalItems][2];
        for (int i=0; i<totalItems; i++) {
            String item = this.orderOfItems.get(i);
            String stringCL = (this.isCup(item) ? "cup" : "lid");
            stackingItems[i] = new String[]{stringCL, item.substring(1)};
        }
        return stackingItems;
    }

    public int[] lidedCups() {
        List<Integer> lidedCups = new ArrayList<>();
        int itemIndex;
        for (String item : this.orderOfItems) {
            itemIndex = this.getI(item);
            if (this.isCup(item) && this.isLided(itemIndex)) {
                lidedCups.add(itemIndex);
            }
        }
        return lidedCups.stream().mapToInt(Integer::intValue).toArray(); // \\\\\\\\\\
    }

    private void clear() {
        if (this.orderOfItems.size() > 0) {
            this.removeItem(this.orderOfItems.get(0));
        }
    }

    public void reverseTower() {
        // List<Integer> heightOfItemsCopy = new ArrayList<>(this.heightOfItems);
        List<String> orderOfItemsCopy = new ArrayList<>(this.orderOfItems);
        this.clear();

        int j = orderOfItemsCopy.size() - 1;
        String item = orderOfItemsCopy.get(j);
        int newHeight = this.heightReachedByNewItem(this.getI(item), this.isCup(item));
        while ((newHeight < this.height) && (j >= 0)) {
            this.pushItem(this.getI(item), this.isCup(item));
            item = orderOfItemsCopy.get(j);
            newHeight = this.heightReachedByNewItem(this.getI(item), this.isCup(item));
            j--;
        }
        this.ok = true;
    }

    public void orderTower() {
        List<String> itemsDescendingOrder = Collections.sort(this.orderOfItems, Collections.reverseOrder());

        int j = 0;
        String item;
        int itemIndex;
        boolean isCup;
        int newHeight = 0;
        while ((j < itemsDescendingOrder.size()) && (newHeight < this.height)) {
            item = itemsDescendingOrder.get(j);
            itemIndex = this.getI(item);
            isCup = this.isCup(item);
            newHeight = this.heightReachedByNewItem(itemIndex, isCup);
            if ((!isCup) && (itemsDescendingOrder.indexOf("C" + itemIndex) > 0)) { // in descending order, the l comes before the c
                this.pushCup(itemIndex);
                newHeight = this.heightReachedByNewItem(itemIndex, isCup);
                j++;
            }
            if (newHeight < this.height) {
                this.pushItem(itemIndex, isCup);
                j++;
            }
        }
        this.ok = true;
    }

    private void showMessage(String message) {
        if (this.visible) {
            JOptionPane.showMessageDialog(null, message);
            this.ok = false;
        }
    }
}