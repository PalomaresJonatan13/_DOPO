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
        if (this.visibleItems) {
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
        boolean visible = this.frame.isVisible();
        for (TowerItem item : this.towerItems) {
            visible = visible && item.isVisible();
        }
        return visible;
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

    private int positionOfItem(TowerItem item) {
        return this.positionOfItem(item.getIndex(), item.isCup());
    }

    private boolean hasItem(int index, boolean isCup) {
        return this.positionOfItem(index, isCup) >= 0;
    }

    private boolean hasItem(TowerItem item) {
        return this.hasItem(item.getIndex(), item.isCup());
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

    private int positionOfNextGreaterIndex(TowerItem item) {
        return this.positionOfNextGreaterIndex(item.getIndex(), item.isCup());
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

    private int itemEnclosingItem(TowerItem item) {
        return this.itemEnclosingItem(item.getIndex(), item.isCup());
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

    private int heightReachedByNewItem(TowerItem item) {
        return this.heightReachedByNewItem(item.getIndex(), item.isCup());
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

    void remove(int index, boolean isCup) {
        try {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            boolean isVisible = this.visibleItems;
            this.makeItemsInvisible();
            int position = this.positionOfItem(index, isCup);
            this.removeFromPosition(position);
            for (int j=position+1; j<towerItemsCopy.size(); j++) {
                TowerItem item = towerItemsCopy.get(j);
                this.uncheckedPush(item);
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
            for (int j=0; j<copy.size(); j++) {
                TowerItem item = copy.get(j);
                this.pushItem(item);
                if (item.getType().equals(Cup.COVERED)) j++;
            }
        } catch (TowerException e) {
            throw new TowerArgumentException("Restore: The given list of TowerItems is not a valid copy (cannot push all the items in the copy given because there would be an overflow).");
        }
        if (isVisible) this.makeItemsVisible();
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private TowerItem prepareNewItem(TowerItem newItem) throws TowerException {
        newItem.enable();
        this.towerItems.add(newItem);
        TowerItem placeholder = newItem.onPush(this);

        if (placeholder != null) {
            if (placeholder.getHeightReached() == 0) {
                this.towerItems.removeLast();
                int newHeight = this.heightReachedByNewItem(newItem);
                placeholder.setHeightReached(newHeight);
                this.towerItems.add(placeholder);
            }

            newItem.setHeightReached(placeholder.getHeightReached());
            placeholder.disable();
        }
        return placeholder;
    }

    private void uncheckedPush(int index, boolean isCup, String type) {
        if (isCup) {
            this.pushCup(index, type);
        } else {
            this.pushLid(index, type);
        }
    }

    private void uncheckedPush(TowerItem item) {
        this.uncheckedPush(item.getIndex(), item.isCup(), item.getType());
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
        TowerItem placeholder = null;
        boolean placeholderCreated = true;
        try {placeholder = this.prepareNewItem(newItem);}
        catch (TowerException e) {placeholderCreated = false;}
        this.visibleItems = oldVisibility;
        
        if (!(placeholderCreated && placeholder == null)) {
            if (!(this.height() > this.height && this.visible) && placeholderCreated) {
                HashMap<Integer, Color> colors = this.itemColors;
                if (colors.get(index) == null) colors.put(index, placeholder.getColor());
                newItem.setColor(colors.get(index));
                
                this.towerItems.set(this.towerItems.indexOf(placeholder), newItem);
                newItem.enable();
                newItem.updateLiddedState();
                if (this.visibleItems) this.makeItemsVisible();
                this.ok = true;
            } else {
                this.visibleItems = oldVisibility;
                this.restoreTower(towerItemsCopy, this.visibleItems);
                this.ok = false;
                throw new TowerException(TowerException.overflow(index, isCup, type));
            }
        }
    }

    private void pushItem(TowerItem item) throws TowerException {
        this.pushItem(item.getIndex(), item.isCup(), item.getType());
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
                        this.uncheckedPush(lastItem);
                    }
                }
                if (isVisible) this.makeItemsVisible();
                this.ok = true;
            } else {
                throw new TowerException(TowerException.removingNotRemovableItem(item.getIndex(), item.isCup(), item.getType()));
            }
        } else {
            throw new TowerArgumentException("Remove: The desired item to be removed does not belong to the Tower.");
        }
    }

    private void removeItem(TowerItem item) throws TowerException {
        this.removeItem(item.getIndex(), item.isCup());
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
            this.removeItem(itemToRemove);
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
                this.uncheckedPush(item);
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
            this.uncheckedPush(nextItem);
        }
        while (this.height() > this.height) {
            this.pop();
        }

        if (isVisible) this.makeItemsVisible();
        this.ok = true;
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
                    if (!this.hasItem(itemToReinsert)) this.pushItem(itemToReinsert);
                    j++;
                }
                if (isVisible) this.makeItemsVisible();
            } catch (TowerException e) {
                this.restoreTower(towerItemsCopy, isVisible);
                throw new TowerException(TowerException.overflow(index, isCup, type));
            }
        } else {
            throw new TowerArgumentException("Insert: The given position to insert should be greater than or equal to 0 and less than or equal to the number of items in the Tower.");
        }
    }

    void insert(TowerItem item, int position) throws TowerException {
        this.insert(item.getIndex(), item.isCup(), item.getType(), position);
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
                    this.pushItem(item2);
                    int j = position1;
                    while (++j < position2) {
                        TowerItem item = towerItemsCopy.get(j);
                        if (!this.hasItem(item)) this.pushItem(item);
                    }
                    if (!this.hasItem(item1)) this.pushItem(item1);
                    while (++j < towerItemsCopy.size()) {
                        TowerItem item = towerItemsCopy.get(j);
                        if (!this.hasItem(item)) this.pushItem(item);
                    }
                    if (isVisible) this.makeItemsVisible();
                } catch (TowerException e) {
                    this.restoreTower(towerItemsCopy, isVisible);
                    throw new TowerException(TowerException.impossibleSwap(index1, isCup1, item1.getType(), index2, isCup2, item2.getType()));
                }
            } else {this.ok = true;}
        } else {
            throw new TowerArgumentException("Swap: The given items to swap do not both belong to the Tower.");
        }
    }

    private void swapItems(TowerItem item1, TowerItem item2) throws TowerException {
        this.swapItems(item1.getIndex(), item1.isCup(), item2.getIndex(), item2.isCup());
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
                    this.swapItems(firstItem, secondItem);
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

    private boolean isCupTemporarilyCovered(int index, String type, int position) {
        int cupPosition = this.positionOfItem(index, true);
        if (0 <= position && position <= this.numberOfItems()) {
            if (this.hasItem(index, true) && !this.hasItem(index, false) && position > cupPosition) {
                if (TowerItem.isAValidType(false, type)) {
                    List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
                    if (position != this.numberOfItems()) this.removeFromPosition(position);
                    this.uncheckedPush(index, false, type);

                    TowerItem cup = TowerItem.getTowerItem(index, true);
                    boolean covered = cup.isLidded();
                    this.restoreTower(towerItemsCopy, this.visibleItems);
                    return covered;
                } else {
                    throw new TowerArgumentException("Cover: The type given is not valid.");
                }
            } else {
                throw new TowerArgumentException("Cover: To check if the cup is temporarily covered by a lid during the insertion of the lid, the cup must belong to the tower, while the lid must not, and the position for the insertion must be greater than the position of the cup in the tower.");
            }
        } else {
            throw new TowerArgumentException("Cover: The given position to insert the lid at is not valid, it should be between 0 and the total number of items in the tower.");
        }
    }

    private int positionToCoverCup(TowerItem lid) {
        int index = lid.getIndex();
        List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
        if (this.hasItem(index, true) && !this.hasItem(lid)) {
            TowerItem cup = TowerItem.getTowerItem(index, true);
            int limit = this.positionOfNextGreaterIndex(cup);
            if (limit == -1) limit = this.numberOfItems();
            int j = this.positionOfItem(cup) + 1;
            int lastJ = j;
            boolean lidded = false, stop = false, temporarilyLidded = false;
            while (j <= limit && !stop) {
                if (j != this.numberOfItems()) {
                    TowerItem itemInPosition = this.towerItems.get(j);
                    int itemIndex = itemInPosition.getIndex();
                    boolean isCup = itemInPosition.isCup();
                    int itemIndexEnclosing = this.itemEnclosingItem(itemInPosition);
                    if (itemInPosition.isLidded() && !isCup && itemIndex < index) {
                        j = this.positionOfItem(itemInPosition) + 1;
                        continue;
                    }
                    if (itemIndexEnclosing != -1 && itemIndexEnclosing < index) {
                        j = this.positionOfItem(itemIndexEnclosing, false) + 1;
                        continue;
                    }
                }
                try {
                    temporarilyLidded = this.isCupTemporarilyCovered(index, lid.getType(), j);
                    this.insert(lid, j);
                    cup = TowerItem.getTowerItem(index, true);
                    if (cup.isLidded()) { // then this
                        lidded = true;
                        lastJ = j;
                        j++;
                    } else stop = true; // finally this
                    this.restoreTower(towerItemsCopy, this.visibleItems);
                } catch (TowerException e) { // first this (if isVisible)
                    lidded = false;
                    j++;
                }
            }
            return lidded ? lastJ : (temporarilyLidded ? -2 : -1); // -1;
        } else {
            throw new TowerArgumentException("Cover: To find the position where the lid must be inserted to cover the cup, the cup must belong to the tower, while the lid must not.");
        }
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
                try {
                    if (lid.isInverted()) this.insert(lid, position+1);
                    else if (position == this.numberOfItems()-1) this.pushItem(lid);
                    else {
                        int positionToCover = this.positionToCoverCup(lid);
                        if (positionToCover >= 0) {
                            this.insert(lid, positionToCover);
                        }
                        else if (positionToCover == -1) throw new TowerException(""); /* it is handled in the catch below */
                    }
                } catch (TowerException e) {
                    this.restoreTower(towerItemsCopy, this.visibleItems);
                    throw new TowerException(TowerException.impossibleCover(index));
                }
            }
        } else {
            throw new TowerArgumentException("Cover: To cover the cup, both cup and lid should belong to the tower.");
        }
    }

    private void coverFromPosition(int position) throws TowerException {
        if (0 <= position && position < this.numberOfItems()) {
            List<TowerItem> towerItemsCopy = new ArrayList<>(this.towerItems);
            boolean isVisible = this.visibleItems;
            this.makeItemsInvisible();
            this.visible = false;

            int j = this.numberOfItems() - 1;
            while (j >= position) {
                TowerItem item = this.towerItems.get(j);
                if (item.isCup()) {
                    int itemIndex = item.getIndex();
                    TowerItem lid = TowerItem.getTowerItem(itemIndex, false);
                    if (lid != null) {
                        this.coverCup(itemIndex);
                        int itemPosition = this.positionOfItem(item);
                        if (itemPosition + 1 < this.numberOfItems()) this.coverFromPosition(itemPosition + 1);
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
                throw new TowerException("Couldn't cover the cups because it would result in an overflow.");
            }
        } else {
            throw new TowerArgumentException("Cover: The position given from which to start covering is not valid, it should be between 0 and the total number of items (exclusive) in the tower.");
        }
    }

    public void cover() {
        if (!this.isEmpty()) {
            try {
                this.coverFromPosition(0);
            } catch (TowerException e) {
                this.reportFail(e.getMessage());
            }
        }
    }
}