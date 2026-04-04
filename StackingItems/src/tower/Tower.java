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
    public static final int MARGIN = 20;
    public static final int BLOCKSIZE = 20;

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

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void reportFail(String message) {
        if (this.visible) {
            JOptionPane.showMessageDialog(null, message);
        }
        this.ok = false;
    }
    
    public void exit() {
        System.exit(0);
    }

    public boolean ok() {
        return this.ok;
    }

    private int numberOfItems() {
        return this.towerItems.size();
    }

    private boolean isEmpty() {
        return this.towerItems.isEmpty();
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void makeItemsVisible() {
        for (TowerItem item : this.towerItems) {
            item.makeVisible();
        }
        this.visibleItems = true;
    }

    private void makeItemsInvisible() {
        if (this.visibleItems) {
            for (TowerItem item : this.towerItems) {
                item.makeInvisible();
            }
            this.visibleItems = false;
        }
    }

    public void makeVisible() {
        if (this.height() <= this.height) {
            this.frame.makeVisible();
            this.makeItemsVisible();
            this.visible = true;
            this.ok = true;
        } else {
            // wont be displayed because the tower is invisible.
            this.reportFail("Cannot make visible because the tower is overflowed.");
        }
    }

    public void makeInvisible() {
        this.frame.makeInvisible();
        this.makeItemsInvisible();
        this.visible = false;
        this.ok = true;
    }

    public boolean isVisible() {
        return this.visible;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

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

    private int positionOfNextGreaterIndex(int index, boolean isCup) {
        int positionOfGreaterIndex = -1;

        if (this.hasItem(index, isCup)) {
            int position = this.positionOfItem(index, isCup);
            int j = position + 1;
            boolean foundGreaterIndex = false;
            while (j < this.numberOfItems() && !foundGreaterIndex) {
                TowerItem item = this.towerItems.get(j);
                foundGreaterIndex = item.getIndex() > index;
                j++;
            }
            if (foundGreaterIndex) positionOfGreaterIndex = --j;
        } else {
            throw new TowerArgumentException("Position: The item for which to make the operations does not belong to the tower.");
        }

        return positionOfGreaterIndex;
    }

    private int itemEnclosingItem(int index, boolean isCup) {
        int containerIndex = -1;
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            int itemHeightReached = this.towerItems.get(itemPosition).getHeightReached();

            int j = this.numberOfItems()-1;
            while (j > itemPosition && containerIndex == -1) {
                TowerItem item = this.towerItems.get(j);
                int itemIndex = item.getIndex();
                if (item.isLidded() && !item.isCup() && itemIndex != index) {
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

    private int heightReachedByNewItem(int index, boolean isCup) { // normal items only
        int newItemHeight = (isCup ? 2*index - 1 : 1);
        int heightReachedByNewItem = newItemHeight;
        if (!this.isEmpty()) {
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

    void pop() {
        if (!this.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();

            lastItem.disable();
            this.towerItems.removeLast();
            this.ok = true;
        } else {
            throw new TowerStateException("Pop: Cannot pop items from an empty Tower.");
        }
    }

    private void clear() {
        while (!this.isEmpty()) {
            this.pop();
        }
    }

    private void removeFromPosition(int position) {
        if (0 <= position && position < this.numberOfItems()) {
            for (int j=this.numberOfItems()-1; j >= position; j--) {
                this.pop();
            }
            this.ok = true;
        } else {
            throw new TowerArgumentException("Remove: The given position to remove from should be greater than or equal to 0 and less than the number of items in the Tower.");
        }
    }

    private void remove(int index, boolean isCup) {
        try {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            boolean isVisible = this.visibleItems;
            this.makeItemsInvisible();
            int position = this.positionOfItem(index, isCup);
            this.removeFromPosition(position);
            for (int j=position+1; j<towerItemsCopy.size(); j++) {
                TowerItem item = towerItemsCopy.get(j);
                this.uncheckedPush(item.getIndex(), item.isCup(), item.getType());
            }
            if (isVisible) this.makeItemsVisible();
        } catch (TowerArgumentException e) {
            throw new TowerArgumentException("Remove: The item to remove does not belong to the Tower.");
        }
    }

    private void restoreTower(List<TowerItem> copy, boolean isVisible) {
        this.clear();
        this.makeItemsInvisible();

        try {
            for (TowerItem item : copy) {
                this.pushItem(item.getIndex(), item.isCup(), item.getType());
            }
        } catch (TowerException e) {
            throw new TowerArgumentException("Restore: The given list of TowerItems is not a valid copy (cannot push all the items in the copy given because there would be an overflow).");
        }
        if (isVisible) this.makeItemsVisible();
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void adjustColorForNewItem(TowerItem newItem) {
        if (!this.isEmpty()) {
            TowerItem lastItem = this.towerItems.getLast();
            Color currentColor = newItem.getColor();
            if (newItem.getIndex() != lastItem.getIndex() && currentColor == lastItem.getColor()) {
                newItem.setColor(TowerItem.randomItemColor(currentColor));
            }
        }
    }

    private TowerItem prepareNewItem(TowerItem newItem) throws TowerException {
        newItem.enable();
        this.towerItems.add(newItem);
        TowerItem placeholder = newItem.onPush(this);

        if (placeholder.getHeightReached() == 0) {
            this.towerItems.removeLast();
            int newHeight = this.heightReachedByNewItem(newItem.getIndex(), newItem.isCup());
            placeholder.setHeightReached(newHeight);
            this.towerItems.add(placeholder);
        }

        newItem.setHeightReached(placeholder.getHeightReached());
        placeholder.disable();
        return placeholder;
    }

    private void uncheckedPush(int index, boolean isCup, String type) {
        if (isCup) {
            this.pushCup(index, type);
        } else {
            this.pushLid(index, type);
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private void pushItem(int index, boolean isCup, String type) throws TowerException {
        if (index <= 0) throw new TowerArgumentException("Push: The index of the item must be positive");
        if (2*index-1 > this.width || (isCup && 2*index-1 > this.height)) {
            throw new TowerArgumentException("Push: The item does not fit in the tower.");
        }
        if (this.hasItem(index, isCup)) throw new TowerArgumentException("Push: The item already exists.");
        if (!TowerItem.isAValidType(isCup, type)) throw new TowerArgumentException("Push: Invalid type given.");
        
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        TowerItem newItem = TowerItem.getTowerItem(index, isCup, type, this.width, this.height);
        boolean oldVisibility = this.visibleItems;
        this.makeItemsInvisible();
        TowerItem placeholder = this.prepareNewItem(newItem);
        this.visibleItems = oldVisibility;

        if (!(this.height() > this.height && this.visible)) {
            HashMap<Integer, Color> colors = this.itemColors;
            if (placeholder == this.towerItems.getLast()) this.adjustColorForNewItem(newItem);
            if (colors.get(index) == null) colors.put(index, placeholder.getColor());
            newItem.setColor(colors.get(index));
            
            this.towerItems.set(this.towerItems.indexOf(placeholder), newItem);
            newItem.enable();
            newItem.updateLiddedState();
            if (this.visibleItems) this.makeItemsVisible();
            this.ok = true;
        } else {
            this.restoreTower(towerItemsCopy, this.visibleItems);
            this.ok = false;
            throw new TowerException(TowerException.OVERFLOW(index, isCup));
        }
    }

    private void removeItem(int index, boolean isCup) throws TowerException {
        if (this.hasItem(index, isCup)) {
            int itemPosition = this.positionOfItem(index, isCup);
            TowerItem item = this.towerItems.get(itemPosition);
            if (item.isRemovable()) {
                boolean isLidded = item.isLidded();
                if (isLidded) {
                    int associatedItemPosition = this.positionOfItem(index, !isCup);
                    itemPosition = Math.min(itemPosition, associatedItemPosition);
                }

                List<TowerItem> itemsSubList = this.towerItems.subList(itemPosition + 1, this.numberOfItems());
                List<TowerItem> itemsAfterItem = new ArrayList<>(itemsSubList);
                this.removeFromPosition(itemPosition);

                boolean isVisible = this.visibleItems;
                this.makeItemsInvisible();
                while (!itemsAfterItem.isEmpty()) {
                    TowerItem lastItem = itemsAfterItem.removeFirst();
                    if (!(isLidded && lastItem.getIndex() == index)) {
                        this.uncheckedPush(lastItem.getIndex(), lastItem.isCup(), lastItem.getType());
                    }
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
            if (item.isLidded() && item.isCup()) {
                liddedCups.add(item.getIndex());
            }
        }
        int[] liddedCupsArray = liddedCups.stream().mapToInt(i -> i).toArray();
        Arrays.sort(liddedCupsArray);
        return liddedCupsArray;
    }

    public void reverseTower() {
        if (!this.isEmpty()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            this.clear();
            boolean isVisible = this.visibleItems;
            this.makeItemsInvisible();

            while (!towerItemsCopy.isEmpty()) {
                TowerItem item = towerItemsCopy.removeLast();
                this.uncheckedPush(item.getIndex(), item.isCup(), item.getType());
            }
            while (this.height() > this.height) {
                this.pop();
            }

            if (isVisible) this.makeItemsVisible();
        }
        this.ok = true;
    }

    public void orderTower() {
        List<TowerItem> itemsDescendingOrder = new ArrayList<>(this.towerItems);
        itemsDescendingOrder.sort((a, b) -> Integer.compare(b.getIndex(), a.getIndex()));
        this.clear();
        boolean isVisible = this.visibleItems;
        this.makeItemsInvisible();

        while (!itemsDescendingOrder.isEmpty()) {
            TowerItem nextItem = itemsDescendingOrder.removeFirst();
            if (nextItem.isCup() && !this.isEmpty()) {
                TowerItem lastItem = this.towerItems.getLast();
                if (lastItem.getIndex() == nextItem.getIndex()) {
                    this.pop();
                    this.pushCup(nextItem.getIndex(), nextItem.getType());
                    nextItem = lastItem;
                }
            }
            this.uncheckedPush(nextItem.getIndex(), nextItem.isCup(), nextItem.getType());
        }
        while (this.height() > this.height) {
            this.pop();
        }

        if (isVisible) this.makeItemsVisible();
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    // this insert does no check for inserting inside lidded cups, or in the position of a lidded item
    void insert(int index, boolean isCup, String type, int position) throws TowerException {
        if (position == this.numberOfItems()) {
            this.pushItem(index, isCup, type);
        } else if (0 <= position && position < this.numberOfItems()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            boolean isVisible = this.visibleItems;
            this.makeItemsInvisible();
            try {
                this.removeFromPosition(position);
                this.pushItem(index, isCup, type);

                this.ok = true;
                int j = position;
                while (j < towerItemsCopy.size()) {
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

    private void swapItems(int index1, boolean isCup1, int index2, boolean isCup2) throws TowerException {
        int position1 = this.positionOfItem(index1, isCup1);
        int position2 = this.positionOfItem(index2, isCup2);
        if (position1 >= 0 && position2 >= 0) {
            if (position1 != position2) {
                List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
                int maxPos = Math.max(position1, position2);
                position1 = Math.min(position1, position2); position2 = maxPos;
                TowerItem item1 = this.towerItems.get(position1);
                TowerItem item2 = this.towerItems.get(position2);

                boolean isVisible = this.visibleItems;
                this.makeItemsInvisible();
                try {
                    this.removeFromPosition(position1);
                    this.pushItem(item2.getIndex(), item2.isCup(), item2.getType());
                    int j = position1;
                    while (++j < position2) {
                        TowerItem item = towerItemsCopy.get(j);
                        this.pushItem(item.getIndex(), item.isCup(), item.getType());
                    }
                    this.pushItem(item1.getIndex(), item1.isCup(), item1.getType());
                    while (++j < towerItemsCopy.size()) {
                        TowerItem item = towerItemsCopy.get(j);
                        this.pushItem(item.getIndex(), item.isCup(), item.getType());
                    }
                    if (isVisible) this.makeItemsVisible();
                } catch (TowerException e) {
                    this.restoreTower(towerItemsCopy, isVisible);
                    throw new TowerException(TowerException.IMPOSSIBLE_SWAP(index1, isCup1, index2, isCup2));
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
        this.makeItemsInvisible();

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

    private void coverCup(int index) throws TowerException {
        if (this.hasItem(index, true) && this.hasItem(index, false)) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            int position = this.positionOfItem(index, true);
            TowerItem cup = this.towerItems.get(position);
            if (!cup.isLidded()) {
                Lid lid = (Lid) TowerItem.getTowerItem(index, false);
                this.removeLid(index);
                position = this.positionOfItem(index, true);
                cup = this.towerItems.get(position);
                if (lid.isInverted()) {
                    try {this.insert(index, false, lid.getType(), position+1);}
                    catch (TowerException e) {
                        this.restoreTower(towerItemsCopy, this.visibleItems);
                        throw new TowerException(TowerException.IMPOSSIBLE_COVER(index));
                    }
                } else if (position == this.numberOfItems() - 1) {
                    try {this.pushItem(index, false, lid.getType());}
                    catch (TowerException e) {
                        this.restoreTower(towerItemsCopy, this.visibleItems);
                        throw new TowerException(TowerException.IMPOSSIBLE_COVER(index));
                    }
                } else {
                    int limit = this.positionOfNextGreaterIndex(index, true);
                    if (limit == -1) limit = this.numberOfItems();
                    int j = position + 1;
                    boolean lidded = false;
                    boolean stop = false;
                    int lastJ = j;
                    while (j <= limit && !stop) {
                        if (j != this.numberOfItems()) {
                            TowerItem itemInPosition = this.towerItems.get(j);
                            if (itemInPosition.isLidded() && !itemInPosition.isCup() && itemInPosition.getIndex() < index) {
                                // lastJ = j;
                                j = this.positionOfItem(itemInPosition.getIndex(), false) + 1;
                                continue;
                            }
                            int itemIndexEnclosing = this.itemEnclosingItem(itemInPosition.getIndex(), itemInPosition.isCup());
                            if (itemIndexEnclosing != -1 && itemIndexEnclosing < index) {
                                // lastJ = j;
                                j = this.positionOfItem(itemIndexEnclosing, false) + 1;
                                continue;
                            }
                        }
                        try {
                            this.insert(index, false, lid.getType(), j);
                            if (cup.isLidded()) { // then this
                                lidded = true;
                                lastJ = j;
                                j++;
                            } else { // finally this
                                //lidded = false;
                                stop = true;
                            }
                            this.remove(index, false);
                        } catch (TowerException e) { // first this (if isVisible)
                            lidded = false;
                            j++;
                        }
                    }
                    if (lidded) this.insert(index, false, lid.getType(), lastJ);
                    else {
                        this.restoreTower(towerItemsCopy, this.visibleItems);
                        throw new TowerException(TowerException.IMPOSSIBLE_COVER(index));
                    }
                }
            }
        } else {
            throw new TowerArgumentException("Cover: To cover the cup, both cup and lid should belong to the tower.");
        }
    }

    public void cover() {
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        boolean isVisible = this.visibleItems;
        this.makeItemsInvisible();
        this.visible = false;

        int j = this.numberOfItems() - 1;
        while (j >= 0) {
            TowerItem item = this.towerItems.get(j);
            if (item.isCup()) {
                TowerItem lid = TowerItem.getTowerItem(item.getIndex(), false);
                if (lid != null) {
                    try {
                        this.coverCup(item.getIndex());
                    } catch (TowerException e) {
                        /* As the tower is invisible, there wont be any TowerExceptions thrown from .coverCup */
                    }
                }
            }
            j--;
        }
        this.visible = isVisible;
        if (!(this.height() > this.height && isVisible)) {
            if (isVisible) this.makeItemsVisible();
            this.ok = true;
        } else {
            this.restoreTower(towerItemsCopy, isVisible);
            this.reportFail("Couldn't cover the cups because it would result in an overflow.");
        }
    }
}