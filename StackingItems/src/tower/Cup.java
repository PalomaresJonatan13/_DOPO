package tower;
import shapes.*;

import java.util.*;

import exceptions.TowerException;

import java.awt.Color;

class Cup extends TowerItem {
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    protected String type = NORMAL;

    public static String NORMAL = "normal";
    public static String OPENER = "opener";
    public static String HIERARCHICAL = "hierarchical";
    public static String[] types = {NORMAL, OPENER, HIERARCHICAL};

    protected Cup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, NORMAL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, String type, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            if (type == NORMAL) cup = new Cup(index, blockSize, towerMargin, towerWidth, towerHeight);
            else if (type == OPENER) cup = new OpenerCup(index, blockSize, towerMargin, towerWidth, towerHeight);
            else if (type == HIERARCHICAL) cup = new HierarchicalCup(index, blockSize, towerMargin, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            cup = (Cup) associatedItems.get("cup");
        }
        return cup;
    }

    public static Cup getCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        return Cup.getCup(index, NORMAL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Cup getCup(int index) {
        Cup cup = null;
        if (activeItems.containsKey(index)) {
            cup = (Cup) activeItems.get(index).get("cup");
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public void enable() {
        if (!this.isActive) {
            if (activeItems.containsKey(this.index)) {
                activeItems.get(index).put("cup", this);
            } else {
                HashMap<String, TowerItem> items = new HashMap<>();
                items.put("cup", this);
                items.put("lid", null);
                activeItems.put(this.index, items);
            }
            this.isActive = true;
        }
    }

    public void disable() {
        if (this.isActive) {
            TowerItem associatedLid = activeItems.get(this.index).get("lid");
            if (associatedLid == null) {
                activeItems.remove(this.index);
            } else {
                activeItems.get(this.index).put("cup", null);
            }
            this.makeInvisible();
            this.isActive = false;
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public boolean isCup() {
        return true;
    }

    public boolean isRemovable() {
        return true;
    }

    public int height() {
        return this.width();
    }

    public String toString() {
        return "Cup(" + this.index + ")";
    }

    public String[] asArray() {
        return new String[]{"cup", this.index+""};
    }

    public Lid lid() {
        Lid lid = null;
        if (this.isActive) {
            lid = (Lid) activeItems.get(this.index).get("lid");
        }
        return lid;
    }

    public boolean updateLiddedState() {
        Lid lid = this.lid();
        if (lid != null) {
            boolean lastState = this.isLidded;

            this.isLidded = lid.getHeightReached() == this.heightReached + 
                (lid.isInverted() ? - this.height() : 1);
            if (this.isLidded != lastState) lid.updateLiddedState();
        } else this.isLidded = false;

        return this.isLidded;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (Arrays.asList(TowerItem._COLORS).contains(color)) {
                this.color = color;

                this.base.changeColor(this.color);
                this.left.changeColor(this.color);
                this.right.changeColor(this.color);

                Lid lid = this.lid();
                if (lid != null) lid.setColor(color);
            } else {
                throw new IllegalArgumentException("Invalid color.");
            }
        }
    }

    public void makeVisible() {
        if (this.isActive) {
            this.base.makeVisible();
            this.left.makeVisible();
            this.right.makeVisible();
            this.isVisible = true;
        } else {
            throw new IllegalStateException("Cannot make the cup visible if it is not active.");
        }
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.left.makeInvisible();
        this.right.makeInvisible();
        this.isVisible = false;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void createSides() {
        // the sides will overlap, but it wont matter
        int sideLengthPx = this.width()*this.blockSize;
        this.base = new Rectangle(sideLengthPx, this.blockSize);
        this.left = new Rectangle(this.blockSize, sideLengthPx);
        this.right = new Rectangle(this.blockSize, sideLengthPx);

        this.base.changeColor(this.color);
        this.left.changeColor(this.color);
        this.right.changeColor(this.color);

        this.base.moveVerticallyTo(sideLengthPx - this.blockSize);
        this.right.moveHorizontallyTo(sideLengthPx - this.blockSize);
    }

    // this is in terms of pixels
    protected void centerX() {
        int newX = this.towerMargin + (this.towerWidth - this.width())*this.blockSize/2;
        this.base.moveHorizontallyTo(newX);
        this.left.moveHorizontallyTo(newX);
        this.right.moveHorizontallyTo(newX + (this.width() - 1)*this.blockSize);
    }

    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y + (this.height() - 1)*this.blockSize);
        this.left.moveVerticallyTo(y);
        this.right.moveVerticallyTo(y);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public static String[] getTypes() {
        return types;
    }

    public static boolean isAValidType(String type) {
        return Arrays.asList(types).contains(type);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    private static class OpenerCup extends Cup {
        protected OpenerCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
            super(index, blockSize, towerMargin, towerWidth, towerHeight);
            this.type = Cup.OPENER;
        }

        public String getType() {
            return this.type;
        }

        public TowerItem onPush(Tower tower) {
            tower.popCup();
            // this.disable();
            tower.pushCup(this.index);
            TowerItem placeholder = Cup.getCup(this.index);

            String[][] items = tower.stackingItems();
            int j = items.length - 1;
            boolean lidsToRemove = true;
            while (j >= 0 && lidsToRemove) {
                TowerItem item = TowerItem.fromArray(items[j]);
                if (
                    !item.isCup() &&
                    item.getHeightReached() == placeholder.getHeightReached() - placeholder.height() &&
                    item.isRemovable()
                ) {
                    tower.pop();
                } else {
                    lidsToRemove = false;
                }
                j--;
            }
            return placeholder;        
        }
    }

    private static class HierarchicalCup extends Cup {
        protected HierarchicalCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
            super(index, blockSize, towerMargin, towerWidth, towerHeight);
            this.type = Cup.HIERARCHICAL;
        }

        public String getType() {
            return this.type;
        }

        public boolean isRemovable() {
            return this.isRemovable;
        }

        public TowerItem onPush(Tower tower) throws TowerException {
            tower.popCup();
            // this.disable();

            String[][] items = tower.stackingItems();
            int j = items.length - 1;
            boolean foundGreaterIndex = false;
            while (j >= 0 && !foundGreaterIndex) {
                TowerItem item = TowerItem.fromArray(items[j]);
                foundGreaterIndex = item.getIndex() > this.index;
                j--;
            }
            
            tower.insert(this.index, true, NORMAL, ++j);
            if (j == 0) this.isRemovable = false;
            return Cup.getCup(this.index);
        }
    }

}