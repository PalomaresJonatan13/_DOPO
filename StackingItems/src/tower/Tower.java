package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;
import javax.swing.JOptionPane;


public class Tower {
    private int width;
    private int height;
    private boolean visible;
    private boolean ok;
    private List<TowerItem> towerItems;
    private HashMap<Integer, Color> itemColors = new HashMap<>();
    private TowerFrame frame;
    private static int MARGIN = 20;
    private static int BLOCKSIZE = 20;

    // width in terms of i, this.width in terms of 2i-1, height and maxHeight in terms of i
    public Tower(int width, int maxHeight) {
        if ((width > 0) && (maxHeight > 0)) {
            this.width = 2*width - 1;
            this.height = maxHeight;
            this.visible = true;
            this.ok = true;
            this.towerItems = new ArrayList<>(); 
            this.frame = new TowerFrame(this.width*BLOCKSIZE, this.height*BLOCKSIZE, BLOCKSIZE, MARGIN);

            Canvas.getCanvas(this.width*BLOCKSIZE + 2*MARGIN, this.height*BLOCKSIZE + 2*MARGIN);
            this.makeVisible();
            Cup.clearActiveCups();
        } else {
            String message = "`width` and `height` must be positive natural numbers.";
            this.reportFail(message);
            throw new IllegalArgumentException(message);
        }
    }

    public Tower(int cups) {
        this(cups, (int) Math.pow(cups, 2));

        for (int i=1; i<=cups; i++) {
            this.pushCup(i);
        }
    }
    
    public boolean ok() {
        return this.ok;
    }

    private void reportFail(String message) {
        if (this.visible) {
            JOptionPane.showMessageDialog(null, message);
        }
        this.ok = false;
    }
    
