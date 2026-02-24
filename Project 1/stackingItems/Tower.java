
/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------

import java.util.*;
import javax.swing.JOptionPane;


public class Tower {
    private int width;
    private int height;
    private boolean visible;
    private boolean ok;
    private List<TowerItem> orderOfItems;
    private TowerFrame frame;
    private static int MARGIN = 20;
    private static int BLOCKSIZE = 20;

    private boolean showMessages = true; // ------------------------------------------------------

    // width in terms of i, this.width in terms of 2i-1, height and maxHeight in terms of i
    public Tower(int width, int maxHeight) {
        if ((width > 0) && (maxHeight > 0)) {
            this.width = 2*width - 1;
            this.height = maxHeight;
            this.visible = true;
            this.ok = true;
            this.orderOfItems = new ArrayList<>(); 
            this.frame = new TowerFrame(this.width*BLOCKSIZE, this.height*BLOCKSIZE, BLOCKSIZE, MARGIN);

            Canvas.getCanvas(this.width*BLOCKSIZE + 2*MARGIN, this.height*BLOCKSIZE + 2*MARGIN);
            this.frame.makeVisible();
        } else {
            String message = "`width` and `height` must be positive natural numbers.";
            this.showMessage(message);
            throw new IllegalArgumentException(message);
        }
    }
    
    public boolean ok() {
        return this.ok;
    }

    public void setShowMessages(boolean showMessages) { // ------------------------------------------------------
        this.showMessages = showMessages;
    }

    private void showMessage(String message) {
        if (this.visible) { // ------------------------------------------------------
            if (this.showMessages) JOptionPane.showMessageDialog(null, message);
            this.ok = false;
        }
    }
    
