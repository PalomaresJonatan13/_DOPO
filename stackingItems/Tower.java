
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

    private boolean showMessages = true; // ------------------------------------------------------

    // width in terms of i, this.width in terms of 2i-1, height and maxHeight in terms of i
    public Tower(int width, int maxHeight) {
        if ((width > 0) && (maxHeight > 0)) {
            this.width = 2*width - 1;
            this.height = maxHeight;
            this.visible = true;
            this.ok = true;
            this.orderOfItems = new ArrayList<>(); // {"c13", "l1", "c4"}
            this.heightOfItems = new ArrayList<>();
            this.frame = new TowerFrame(this.width*BLOCKSIZE, this.height*BLOCKSIZE, BLOCKSIZE, MARGIN);
            this.cups = new HashMap<>();

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
        return (this.heightOfItems.size() > 0 ? Collections.max(this.heightOfItems) : 0);
    }

    public void makeVisible() {
        if (this.height() > this.height) {
            this.showMessage("Cannot make visible because the tower is overflowed.");
        } else if (!this.visible) {
            this.frame.makeVisible();
            for (String item : this.orderOfItems) {
                int itemIndex = this.getItemIndex(item);
                Cup associatedCup = this.cups.get(itemIndex);
                if (this.isCup(item)) {
                    associatedCup.makeVisible();
                } else {
                    associatedCup.makeLidVisible();
                }
            }
            this.visible = true;
            this.ok = true;
        }
    }

    public void makeInvisible() {
        this.visible = false;
        this.frame.makeInvisible();
        for (String item : this.orderOfItems) {
            int itemIndex = this.getItemIndex(item);
            Cup associatedCup = this.cups.get(itemIndex);
            if (this.isCup(item)) {
                associatedCup.makeInvisible();
            } else {
                associatedCup.makeLidInvisible();
            }
        }
        this.ok = true;
    }

    private boolean hasItem(String item) {
        return this.orderOfItems.contains(item);
    }

    public int getItemIndex(String item) { // needs to be public ?????
        return Integer.parseInt(item.substring(1));
    }

    private boolean isCup(String item) {
        return item.charAt(0) == 'C';
    }

    private int heightReachedByNewItem(int index, boolean isCup) {
        if (this.orderOfItems.size() == 0) {
            return (isCup ? 2*index - 1 : 1);
        }
        String lastItem = this.orderOfItems.getLast();
        // 1 if the last item was a lid
        int lastHeight = (this.isCup(lastItem) ? 2*this.getItemIndex(lastItem) - 1 : 1);

        int itemWidth = 2*index - 1;
        int itemHeight = (isCup ? itemWidth : 1);
        int lastHeightReached = this.heightOfItems.getLast();
        // this is the height the new item will reach
        return lastHeightReached + (lastHeight <= itemWidth ? itemHeight : itemHeight + 1 - lastHeight);
    }

    /*
    private int heightReachedByNewItem(int index, boolean isCup) {
        size == 0, the same, but try using a variable named heightReached and return it at the end
        else if the last item has width greater than the new item,
            height reached = last height of items + (height of new item + 1 - height of last item)
        else
            maxHeight reached = 0
            while, going backwards until finding an item with greater or equal width
                update the maxHeight with the heights reached by those items.
                if the item has same width, include it too for the maxHeght
            the height reached by the new item = maxHeight + height of new item.
    }
    */

    private boolean isLidded(int index) {
        boolean isLidded = false;
        int cupIndex = this.orderOfItems.indexOf("C" + index);
        if (cupIndex >= 0 && this.orderOfItems.size() > cupIndex + 1) {
            isLidded = this.orderOfItems.get(cupIndex+1).equals("L" + index);
        }
        return isLidded;
    }

    /* is lidded if the lid has lidHeight = cupHeight + 1*/ // ---------------------------------------------------

    public int[] getHeightOfItems() {
        return this.heightOfItems.stream().mapToInt(Integer::intValue).toArray(); // \\\\\\\\\\
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void pushItem(int index, boolean isCup) {
        this.ok = false;
        String errorMessage = "";
        String stringCL = (isCup ? "C" : "L");
        if (!this.orderOfItems.contains(stringCL + index) && (index > 0) && (2*index-1 <= this.width)) {
            // this is the height the new item will reach
            int newHeight = this.heightReachedByNewItem(index, isCup);
            if (!((newHeight > this.height) && this.visible)) {
                Cup associatedCup;
                if (this.cups.containsKey(index)) {associatedCup = this.cups.get(index);}
                else {
                    associatedCup = new Cup(index, BLOCKSIZE);
                    this.cups.put(index, associatedCup);
                }
                if (isCup) {
                    associatedCup.centerX(MARGIN, this.width*BLOCKSIZE);
                    associatedCup.moveVerticallyTo(MARGIN + (this.height - newHeight)*BLOCKSIZE);
                    if (this.visible) {associatedCup.makeVisible();}
                } else {
                    Lid newLid = associatedCup.getLid();
                    newLid.centerX(MARGIN, this.width*BLOCKSIZE);
                    newLid.moveVerticallyTo(MARGIN + (this.height - newHeight)*BLOCKSIZE);
                    if (this.visible) {newLid.makeVisible();}
                }
                this.heightOfItems.add(newHeight);
                this.orderOfItems.add(stringCL + index);
                this.ok = true;
            } else {errorMessage = "Adding the new item will result in an overflow of the tower's frame.";}
        } else {errorMessage = "Either the item already exists, the given size for the item is nonpositive or it is greater than the current allowed width.";}
        if (!this.ok) {
            this.showMessage(errorMessage);
        }
    }

    private void pop() {
        int totalItems = this.orderOfItems.size();
        if (totalItems > 0) {
            String lastItem = this.orderOfItems.getLast();
            int lastIndex = this.getItemIndex(lastItem);

            if (this.isCup(lastItem)) this.cups.get(lastIndex).makeInvisible();
            else this.cups.get(lastIndex).makeLidInvisible();
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

    private void removeItem(int index, boolean isCup) {
        String stringCL = (isCup ? "C" : "L");
        if (this.hasItem(stringCL + index)) {
            boolean itemFound = false;
            String lastItem; int lastIndex;
            List<String> items = new ArrayList<>();
            boolean cuplidded = this.isLidded(index);
            do {
                lastItem = this.orderOfItems.getLast();
                lastIndex = this.getItemIndex(lastItem);
                if (lastItem.equals(stringCL + index)) {itemFound = true;}
                else {items.add(lastItem);}
                // this is why the while is used instead of .subList and .indexOf
                this.pop();
            } while (!itemFound);

            int limit = 0;
            if (cuplidded) {
                if (isCup) {limit = 1;} // dont reinsert the last lid removed if the cup removed was lidded
                else {this.pop();} // remove the cup associated
            }
            while (items.size() > limit) {
                lastItem = items.getLast();
                lastIndex = this.getItemIndex(lastItem);

                if (this.isCup(lastItem)) {this.pushCup(lastIndex);}
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
            existsItem = this.isCup(this.orderOfItems.get(j)) == isCup;
            j--;
        }
        
        if (existsItem) {
            String itemToRemove = this.orderOfItems.get(++j);
            this.removeItem(this.getItemIndex(itemToRemove), isCup);
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
            String item = this.orderOfItems.get(i);
            String stringCL = (this.isCup(item) ? "cup" : "lid");
            stackingItems[i] = new String[]{stringCL, item.substring(1)};
        }
        return stackingItems;
    }

    public int[] liddedCups() {
        List<Integer> liddedCups = new ArrayList<>();
        int itemIndex;
        for (String item : this.orderOfItems) {
            itemIndex = this.getItemIndex(item);
            if (this.isCup(item) && this.isLidded(itemIndex)) {
                liddedCups.add(itemIndex);
            }
        }
        return liddedCups.stream().mapToInt(Integer::intValue).toArray(); // \\\\\\\\\\
    }

    // SHOULD THE LIDDED CUPS CONDITION BE APPLIED HERE ??????????????----------------***************\\\\\\\\\\\\\\\\
    public void reverseTower() {
        // List<Integer> heightOfItemsCopy = new ArrayList<>(this.heightOfItems);
        List<String> orderOfItemsCopy = new ArrayList<>(this.orderOfItems);
        this.clear();

        int j = orderOfItemsCopy.size() - 1;
        String item = orderOfItemsCopy.get(j);
        int newHeight = this.heightReachedByNewItem(this.getItemIndex(item), this.isCup(item));
        while ((newHeight < this.height) && (j >= 0)) {
            this.pushItem(this.getItemIndex(item), this.isCup(item));
            j--;
            if (j >= 0) {
                item = orderOfItemsCopy.get(j);
                newHeight = this.heightReachedByNewItem(this.getItemIndex(item), this.isCup(item));
            }
        }
        this.ok = true;
    }

    public void orderTower() {
        List<String> itemsDescendingOrder = new ArrayList<>(this.orderOfItems);
        Collections.sort(itemsDescendingOrder, Collections.reverseOrder());
        this.clear();

        int j = 0;
        String item;
        int itemIndex;
        boolean isCup;
        int newHeight = 0;
        while ((j < itemsDescendingOrder.size()) && (newHeight < this.height)) {
            item = itemsDescendingOrder.get(j);
            itemIndex = this.getItemIndex(item);
            isCup = this.isCup(item);
            newHeight = this.heightReachedByNewItem(itemIndex, isCup);
            // in descending order, the l comes before the c, so if the cup is before the lid, we push the cup
            if ((!isCup) && (itemsDescendingOrder.indexOf("C" + itemIndex) >= 0)) {
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
}