    public void exit() {
        System.exit(0);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int height() {
        int maxHeight = 0;
        for (TowerItem item : this.towerItems) {
            maxHeight = Math.max(maxHeight, item.getHeightReached());
        }
        return maxHeight;
    }

    public void makeVisible() {
        if (this.height() > this.height) {
            this.reportFail("Cannot make visible because the tower is overflowed.");
        } else {
            this.frame.makeVisible();
            for (TowerItem item : this.towerItems) {
                item.makeVisible();
            }
            this.visible = true;
            this.ok = true;
        }
    }

    public void makeInvisible() {
        this.frame.makeInvisible();
        for (TowerItem item : this.towerItems) {
            item.makeInvisible();
        }
        this.visible = false;
        this.ok = true;
    }

    public boolean isVisible() {
        return this.visible;
    }

    private int positionOfItem(int index, boolean isCup) {
        int itemFound = -1;
        int j = 0;
        while (itemFound == -1 && (j < this.towerItems.size())) {
            TowerItem item = this.towerItems.get(j);
            if ((item.isCup() == isCup) && (item.getIndex() == index)) {
                itemFound = j;
            }
            j++;
        }
        return itemFound;
    }

    private boolean hasItem(int index, boolean isCup) {
        return positionOfItem(index, isCup) >= 0;
    }

    private int heightReachedByNewItem(int index, boolean isCup) {
        int newItemHeight = (isCup ? 2*index - 1 : 1);
        int heightReachedByNewItem = newItemHeight;
        if (this.towerItems.size() > 0) {
            int j = this.towerItems.size() - 1;
            boolean foundGreaterEqualWidth = false;
            while (j >= 0 && !foundGreaterEqualWidth) {
                TowerItem item = this.towerItems.get(j);
                int itemIndex = item.getIndex();
                int heightReached = item.getHeightReached();
                int itemHeight = item.height();
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

    /* private boolean isLidCoveringCup(int index) { // the lid must be the last item in the tower
        boolean isCovering = false;
        int lidIndex = this.positionOfItem(index, false);
        int cupIndex = this.positionOfItem(index, true);
        if (lidIndex >= 0 && cupIndex >= 0) {
            Lid lid = (Lid) this.towerItems.get(lidIndex);
            Cup cup = (Cup) this.towerItems.get(cupIndex);
            isCovering = cup.getHeightReached() + 1 == lid.getHeightReached();
        }
        return isCovering;
    } */

    public int[] heightReachedByItems() {
        int[] heightOfItems = new int[this.towerItems.size()];
        for (int i=0; i<this.towerItems.size(); i++) {
            heightOfItems[i] = this.towerItems.get(i).getHeightReached();
        }
        return heightOfItems;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void pushItem(int index, boolean isCup) {
        if (!this.hasItem(index, isCup) && (index > 0) && (2*index-1 <= this.width)) {
            // this is the height the new item will reach
            int newHeight = this.heightReachedByNewItem(index, isCup);
            if (!((newHeight > this.height) && this.visible)) {
                TowerItem newItem = (isCup ?
                    Cup.getCup(index, BLOCKSIZE, MARGIN, this.width, this.height) :
                    Lid.getLid(index, BLOCKSIZE, MARGIN, this.width, this.height)
                );

                if (this.itemColors.get(index) == null) {
                    this.itemColors.put(index, newItem.getColor());
                }
                newItem.setColor(this.itemColors.get(index));
                newItem.setHeightReached(newHeight);
                if (this.visible) {newItem.makeVisible();}

                this.towerItems.add(newItem);
                newItem.updateLiddedState(); // -----------------------
                this.ok = true;
            } else {
                this.reportFail("Adding the new item will result in an overflow of the tower's frame.");
            }
        } else {
            this.reportFail("Either the item already exists, the given size for the item is nonpositive or it is greater than the current allowed width.");
        }
    }

    private void pop() {
        if (!this.towerItems.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();
            int lastIndex = lastItem.getIndex();

            lastItem.makeInvisible();
            // in case there is not the cup or lid to keep saving the cup associated
            if (!(this.hasItem(lastIndex, true) && this.hasItem(lastIndex, false))) {
                lastItem.deactivate();
            }
            this.towerItems.removeLast();
            this.ok = true;
        } else {
            this.reportFail("Cannot pop items from an empty Tower.");
        }
    }

    private void removeStartingAtItem(int index, boolean isCup) {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            for (int j=this.towerItems.size() - 1; j >= itemPosition; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            this.reportFail("The item from which to start removing does not belong to the Tower.");
        }
    }

    private void removeFromPosition(int position) {
        if (0 <= position && position < this.towerItems.size()) {
            for (int j=this.towerItems.size() - 1; j >= position; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            this.reportFail("The given position to remove from should be greater than or equal to 0 and less than the number of items in the Tower.");
        }
    }

    private void removeItem(int index, boolean isCup) {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            TowerItem item = this.towerItems.get(itemPosition);
            boolean isLidded = item.updateLiddedState();
            if (isLidded && item.isCup()) itemPosition++;

            List<TowerItem> itemsSubList = this.towerItems.subList(itemPosition + 1, this.towerItems.size());
            List<TowerItem> itemsAfterItem = new ArrayList<>(itemsSubList);
            this.removeStartingAtItem(index, isLidded || isCup);

            TowerItem lastItem;
            while (!itemsAfterItem.isEmpty()) {
                lastItem = itemsAfterItem.getFirst();
                this.pushItem(lastItem.getIndex(), lastItem.isCup());
                itemsAfterItem.removeFirst();
            }
            this.ok = true;
        } else {
            this.reportFail("The desired item to be removed does not belong to the Tower.");
        }
    }

    private void popItem(boolean isCup) {
        boolean existsItem = false;
        int j = this.towerItems.size() - 1;
        while (!existsItem && (j >= 0)) {
            existsItem = this.towerItems.get(j).isCup() == isCup;
            j--;
        }
        
        if (existsItem) {
            TowerItem itemToRemove = this.towerItems.get(++j);
            this.removeItem(itemToRemove.getIndex(), isCup);
        } else {
            String itemType = (isCup ? "cup" : "lid");
            this.reportFail("There are no " + itemType + "s to pop.");
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
        while (!this.towerItems.isEmpty()) {
            this.pop();
        }
    }
    
    public String[][] stackingItems() {
        int totalItems = this.towerItems.size();
        String[][] stackingItems = new String[totalItems][2];
        for (int i=0; i<totalItems; i++) {
            TowerItem item = this.towerItems.get(i);
            stackingItems[i] = item.asArray();
        }
        return stackingItems;
    }

    public int[] liddedCups() {
        List<Integer> liddedCups = new ArrayList<>();
        for (TowerItem item : this.towerItems) {
            if (item.updateLiddedState() && item.isCup()) {
                liddedCups.add(item.getIndex());
            }
        }
        int[] liddedCupsArray = liddedCups.stream().mapToInt(i -> i).toArray();
        Arrays.sort(liddedCupsArray);
        return liddedCupsArray;
    }

    public void reverseTower() {
        if (!this.towerItems.isEmpty()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            this.clear();

            int j = towerItemsCopy.size() - 1;
            TowerItem item = towerItemsCopy.get(j);
            int newHeight = this.heightReachedByNewItem(item.getIndex(), item.isCup());
            while ((newHeight <= this.height) && (j >= 0)) {
                this.pushItem(item.getIndex(), item.isCup());
                j--;
                if (j >= 0) {
                    item = towerItemsCopy.get(j);
                    newHeight = this.heightReachedByNewItem(item.getIndex(), item.isCup());
                }
            }
        }
        this.ok = true;
    }

    public void orderTower() {
        if (!this.towerItems.isEmpty()) {
            List<TowerItem> itemsDescendingOrder = new ArrayList<>(this.towerItems);
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

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void restoreOrderOfItems(List<TowerItem> copy) {
        this.clear();
        for (TowerItem item : copy) {
            this.pushItem(item.getIndex(), item.isCup());
        }
    }

    public void insert(int index, boolean isCup, int position) {
        if (position == this.towerItems.size()) {
            this.pushItem(index, isCup);
        } else if (0 <= position && position < this.towerItems.size()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            TowerItem itemAtPosition = this.towerItems.get(position);
            if (itemAtPosition.updateLiddedState() && !itemAtPosition.isCup()) position--;

            this.removeFromPosition(position);
            this.pushItem(index, isCup);

            this.ok = true;
            int j = position;
            while (j < towerItemsCopy.size() && this.ok()) {
                TowerItem itemToReinsert = towerItemsCopy.get(j);
                this.pushItem(itemToReinsert.getIndex(), itemToReinsert.isCup());
                j++;
            }

            if (!this.ok()) {
                this.restoreOrderOfItems(towerItemsCopy);
                this.reportFail("Cannot insert the item in the given position because the resulting order will overflow the tower's frame.");
            }
        } else {
            this.reportFail("The given position to insert should be greater than or equal to 0 and less than or equal to the number of items in the Tower.");
        }
    }

    // should include the interaction for lidded cups
    private void swapItems(int index1, boolean isCup1, int index2, boolean isCup2) {
        int position1 = this.positionOfItem(index1, isCup1);
        int position2 = this.positionOfItem(index2, isCup2);
        if (position1 >= 0 && position2 >= 0) {
            if (position1 != position2) {
                int maxPos = Math.max(position1, position2);
                position1 = Math.min(position1, position2);
                position2 = maxPos;
                TowerItem item1 = this.towerItems.get(position1);
                TowerItem item2 = this.towerItems.get(position2);
                index1 = item1.getIndex(); index2 = item2.getIndex();
                isCup1 = item1.isCup(); isCup2 = item2.isCup();
                List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);

                boolean item1IsLidded = item1.updateLiddedState();
                boolean item2IsLidded = item2.updateLiddedState();
                if (item1IsLidded && !isCup1) { position1--; isCup1 = true; }
                if (item2IsLidded && !isCup2) { position2--; isCup2 = true; }
                this.removeItem(index1, isCup1);
                this.removeItem(index2, isCup2);

                this.insert(index2, isCup2, position1);
                if (item2IsLidded) {
                    this.insert(index2, false, position1+1);
                    if (!item1IsLidded) position2++;
                }
                this.insert(index1, isCup1, position2);
                if (item1IsLidded) this.insert(index1, false, position2+1);

                if (!this.ok()) {
                    this.restoreOrderOfItems(towerItemsCopy);
                    this.reportFail("Cannot swap the given items because the resulting order will overflow the tower's frame.");
                }
            } else {this.ok = true;}
        } else {
            this.reportFail("The given items to swap do not both belong to the Tower.");
        }
    }

    public void swap(String[] item1, String[] item2) {
        if (TowerItem.isAValidItem(item1) && TowerItem.isAValidItem(item2)) {
            boolean isCup1 = item1[0].equals("cup");
            boolean isCup2 = item2[0].equals("cup");
            int index1 = Integer.parseInt(item1[1]);
            int index2 = Integer.parseInt(item2[1]);
            this.swapItems(index1, isCup1, index2, isCup2);
        } else {
            this.reportFail("The given items to swap are not valid. They should be string arrays of length 2, where the first element is either `cup` or `lid` and the second element is a positive integer representing the size of the item.");
        }
    }

    public void cover() {
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        int j = 0;
        while (j < towerItemsCopy.size() && this.ok()) {
            TowerItem item = towerItemsCopy.get(j);
            int itemIndex = item.getIndex();
            if (item.isCup() && !item.updateLiddedState()) {
                int positionOfLid = this.positionOfItem(itemIndex, false);
                if (positionOfLid >= 0) {
                    this.removeLid(itemIndex);
                    int positionOfCup = this.positionOfItem(itemIndex, true);
                    this.insert(itemIndex, false, positionOfCup + 1);
                }
            }
            j++;
        }
        if (!this.ok()) {
            this.restoreOrderOfItems(towerItemsCopy);
            this.reportFail("Couldn't cover the cups because it would result in an overflow.");
        }
    }
}