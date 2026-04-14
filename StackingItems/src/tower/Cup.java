package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;

abstract class Cup extends TowerItem {
    protected Rectangle base;
    protected Rectangle left;
    protected Rectangle right;

    public static String NORMAL = "normal";
    public static String OPENER = "opener";
    public static String HIERARCHICAL = "hierarchical";
    public static String COVERED = "covered";
    public static String[] types = {NORMAL, OPENER, HIERARCHICAL, COVERED};

    protected Cup(int index, String type, int towerWidth, int towerHeight) {
        super(index, type, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, String type, int towerWidth, int towerHeight) {
        if (type.equals(NORMAL)) return NormalCup.getCup(index, towerWidth, towerHeight);
        else if (type.equals(OPENER)) return OpenerCup.getCup(index, towerWidth, towerHeight);
        else if (type.equals(HIERARCHICAL)) return HierarchicalCup.getCup(index, towerWidth, towerHeight);
        else if (type.equals(COVERED)) return CoveredCup.getCup(index, towerWidth, towerHeight);
        else throw new IllegalArgumentException("The type given is not valid.");
    }

    public static Cup getCup(int index, int towerWidth, int towerHeight) {
        return getCup(index, NORMAL, towerWidth, towerHeight);
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

    @Override
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

    @Override
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

    @Override
    public boolean isCup() {
        return true;
    }

    @Override
    public int height() {
        return this.width();
    }

    @Override
    public String toString() {
        return "Cup(" + this.index + ", " + this.type + ")";
    }

    @Override
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

    @Override
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

    @Override
    public void setColor(Color color) {
        if (this.color != color) {
            if (TowerItem.isAValidItemColor(color)) {
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

    @Override
    public void makeVisible() {
        if (this.isActive) {
            this.base.makeVisible();
            this.left.makeVisible();
            this.right.makeVisible();
            this.makeExtraShapesVisible();
            this.visible = true;
        } else {
            throw new IllegalStateException("Cannot make the cup visible if it is not active.");
        }
    }

    @Override
    public void makeInvisible() {
        this.base.makeInvisible();
        this.left.makeInvisible();
        this.right.makeInvisible();
        this.makeExtraShapesInvisible();
        this.visible = false;
    }

    @Override
    public boolean isVisible() {
        return (
            this.base.isVisible() &&
            this.left.isVisible() &&
            this.right.isVisible()
        );
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createSides() {
        // the sides will overlap, but it wont matter
        int sideLengthPx = this.width()*BLOCKSIZE;
        this.base = new Rectangle(sideLengthPx, BLOCKSIZE);
        this.left = new Rectangle(BLOCKSIZE, sideLengthPx);
        this.right = new Rectangle(BLOCKSIZE, sideLengthPx);

        this.base.changeColor(this.color);
        this.left.changeColor(this.color);
        this.right.changeColor(this.color);

        /* this.base.moveVerticallyTo(sideLengthPx - BLOCKSIZE);
        this.right.moveHorizontallyTo(sideLengthPx - BLOCKSIZE); */
    }

    // this is in terms of pixels
    @Override
    protected void centerX() {
        int newX = TOWER_MARGIN + (this.towerWidth - this.width())*BLOCKSIZE/2;
        this.base.moveHorizontallyTo(newX);
        this.left.moveHorizontallyTo(newX);
        this.right.moveHorizontallyTo(newX + (this.width() - 1)*BLOCKSIZE);
        this.centerExtraShapesX();
    }

    @Override
    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y + (this.height() - 1)*BLOCKSIZE);
        this.left.moveVerticallyTo(y);
        this.right.moveVerticallyTo(y);
        this.moveExtraShapesVertically();
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
}