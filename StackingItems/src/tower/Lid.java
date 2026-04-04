package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;

abstract class Lid extends TowerItem {
    protected Rectangle base;
    protected Circle[] lidShapes;
    protected boolean isInverted = false;

    public static String NORMAL = "normal";
    public static String FEARFUL = "fearful";
    public static String CRAZY = "crazy";
    public static String[] types = {NORMAL, FEARFUL, CRAZY};

    protected Lid(int index, String type, int towerWidth, int towerHeight) {
        super(index, type, towerWidth, towerHeight);
    }

    public static Lid getLid(int index, String type, int towerWidth, int towerHeight) {
        if (type == NORMAL) return NormalLid.getLid(index, towerWidth, towerHeight);
        else if (type == FEARFUL) return FearfulLid.getLid(index, towerWidth, towerHeight);
        else if (type == CRAZY) return CrazyLid.getLid(index, towerWidth, towerHeight);
        else throw new IllegalArgumentException("The type given is not valid.");
    }

    public static Lid getLid(int index, int towerWidth, int towerHeight) {
        return getLid(index, NORMAL, towerWidth, towerHeight);
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
        this.base = new Rectangle(this.width()*BLOCKSIZE, BLOCKSIZE);
        this.base.changeColor(this.color);

        this.lidShapes = new Circle[2];
        this.lidShapes[0] = new Circle(BLOCKSIZE/3);
        this.lidShapes[1] = new Circle(BLOCKSIZE/3);
        this.lidShapes[0].changeColor(Color.WHITE);
        this.lidShapes[1].changeColor(Color.BLACK);
    }

    // this is in terms of pixels
    protected void centerX() {
        int newX = TOWER_MARGIN + (this.towerWidth - this.width())*BLOCKSIZE/2;
        this.base.moveHorizontallyTo(newX);
        this.lidShapes[0].moveHorizontallyTo(newX + BLOCKSIZE/2);
        this.lidShapes[1].moveHorizontallyTo(newX + 5*BLOCKSIZE/6);
    }

    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
        this.lidShapes[0].moveVerticallyTo(y + BLOCKSIZE/2);
        this.lidShapes[1].moveVerticallyTo(y + BLOCKSIZE/2);
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