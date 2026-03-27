package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;
import javax.swing.JOptionPane;


public class Tower {
    private int width;
    private int height;
    private boolean visible;
    private boolean visibleItems;
    private boolean ok;
    private TowerFrame frame;
    private List<TowerItem> towerItems;
    private HashMap<Integer, Color> itemColors = new HashMap<>();
    private static int MARGIN = 20;
    private static int BLOCKSIZE = 20;

    // width in terms of i, this.width in terms of 2i-1, height and maxHeight in terms of i
    public Tower(int width, int maxHeight) {
        if ((width > 0) && (maxHeight > 0)) {
            this.width = 2*width - 1;
            this.height = maxHeight;
            this.visible = true;
            this.visibleItems = true;
            this.ok = true;
            this.towerItems = new ArrayList<>(); 
            this.frame = new TowerFrame(this.width*BLOCKSIZE, this.height*BLOCKSIZE, BLOCKSIZE, MARGIN);

            Canvas.getCanvas(this.width*BLOCKSIZE + 2*MARGIN, this.height*BLOCKSIZE + 2*MARGIN);
            this.makeVisible();
            TowerItem.clearActiveItems();
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

    private int numberOfItems() {
        return this.towerItems.size();
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

    private void makeItemsVisible() {
        for (TowerItem item : this.towerItems) {
            item.makeVisible();
        }
        this.visibleItems = true;
        this.visible = true;
    }

    private void makeItemsInvisible() {
        for (TowerItem item : this.towerItems) {
            item.makeInvisible();
        }
        this.visibleItems = false;
        this.visible = false;
    }

    public void makeVisible() {
        if (this.height() <= this.height) {
            this.frame.makeVisible();
            this.makeItemsVisible();
            this.ok = true;
        } else {
            this.reportFail("Cannot make visible because the tower is overflowed.");
        }
    }

    public void makeInvisible() {
        this.frame.makeInvisible();
        this.makeItemsInvisible();
        this.ok = true;
    }

    public boolean isVisible() {
        return this.visible;
    }

    private int positionOfItem(int index, boolean isCup) {
        int itemFound = -1;
        int j = 0;
        while (itemFound == -1 && (j < this.numberOfItems())) {
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
        if (this.numberOfItems() > 0) {
            int j = this.numberOfItems() - 1;
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

    public int[] heightReachedByItems() {
        int[] heightOfItems = new int[this.numberOfItems()];
        for (int i=0; i<this.numberOfItems(); i++) {
            heightOfItems[i] = this.towerItems.get(i).getHeightReached();
        }
        return heightOfItems;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void adjustColorForNewItem(TowerItem newItem) {
        if (!this.towerItems.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();
            Color currentColor = newItem.getColor();
            if (newItem.getIndex() != lastItem.getIndex() && currentColor == lastItem.getColor()) {
                newItem.setColor(TowerItem.randomItemColor(currentColor));
            }
        }
    }

    private void pushItem(int index, boolean isCup) {
        if (!this.hasItem(index, isCup) && (index > 0) && (2*index-1 <= this.width)) {
            // this is the height the new item will reach
            int newHeight = this.heightReachedByNewItem(index, isCup);
            if (!((newHeight > this.height) && this.visible)) {
                TowerItem newItem = (isCup ?
                    Cup.getCup(index, BLOCKSIZE, MARGIN, this.width, this.height) :
                    Lid.getLid(index, BLOCKSIZE, MARGIN, this.width, this.height)
                );

                this.adjustColorForNewItem(newItem);
                if (this.itemColors.get(index) == null) {
                    this.itemColors.put(index, newItem.getColor());
                }
                newItem.setColor(this.itemColors.get(index));
                newItem.setHeightReached(newHeight);
                if (this.visibleItems) newItem.makeVisible();

                this.towerItems.add(newItem);
                newItem.updateLiddedState();
                newItem.enable();
                this.ok = true;
            } else {
                throw new IllegalStateException("Adding the new item will result in an overflow of the tower's frame.");
            }
        } else {
            throw new IllegalArgumentException("Either the item already exists, the given size for the item is nonpositive or it is greater than the current allowed width.");
        }
    }

    private void pop() {
        if (!this.towerItems.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();

            lastItem.disable();
            this.towerItems.removeLast();
            this.ok = true;
        } else {
            throw new IllegalStateException("Cannot pop items from an empty Tower.");
        }
    }

    private void removeStartingAtItem(int index, boolean isCup) {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            for (int j=this.numberOfItems() - 1; j >= itemPosition; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            throw new IllegalArgumentException("The item from which to start removing does not belong to the Tower.");
        }
    }

    private void removeFromPosition(int position) {
        if (0 <= position && position < this.numberOfItems()) {
            for (int j=this.numberOfItems() - 1; j >= position; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            throw new IllegalArgumentException("The given position to remove from should be greater than or equal to 0 and less than the number of items in the Tower.");
        }
    }

    private void removeItem(int index, boolean isCup) {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            TowerItem item = this.towerItems.get(itemPosition);
            boolean isLidded = item.updateLiddedState();
            if (isLidded && !item.isCup()) itemPosition = this.positionOfItem(index, true);

            List<TowerItem> itemsSubList = this.towerItems.subList(itemPosition + 1, this.numberOfItems());
            List<TowerItem> itemsAfterItem = new ArrayList<>(itemsSubList);
            this.removeStartingAtItem(index, isLidded || isCup);

            boolean isVisible = this.visibleItems;
            this.visibleItems = false;
            TowerItem lastItem;
            while (!itemsAfterItem.isEmpty()) {
                lastItem = itemsAfterItem.getFirst();
                if (!(isLidded && lastItem.getIndex() == index)) {
                    this.pushItem(lastItem.getIndex(), lastItem.isCup());
                }
                itemsAfterItem.removeFirst();
            }
            if (isVisible) this.makeItemsVisible();
            this.ok = true;
        } else {
            throw new IllegalArgumentException("The desired item to be removed does not belong to the Tower.");
        }
    }

    private void popItem(boolean isCup) {
        boolean existsItem = false;
        int j = this.numberOfItems() - 1;
        while (!existsItem && (j >= 0)) {
            existsItem = this.towerItems.get(j).isCup() == isCup;
            j--;
        }
        
        if (existsItem) {
            TowerItem itemToRemove = this.towerItems.get(++j);
            this.removeItem(itemToRemove.getIndex(), isCup);
        } else {
            String itemType = (isCup ? "cup" : "lid");
            throw new IllegalStateException("There are no " + itemType + "s to pop.");
        }
    }

    public void pushCup(int index) {
        try {
            this.pushItem(index, true);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
    }

    public void popCup() {
        try {
            this.popItem(true);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
    }

    public void removeCup(int index) {
        try {
            this.removeItem(index, true);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
    }
    
    public void pushLid (int index) {
        try {
            this.pushItem(index, false);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
    }
    
    public void popLid () {
        try {
            this.popItem(false);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
    }
    
    public void removeLid (int index) {
        try {
            this.removeItem(index, false);
        } catch (Exception e) {
            this.reportFail(e.getMessage());
        }
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
        int totalItems = this.numberOfItems();
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
            boolean isVisible = this.visibleItems;
            this.visibleItems = false;

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
            if (isVisible) this.makeItemsVisible();
        }
        this.ok = true;
    }

    public void orderTower() {
        if (!this.towerItems.isEmpty()) {
            List<TowerItem> itemsDescendingOrder = new ArrayList<>(this.towerItems);
            itemsDescendingOrder.sort((a, b) -> Integer.compare(b.getIndex(), a.getIndex()));
            this.clear();
            boolean isVisible = this.visibleItems;
            this.visibleItems = false;

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
            if (isVisible) this.makeItemsVisible();
        } 
        this.ok = true;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void restoreOrderOfItems(List<TowerItem> copy) {
        this.clear();
        boolean isVisible = this.visibleItems;
        this.visibleItems = false;
        for (TowerItem item : copy) {
            this.pushItem(item.getIndex(), item.isCup());
        }
        if (isVisible) this.makeItemsVisible();
    }

    private void insert(int index, boolean isCup, int position) {
        if (position == this.numberOfItems()) {
            this.pushItem(index, isCup);
        } else if (0 <= position && position < this.numberOfItems()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            try {
                TowerItem itemAtPosition = this.towerItems.get(position);
                if (itemAtPosition.updateLiddedState() && !itemAtPosition.isCup()) position--;

                this.removeFromPosition(position);
                boolean isVisible = this.visibleItems;
                this.visibleItems = false;
                this.pushItem(index, isCup);

                this.ok = true;
                int j = position;
                while (j < towerItemsCopy.size() && this.ok()) {
                    TowerItem itemToReinsert = towerItemsCopy.get(j);
                    this.pushItem(itemToReinsert.getIndex(), itemToReinsert.isCup());
                    j++;
                }
                if (isVisible) this.makeItemsVisible();
            } catch (Exception e) {
                this.restoreOrderOfItems(towerItemsCopy);
                throw new IllegalStateException("Cannot insert the item in the given position because the resulting order will overflow the tower's frame.");
            }
        } else {
            throw new IllegalArgumentException("The given position to insert should be greater than or equal to 0 and less than or equal to the number of items in the Tower.");
        }
    }

    private void swapItems(int index1, boolean isCup1, int index2, boolean isCup2) {
        int position1 = this.positionOfItem(index1, isCup1);
        int position2 = this.positionOfItem(index2, isCup2);
        if (position1 >= 0 && position2 >= 0) {
            if (position1 != position2) {
                List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
                try {
                    int maxPos = Math.max(position1, position2);
                    position1 = Math.min(position1, position2);
                    position2 = maxPos;
                    TowerItem item1 = this.towerItems.get(position1);
                    TowerItem item2 = this.towerItems.get(position2);
                    index1 = item1.getIndex(); index2 = item2.getIndex();
                    isCup1 = item1.isCup(); isCup2 = item2.isCup();

                    boolean item1IsLidded = item1.updateLiddedState();
                    boolean item2IsLidded = item2.updateLiddedState();
                    if (item1IsLidded && !isCup1) { position1--; isCup1 = true; }
                    if (item2IsLidded && !isCup2) { position2--; isCup2 = true; }

                    boolean isVisible = this.visibleItems;
                    this.visibleItems = false;
                    this.removeItem(index1, isCup1);
                    this.removeItem(index2, isCup2);

                    this.insert(index2, isCup2, position1);
                    if (item2IsLidded) {
                        this.insert(index2, false, position1+1);
                        if (!item1IsLidded) position2++;
                    }
                    this.insert(index1, isCup1, position2);
                    if (item1IsLidded) this.insert(index1, false, position2+1);
                    if (isVisible) this.makeItemsVisible();
                } catch (Exception e) {
                    this.restoreOrderOfItems(towerItemsCopy);
                    throw new IllegalStateException("Cannot swap the given items because the resulting order will overflow the tower's frame.");
                }
            } else {this.ok = true;}
        } else {
            throw new IllegalArgumentException("The given items to swap do not both belong to the Tower.");
        }
    }

    public void swap(String[] item1, String[] item2) {
        if (TowerItem.isAValidItem(item1) && TowerItem.isAValidItem(item2)) {
            boolean isCup1 = item1[0].equals("cup");
            boolean isCup2 = item2[0].equals("cup");
            int index1 = Integer.parseInt(item1[1]);
            int index2 = Integer.parseInt(item2[1]);
            try {
                this.swapItems(index1, isCup1, index2, isCup2);
            } catch (Exception e) {
                this.reportFail(e.getMessage());
            }
        } else {
            this.reportFail("The given items to swap are not valid. They should be string arrays of length 2, where the first element is either `cup` or `lid` and the second element is a positive integer representing the size of the item.");
        }
    }

    public String[][] swapToReduce() {
        boolean heightReduced = false;
        int lastHeight = this.height();
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        TowerItem firstItem = null;
        TowerItem secondItem = null;
        boolean isVisible = this.visibleItems;
        this.visibleItems = false;

        int j = 0;
        while (j < this.numberOfItems() && !heightReduced) {
            firstItem = this.towerItems.get(j);
            int k = j+1;
            while (k < this.numberOfItems() && !heightReduced) {
                secondItem = this.towerItems.get(k);
                try {
                    this.swapItems(
                        firstItem.getIndex(), firstItem.isCup(),
                        secondItem.getIndex(), secondItem.isCup()
                    );
                    heightReduced = this.height() < lastHeight;
                    this.restoreOrderOfItems(towerItemsCopy);
                } catch (Exception e) {}
                k++;
            }
            j++;
        }
        if (isVisible) this.makeItemsVisible();

        this.ok = true;
        return (heightReduced ?
            new String[][] {firstItem.asArray(), secondItem.asArray()} :
            new String[][] {}
        );
    }

    public void cover() {
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        this.clear();
        List<Integer> lidsExtracted = new ArrayList<>();

        try {
            boolean isVisible = this.visibleItems;
            this.visibleItems = false;
            int j = 0;
            while (j < towerItemsCopy.size() && this.ok()) {
                TowerItem item = towerItemsCopy.get(j);
                int itemIndex = item.getIndex();
                if (item.isCup()) {
                    this.pushItem(itemIndex, true);
                    int positionOfLid = this.positionOfItem(itemIndex, false);
                    int positionOfLidInCopy = -1;
                    for (TowerItem itemInCopy : towerItemsCopy) {
                        if (itemInCopy.toString().equals("Lid("+itemIndex+")")) {
                            positionOfLidInCopy = towerItemsCopy.indexOf(itemInCopy);
                        }
                    }
                    if (positionOfLid >= 0) {
                        this.removeItem(itemIndex, false);
                        this.pushItem(itemIndex, false);
                    } else if (positionOfLidInCopy >= 0) {
                        this.pushItem(itemIndex, false);
                        lidsExtracted.add(itemIndex);
                    }
                } else if (!lidsExtracted.contains(itemIndex)) {
                    this.pushItem(itemIndex, false);
                }
                j++;
            }
            if (isVisible) this.makeItemsVisible();
        } catch (Exception e) {
            this.restoreOrderOfItems(towerItemsCopy);
            this.reportFail("Couldn't cover the cups because it would result in an overflow.");
        }
    }
}