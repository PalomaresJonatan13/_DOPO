package tower;
import shapes.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

import exceptions.TowerException;

class Lid extends TowerItem {
    protected Rectangle base;
    protected Circle[] lidShapes;
    protected String type = NORMAL;
    protected boolean isInverted = false;

    public static String NORMAL = "normal";
    public static String FEARFUL = "fearful";
    public static String CRAZY = "crazy";
    private static String[] types = {NORMAL, FEARFUL, CRAZY};

    protected Lid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, NORMAL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Lid getLid(int index, String type, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            if (type == NORMAL) lid = new Lid(index, blockSize, towerMargin, towerWidth, towerHeight);
            else if (type == FEARFUL) lid = new FearfulLid(index, blockSize, towerMargin, towerWidth, towerHeight);
            else if (type == CRAZY) lid = new CrazyLid(index, blockSize, towerMargin, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            lid = (Lid) associatedItems.get("lid");
        }
        return lid;
    }

    public static Lid getLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        return Lid.getLid(index, NORMAL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Lid getLid(int index) {
        Lid lid = null;
        if (activeItems.containsKey(index)) {
            lid = (Lid) activeItems.get(index).get("lid");
        }
        return lid;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public void enable() {
        if (!this.isActive) {
            if (activeItems.containsKey(this.index)) {
                activeItems.get(index).put("lid", this);
            } else {
                HashMap<String, TowerItem> items = new HashMap<>();
                items.put("cup", null);
                items.put("lid", this);
                activeItems.put(this.index, items);
            }
            this.isActive = true;
        }
    }

    public void disable() {
        if (this.isActive) {
            TowerItem associatedCup = activeItems.get(this.index).get("cup");
            if (associatedCup == null) {
                activeItems.remove(this.index);
            } else {
                activeItems.get(this.index).put("lid", null);
            }
            this.makeInvisible();
            this.isActive = false;
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public boolean isCup() {
        return false;
    }

    public boolean isInverted() {
        return this.isInverted;
    }

    public int height() {
        return 1;
    }
    
    public String toString() {
        return "Lid(" + this.index + ")";
    }

    public String[] asArray() {
        return new String[]{"lid", this.index+""};
    }

    public Cup cup() {
        Cup cup = null;
        if (this.isActive) {
            cup = (Cup) activeItems.get(this.index).get("cup");
        }
        return cup;
    }

    public boolean updateLiddedState() {
        Cup cup = this.cup();
        if (cup != null) {
            boolean lastState = this.isLidded;

            if (this.heightReached == cup.getHeightReached() +
                    (this.isInverted ? - cup.height() : 1)) {
                this.isLidded = true;
                if (this.isVisible) {
                    this.lidShapes[0].makeVisible();
                    this.lidShapes[1].makeVisible();
                }
            } else {
                this.isLidded = false;
                this.lidShapes[0].makeInvisible();
                this.lidShapes[1].makeInvisible();
            }
            if (this.isLidded != lastState) cup.updateLiddedState();
        } else this.isLidded = false;

        return this.isLidded;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (Arrays.asList(TowerItem._COLORS).contains(color)) {
                this.color = color;

                this.base.changeColor(color);

                Cup cup = this.cup();
                if (cup != null) cup.setColor(color);
            } else {
                throw new IllegalArgumentException("Invalid color");
            }
        }
    }

    public void makeVisible() {
        if (this.isActive) {
            this.base.makeVisible();
            if (this.isLidded) {
                this.lidShapes[0].makeVisible();
                this.lidShapes[1].makeVisible();
            }
            this.isVisible = true;
        } else {
            throw new IllegalStateException("Cannot make the lid visible if it is not active.");
        }
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.lidShapes[0].makeInvisible();
        this.lidShapes[1].makeInvisible();
        this.isVisible = false;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void createSides() {
        this.base = new Rectangle(this.width()*this.blockSize, this.blockSize);
        this.base.changeColor(this.color);

        this.lidShapes = new Circle[2];
        this.lidShapes[0] = new Circle(this.blockSize/3);
        this.lidShapes[1] = new Circle(this.blockSize/3);
        this.lidShapes[0].changeColor(Color.WHITE);
        this.lidShapes[1].changeColor(Color.BLACK);
    }

    // this is in terms of pixels
    protected void centerX() {
        int newX = this.towerMargin + (this.towerWidth - this.width())*this.blockSize/2;
        this.base.moveHorizontallyTo(newX);
        this.lidShapes[0].moveHorizontallyTo(newX + this.blockSize/2);
        this.lidShapes[1].moveHorizontallyTo(newX + 5*this.blockSize/6);
    }

    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
        this.lidShapes[0].moveVerticallyTo(y + this.blockSize/2);
        this.lidShapes[1].moveVerticallyTo(y + this.blockSize/2);
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

    private static class FearfulLid extends Lid {
        protected FearfulLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
            super(index, blockSize, towerMargin, towerWidth, towerHeight);
            this.type = Lid.FEARFUL;
        }

        public String getType() {
            return this.type;
        }

        public boolean isRemovable() {
            return this.isRemovable;
        }

        public TowerItem onPush(Tower tower) throws TowerException {
            TowerItem placeholder = null;
            HashMap<String, TowerItem> items = activeItems.get(this.index);
            if (items != null && items.get("cup") != null) {
                placeholder = this;
            } else {
                tower.pop();
                // this.disable();
                throw new TowerException(TowerException.INVALID_PUSH_FEARFUL(index));
            }
            return placeholder;
        }

        public boolean updateLiddedState() { // called during pushItem
            super.updateLiddedState();
            this.isRemovable = !this.isLidded;
            return this.isLidded;
        }
    }

    private static class CrazyLid extends Lid {
        protected CrazyLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
            super(index, blockSize, towerMargin, towerWidth, towerHeight);
            this.type = Lid.CRAZY;
            this.isInverted = true;
        }

        public String getType() {
            return this.type;
        }
        
        public boolean isInverted() {
            return this.isInverted;
        }

        public TowerItem onPush(Tower tower) throws TowerException {
            tower.pop();
            // this.disable();

            tower.pushLid(this.index);
            // tower.makeVisible(); ///////
            String[][] items = tower.stackingItems();
            int j = items.length - 2;
            int itemIndex = 0;
            while (j >= 0 && itemIndex < this.index) {
                TowerItem item = TowerItem.fromArray(items[j]);
                itemIndex = item.getIndex();
                if (itemIndex == this.index && item.updateLiddedState()) {
                    tower.removeLid(this.index);
                    tower.insert(this.index, true, item.getType(), j);
                    // tower.makeVisible(); ///////
                    tower.insert(this.index, false, this.type, j);
                    // tower.makeVisible(); ///////
                    j = 0;
                }
                j--;
            }
            // tower.makeVisible(); ///////
            TowerItem placeholder = Lid.getLid(this.index);
            if (placeholder == null) {
                tower.pushLid(this.index);
                placeholder = Lid.getLid(this.index);
            }
            return placeholder;
        }
    }

}