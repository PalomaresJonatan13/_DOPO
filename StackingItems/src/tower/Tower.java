package tower;
import shapes.*;
import exceptions.*;

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
            throw new TowerArgumentException(message);
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
            // wont be displayed because the tower is invisible.
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

    private int heightReachedByNewItem(int index, boolean isCup) { // normal items only
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

    private void pushItem(int index, boolean isCup, String type) throws TowerException {
        if (index <= 0) throw new TowerArgumentException("Push: The index of the item must be positive");
        if (2*index-1 > this.width || (isCup && 2*index-1 > this.height)) {
            throw new TowerArgumentException("Push: The item's width cannot exceed the tower's width");
        }
        if (this.hasItem(index, isCup)) throw new TowerArgumentException("Push: The item already exists.");
        if (!TowerItem.isAValidType(isCup, type)) throw new TowerArgumentException("Push: Invalid type given.");

        
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        TowerItem newItem = TowerItem.getTowerItem(index, isCup, type, BLOCKSIZE, MARGIN, this.width, this.height);
        newItem.enable();
        this.towerItems.add(newItem);
        boolean oldVisibility = this.visible;
        this.visible = false; this.visibleItems = false;
        TowerItem placeholder = newItem.onPush(this);
        if (placeholder.getHeightReached() == 0) {
            this.towerItems.removeLast();
            int newHeight = this.heightReachedByNewItem(index, isCup);
            placeholder.setHeightReached(newHeight);
            this.towerItems.add(placeholder);
        }
        this.visible = oldVisibility; this.visibleItems = oldVisibility;
        if (!(this.height() > this.height && this.visible)) {
            TowerItem lastItem = this.towerItems.getLast();
            if (placeholder == lastItem) this.adjustColorForNewItem(newItem);
            if (this.itemColors.get(index) == null) {
                this.itemColors.put(index, placeholder.getColor());
            }
            newItem.setColor(this.itemColors.get(index));
            newItem.setHeightReached(placeholder.getHeightReached());
            placeholder.disable(); newItem.enable();

            this.towerItems.set(this.towerItems.indexOf(placeholder), newItem);
            newItem.updateLiddedState();
            if (this.visibleItems) this.makeVisible();
            this.ok = true;
        } else {
            placeholder.disable();
            this.restoreTower(towerItemsCopy, oldVisibility);
            this.ok = false;
            throw new TowerException(TowerException.OVERFLOW(index, isCup));
        }
        


        /* int newHeight = this.heightReachedByNewItem(index, isCup);
        if (!((newHeight > this.height) && this.visible)) {
            TowerItem newItem = TowerItem.getTowerItem(index, isCup, BLOCKSIZE, MARGIN, this.width, this.height);
            newItem.enable();

            this.adjustColorForNewItem(newItem);
            if (this.itemColors.get(index) == null) {
                this.itemColors.put(index, newItem.getColor());
            }
            newItem.setColor(this.itemColors.get(index));
            newItem.setHeightReached(newHeight);
            if (this.visibleItems) newItem.makeVisible();

            this.towerItems.add(newItem);
            newItem.updateLiddedState();
            this.ok = true;
        } else {
            throw new TowerException(TowerException.OVERFLOW(index, isCup));
        } */
    }

    private void safePush(int index, boolean isCup, String type) {
        if (isCup) {
            this.pushCup(index, type);
        } else {
            this.pushLid(index, type);
        }
    }

    void pop() {
        if (!this.towerItems.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();

            lastItem.disable();
            this.towerItems.removeLast();
            this.ok = true;
        } else {
            throw new TowerStateException("Pop: Cannot pop items from an empty Tower.");
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
            throw new TowerArgumentException("Remove: The item from which to start removing does not belong to the Tower.");
        }
    }

    private void removeFromPosition(int position) {
        if (0 <= position && position < this.numberOfItems()) {
            for (int j=this.numberOfItems() - 1; j >= position; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            throw new TowerArgumentException("Remove: The given position to remove from should be greater than or equal to 0 and less than the number of items in the Tower.");
        }
    }

    private void removeItem(int index, boolean isCup) throws TowerException {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            TowerItem item = this.towerItems.get(itemPosition);
            if (item.isRemovable()) {
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
                        this.safePush(lastItem.getIndex(), lastItem.isCup(), lastItem.getType());
                    }
                    itemsAfterItem.removeFirst();
                }
                if (isVisible) this.makeItemsVisible();
                this.ok = true;
            } else {
                throw new TowerException(TowerException.REMOVING_NOT_REMOVABLE_ITEM(item.getIndex(), item.isCup(), item.getType()));
            }
        } else {
            throw new TowerArgumentException("Remove: The desired item to be removed does not belong to the Tower.");
        }
    }

    private void popItem(boolean isCup) throws TowerException {
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
            throw new TowerStateException("Pop: There is no " + itemType + " to pop.");
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public void pushCup(int index, String type) {
        try {
            this.pushItem(index, true, type);
        } catch (TowerException | TowerArgumentException e) {
            this.reportFail(e.getMessage());
        }
    }

    public void pushCup(int index) {
        this.pushCup(index, Cup.NORMAL);
    }

    public void popCup() {
        try {
            this.popItem(true);
        } catch (TowerException | TowerStateException e) {
            this.reportFail(e.getMessage());
        }
    }

    public void removeCup(int index) {
        try {
            this.removeItem(index, true);
        } catch (TowerException | TowerArgumentException e) {
            this.reportFail(e.getMessage());
        }
    }

    public void pushLid(int index, String type) {
        try {
            this.pushItem(index, false, type);
        } catch (TowerException | TowerArgumentException e) {
            this.reportFail(e.getMessage());
        }
    }
    
    public void pushLid(int index) {
        this.pushLid(index, Lid.NORMAL);
    }
    
    public void popLid() {
        try {
            this.popItem(false);
        } catch (TowerException | TowerStateException e) {
            this.reportFail(e.getMessage());
        }
    }
    
    public void removeLid(int index) {
        try {
            this.removeItem(index, false);
        } catch (TowerException | TowerArgumentException e) {
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
                this.safePush(item.getIndex(), item.isCup(), item.getType());
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
            int newHeight = 0;
            while ((j < itemsDescendingOrder.size()) && (newHeight <= this.height)) {
                TowerItem item = itemsDescendingOrder.get(j);
                int itemIndex = item.getIndex();
                boolean isCup = item.isCup();
                String itemType = item.getType();
                
                // in descending order, the l comes before the c, so if the cup is before the lid, we push the cup
                if (!isCup && itemsDescendingOrder.size() > j + 1) {
                    TowerItem nextItem = itemsDescendingOrder.get(j+1);
                    if (nextItem.getIndex() == itemIndex && nextItem.isCup()) {
                        newHeight = this.heightReachedByNewItem(itemIndex, true);
                        if (newHeight <= this.height) {
                            this.pushCup(itemIndex, itemType);
                            j++;
                        }
                    }
                }

                newHeight = this.heightReachedByNewItem(itemIndex, isCup);
                if (newHeight <= this.height) {
                    this.safePush(itemIndex, isCup, itemType);
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

    private void restoreTower(List<TowerItem> copy, boolean isVisible) {
        this.clear();
        this.visibleItems = false;

        try {
            for (TowerItem item : copy) {
                this.pushItem(item.getIndex(), item.isCup(), item.getType());
            }
        } catch (TowerException e) {
            throw new TowerArgumentException("Restore: The given list of TowerItems is not a valid copy (cannot push all the items in the copy given because there would be an overflow).");
        }
        if (isVisible) this.makeItemsVisible();
    }

    // this insert does no check for inserting inside lidded cups, or in the position of a lidded item
    void insert(int index, boolean isCup, String type, int position) throws TowerException {
        if (position == this.numberOfItems()) {
            this.pushItem(index, isCup, type);
        } else if (0 <= position && position < this.numberOfItems()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            boolean isVisible = this.visibleItems;
            this.visibleItems = false;
            try {
                this.removeFromPosition(position);
                this.pushItem(index, isCup, type);

                this.ok = true;
                int j = position;
                while (j < towerItemsCopy.size() && this.ok()) {
                    TowerItem itemToReinsert = towerItemsCopy.get(j);
                    this.pushItem(itemToReinsert.getIndex(), itemToReinsert.isCup(), itemToReinsert.getType());
                    j++;
                }
                if (isVisible) this.makeItemsVisible();
            } catch (TowerException e) {
                this.restoreTower(towerItemsCopy, isVisible);
                throw new TowerException(TowerException.OVERFLOW(index, isCup));
            }
        } else {
            throw new TowerArgumentException("Insert: The given position to insert should be greater than or equal to 0 and less than or equal to the number of items in the Tower.");
        }
    }

    private int itemEnclosing(int index, boolean isCup) {
        int containerIndex = 0;
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            int itemHeightReached = this.towerItems.get(itemPosition).getHeightReached();

            int j = this.numberOfItems()-1;
            while (j > itemPosition && containerIndex == 0) {
                TowerItem item = this.towerItems.get(j);
                int itemIndex = item.getIndex();
                if (item.updateLiddedState() && !item.isCup() && itemIndex != index) {
                    TowerItem cup = TowerItem.getTowerItem(itemIndex, true);
                    if (itemHeightReached > item.getHeightReached() - cup.height()) {
                        containerIndex = itemIndex;
                    }
                }
                j--;
            }
        }
        return containerIndex;
    }

    private void swapItems(int index1, boolean isCup1, int index2, boolean isCup2) throws TowerException {
        int containerIndex = this.itemEnclosing(index1, isCup1);
        if (containerIndex != 0) {
            this.ok = false;
            throw new TowerException(TowerException.SWAP_INSIDE_LIDDED_CUP(index1, isCup1, containerIndex));
        } else containerIndex = this.itemEnclosing(index2, isCup2);
        if (containerIndex != 0) {
            this.ok = false;
            throw new TowerException(TowerException.SWAP_INSIDE_LIDDED_CUP(index2, isCup2, containerIndex));
        }
        
        int position1 = this.positionOfItem(index1, isCup1);
        int position2 = this.positionOfItem(index2, isCup2);
        if (position1 >= 0 && position2 >= 0) {
            if (position1 != position2) {
                List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
                int maxPos = Math.max(position1, position2);
                position1 = Math.min(position1, position2);
                position2 = maxPos;
                TowerItem item1 = this.towerItems.get(position1);
                TowerItem item2 = this.towerItems.get(position2);
                index1 = item1.getIndex(); index2 = item2.getIndex();
                isCup1 = item1.isCup(); isCup2 = item2.isCup();

                boolean item1IsLidded = item1.updateLiddedState();
                boolean item2IsLidded = item2.updateLiddedState();
                if (item1IsLidded && !isCup1) this.swapItems(index1, true, index2, isCup2);
                else if (item2IsLidded && !isCup2) this.swapItems(index1, isCup1, index2, true);
                else {
                    boolean isVisible = this.visibleItems;
                    this.visibleItems = false;
                    try {
                        String item1LidType = null; String item2LidType = null;
                        if (item1IsLidded) item1LidType = ((Cup) item1).lid().getType();
                        if (item2IsLidded) item2LidType = ((Cup) item2).lid().getType();
                        this.removeItem(index1, isCup1);
                        this.removeItem(index2, isCup2);

                        this.insert(index2, isCup2, item2.getType(), position1);
                        if (item2IsLidded) {
                            this.insert(index2, false, item2LidType, position1+1);
                            if (!item1IsLidded) position2++;
                        } else if (item1IsLidded) position2--;
                        this.insert(index1, isCup1, item1.getType(), position2);
                        if (item1IsLidded) this.insert(index1, false, item1LidType, position2+1);
                        if (isVisible) this.makeItemsVisible();
                    } catch (TowerException e) {
                        this.restoreTower(towerItemsCopy, isVisible);
                        throw new TowerException(TowerException.IMPOSSIBLE_SWAP(index1, isCup1, index2, isCup2));
                    }
                }
            } else {this.ok = true;}
        } else {
            throw new TowerArgumentException("Swap: The given items to swap do not both belong to the Tower.");
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
            } catch (TowerException | TowerArgumentException e) {
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
                    this.restoreTower(towerItemsCopy, false);
                } catch (TowerException e) {
                    /* If the swap operation is unsuccessful, we know that with that pair of items we dont get a reduction of the height, therefore, we continue with the next pair */
                }
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

        boolean isVisible = this.visibleItems;
        this.visibleItems = false;
        try {
            int j = 0;
            while (j < towerItemsCopy.size() && this.ok()) {
                TowerItem item = towerItemsCopy.get(j);
                int itemIndex = item.getIndex();
                String itemType = item.getType();
                if (item.isCup()) {
                    this.pushItem(itemIndex, true, itemType);
                    int positionOfLid = this.positionOfItem(itemIndex, false);
                    int positionOfLidInCopy = -1;
                    TowerItem lid = null;
                    for (TowerItem itemInCopy : towerItemsCopy) {
                        if (itemInCopy.toString().equals("Lid("+itemIndex+")")) {
                            positionOfLidInCopy = towerItemsCopy.indexOf(itemInCopy);
                            lid = itemInCopy;
                        }
                    }
                    if (positionOfLid >= 0) {
                        this.removeItem(itemIndex, false);
                        this.pushItem(itemIndex, false, lid.getType());
                    } else if (positionOfLidInCopy >= 0) {
                        this.pushItem(itemIndex, false, lid.getType());
                        lidsExtracted.add(itemIndex);
                    }
                } else if (!lidsExtracted.contains(itemIndex)) {
                    this.pushItem(itemIndex, false, itemType);
                }
                j++;
            }
            if (isVisible) this.makeItemsVisible();
        } catch (TowerException e) {
            this.restoreTower(towerItemsCopy, isVisible);
            this.reportFail("Couldn't cover the cups because it would result in an overflow.");
        }
    }
}