package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;

public class Cup extends TowerItem {
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;

    protected Cup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new Cup(index, blockSize, towerMargin, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            cup = (Cup) associatedItems.get("cup");
        }
        return cup;
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

            this.isLidded = this.heightReached + 1 == lid.getHeightReached();
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
                throw new IllegalArgumentException("Invalid color");
            }
        }
    }

    public void makeVisible() {
        this.base.makeVisible();
        this.left.makeVisible();
        this.right.makeVisible();
        this.isVisible = true;
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
}