    public void exit() {
        System.exit(0);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int height() {
        int maxHeight = 0;
        for (TowerItem item : this.orderOfItems) {
            maxHeight = Math.max(maxHeight, item.getHeightReached());
        }
        return maxHeight;
    }

    public void makeVisible() {
        if (this.height() > this.height) {
            this.showMessage("Cannot make visible because the tower is overflowed.");
        } else {
            this.frame.makeVisible();
            for (TowerItem item : this.orderOfItems) {
                item.makeVisible();
            }
            this.visible = true;
            this.ok = true;
        }
    }

    public void makeInvisible() {
        this.frame.makeInvisible();
        for (TowerItem item : this.orderOfItems) {
            item.makeInvisible();
        }
        this.visible = false;
        this.ok = true;
    }

    private boolean hasItem(int index, boolean isCup) {
        boolean hasItem = false;
        int j = 0;
        while (!hasItem && (j < this.orderOfItems.size())) {
            TowerItem item = this.orderOfItems.get(j);
            if ((item.isCup() == isCup) && (item.getIndex() == index)) {
                hasItem = true;
            }
            j++;
        }
        return hasItem;
    }

    private int indexOfItem(int index, boolean isCup) {
        int itemFound = -1;
        int j = 0;
        while (itemFound == -1 && (j < this.orderOfItems.size())) {
            TowerItem item = this.orderOfItems.get(j);
            if ((item.isCup() == isCup) && (item.getIndex() == index)) {
                itemFound = j;
            }
            j++;
        }
        return itemFound;
    }

    private int heightReachedByNewItem(int index, boolean isCup) {
        int newItemHeight = (isCup ? 2*index - 1 : 1);
        int heightReachedByNewItem = newItemHeight;
        if (this.orderOfItems.size() > 0) {
            int j = this.orderOfItems.size() - 1;
            boolean foundGreaterEqualWidth = false;
            while (j >= 0 && !foundGreaterEqualWidth) {
                TowerItem item = this.orderOfItems.get(j);
                int itemIndex = item.getIndex();
                int heightReached = item.getHeightReached();
                int itemHeight = (item.isCup() ? 2*itemIndex - 1 : 1);
                if (itemIndex > index) {
                    heightReachedByNewItem = Math.max(heightReachedByNewItem, heightReached + (newItemHeight+1-itemHeight));
                    foundGreaterEqualWidth = true;
                } else {
                    heightReachedByNewItem = Math.max(heightReachedByNewItem, heightReached + newItemHeight);
                    if (itemIndex == index) foundGreaterEqualWidth = true;
                }
                j--;
            }
        }
        return heightReachedByNewItem;
    }

    private boolean isItemLidded(int index) {
        boolean isItemLidded = false;
        int cupIndex = this.indexOfItem(index, true);
        if (cupIndex >= 0 && this.orderOfItems.size() > cupIndex + 1) {
            TowerItem itemAfterCup = this.orderOfItems.get(cupIndex + 1);
            isItemLidded = !itemAfterCup.isCup() && itemAfterCup.getIndex() == index;
        }
        return isItemLidded;
    }
    /* is lidded if the lid has lidHeight = cupHeight + 1*/ // ---------------------------------------------------

    public int[] heightReachedByItems() {
        int[] heightOfItems = new int[this.orderOfItems.size()];
        for (int i=0; i<this.orderOfItems.size(); i++) {
            heightOfItems[i] = this.orderOfItems.get(i).getHeightReached();
        }
        return heightOfItems;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void pushItem(int index, boolean isCup) {
        this.ok = false;
        String errorMessage = "";
        if (!this.hasItem(index, isCup) && (index > 0) && (2*index-1 <= this.width)) {
            // this is the height the new item will reach
            int newHeight = this.heightReachedByNewItem(index, isCup);
            if (!((newHeight > this.height) && this.visible)) {
                TowerItem newItem = new TowerItem(isCup, index, BLOCKSIZE, MARGIN, this.width, this.height);
                newItem.setHeightReached(newHeight);
                if (this.visible) {newItem.makeVisible();}
                this.orderOfItems.add(newItem);
                this.ok = true;
            } else {errorMessage = "Adding the new item will result in an overflow of the tower's frame.";}
        } else {errorMessage = "Either the item already exists, the given size for the item is nonpositive or it is greater than the current allowed width.";}
        if (!this.ok) {
            this.showMessage(errorMessage);
        }
    }

    private void pop() {
        if (!this.orderOfItems.isEmpty()) {
            TowerItem lastItem = this.orderOfItems.getLast();
            int lastIndex = lastItem.getIndex();

            lastItem.makeInvisible();
            // in case there is not the cup or lid to keep saving the cup associated
            if (!(this.hasItem(lastIndex, true) && this.hasItem(lastIndex, false))) {
                lastItem.deleteAssociatedCup();
            }
            this.orderOfItems.removeLast();
            this.ok = true;
        } else {
            this.showMessage("Cannot pop items from an empty Tower.");
        }
    }

    private void removeItem(int index, boolean isCup) {
        if (this.hasItem(index, isCup)) {
            boolean itemFound = false;
            TowerItem lastItem; int lastIndex;
            List<TowerItem> items = new ArrayList<>();
            do {
                lastItem = this.orderOfItems.getLast();
                lastIndex = lastItem.getIndex();
                if ((lastItem.isCup() == isCup) && (lastItem.getIndex() == index)) {itemFound = true;}
                else {items.add(lastItem);}
                // this is why the while is used instead of .subList and .indexOf
                this.pop();
            } while (!itemFound);

            int limit = 0;
            boolean cupLidded = this.isItemLidded(index);
            if (cupLidded) {
                if (isCup) {limit = 1;} // dont reinsert the last lid removed if the cup removed was lidded
                else {this.pop();} // remove the cup associated
            }
            while (items.size() > limit) {
                lastItem = items.getLast();
                lastIndex = lastItem.getIndex();

                if (lastItem.isCup()) {this.pushCup(lastIndex);}
                else {this.pushLid(lastIndex);}
                items.removeLast();
            }
            this.ok = true;
        } else {
            this.showMessage("The desired item to be removed does not belong to the Tower.");
        }
    }

    private void popItem(boolean isCup) {
        boolean existsItem = false;
        int j = this.orderOfItems.size() - 1;
        while (!existsItem && (j >= 0)) {
            existsItem = this.orderOfItems.get(j).isCup() == isCup;
            j--;
        }
        
        if (existsItem) {
            TowerItem itemToRemove = this.orderOfItems.get(++j);
            this.removeItem(itemToRemove.getIndex(), isCup);
        } else {
            String itemType = (isCup ? "cup" : "lid");
            this.showMessage("There are no " + itemType + "s to pop.");
        }
    }

    public void pushCup(int index) {
        this.pushItem(index, true);
    }

    public void popCup() {
        this.popItem(true);
    }

    public void removeCup(int index) {
        this.removeItem(index, true);
    }
    
    public void pushLid (int index) {
        this.pushItem(index, false);
    }
    
    public void popLid () {
        this.popItem(false);
    }
    
    public void removeLid (int index) {
        this.removeItem(index, false);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void clear() {
        while (!this.orderOfItems.isEmpty()) {
            this.pop();
        }
    }
    
    public String[][] stackingItems() {
        int totalItems = this.orderOfItems.size();
        String[][] stackingItems = new String[totalItems][2];
        for (int i=0; i<totalItems; i++) {
            TowerItem item = this.orderOfItems.get(i);
            String stringCL = (item.isCup() ? "cup" : "lid");
            stackingItems[i] = new String[]{stringCL, item.getIndex() + ""};
        }
        return stackingItems;
    }

    public int[] liddedCups() {
        List<Integer> liddedCups = new ArrayList<>();
        int itemIndex;
        for (TowerItem item : this.orderOfItems) {
            itemIndex = item.getIndex();
            if (item.isCup() && this.isItemLidded(itemIndex)) {
                liddedCups.add(itemIndex);
            }
        }
        return liddedCups.stream().mapToInt(Integer::intValue).toArray(); // \\\\\\\\\\
    }

    // SHOULD THE LIDDED CUPS CONDITION BE APPLIED HERE ??????????????----------------***************\\\\\\\\\\\\\\\\
    public void reverseTower() {
        if (!this.orderOfItems.isEmpty()) {
            List<TowerItem> orderOfItemsCopy = new ArrayList<>(this.orderOfItems);
            this.clear();

            int j = orderOfItemsCopy.size() - 1;
            TowerItem item = orderOfItemsCopy.get(j);
            int newHeight = this.heightReachedByNewItem(item.getIndex(), item.isCup());
            while ((newHeight <= this.height) && (j >= 0)) {
                this.pushItem(item.getIndex(), item.isCup());
                j--;
                if (j >= 0) {
                    item = orderOfItemsCopy.get(j);
                    newHeight = this.heightReachedByNewItem(item.getIndex(), item.isCup());
                }
            }
        }
        this.ok = true;
    }

    public void orderTower() {
        if (!this.orderOfItems.isEmpty()) {
            List<TowerItem> itemsDescendingOrder = new ArrayList<>(this.orderOfItems);
            itemsDescendingOrder.sort((a, b) -> Integer.compare(b.getIndex(), a.getIndex()));
            this.clear();

            int j = 0;
            TowerItem item;
            int itemIndex;
            boolean isCup;
            int newHeight = 0;
            while ((j < itemsDescendingOrder.size()) && (newHeight <= this.height)) {
                item = itemsDescendingOrder.get(j);
                itemIndex = item.getIndex();
                isCup = item.isCup();
                
                // in descending order, the l comes before the c, so if the cup is before the lid, we push the cup
                if (!isCup && itemsDescendingOrder.size() > j + 1) {
                    TowerItem nextItem = itemsDescendingOrder.get(j+1);
                    if (nextItem.getIndex() == itemIndex && nextItem.isCup()) {
                        newHeight = this.heightReachedByNewItem(itemIndex, true);
                        if (newHeight <= this.height) {
                            this.pushCup(itemIndex);
                            j++;
                        }
                    }
                }
                newHeight = this.heightReachedByNewItem(itemIndex, isCup);
                if (newHeight <= this.height) {
                    this.pushItem(itemIndex, isCup);
                    j++;
                }
            }
        } 
        this.ok = true;
    }
}