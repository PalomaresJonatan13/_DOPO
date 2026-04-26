package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;

abstract class Lid extends TowerItem {
    protected Rectangle base;
    protected Shape[] shapesLidded;
    protected boolean isInverted = false;

    public static String NORMAL = "normal";
    public static String FEARFUL = "fearful";
    public static String CRAZY = "crazy";
    public static String[] types = {NORMAL, FEARFUL, CRAZY};

    protected Lid(int index, String type, int towerWidth, int towerHeight) {
        super(index, type, towerWidth, towerHeight);
    }

    public static Lid getLid(int index, String type, int towerWidth, int towerHeight) {
        if (type.equals(NORMAL)) return NormalLid.getLid(index, towerWidth, towerHeight);
        else if (type.equals(FEARFUL)) return FearfulLid.getLid(index, towerWidth, towerHeight);
        else if (type.equals(CRAZY)) return CrazyLid.getLid(index, towerWidth, towerHeight);
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

    @Override
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

    @Override
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

    @Override
    public boolean isCup() {
        return false;
    }
    
    public boolean isInverted() {
        return this.isInverted;
    }

    @Override
    public int height() {
        return 1;
    }

    @Override
    public String toString() {
        return "Lid(" + this.index + ", " + this.type + ")";
    }

    @Override
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

    @Override
    public boolean updateLiddedState() {
        Cup cup = this.cup();
        if (cup != null) {
            boolean lastState = this.isLidded;

            if (this.heightReached == cup.getHeightReached() +
                    (this.isInverted ? - cup.height() : 1)) {
                this.isLidded = true;
                if (this.visible) {
                    this.shapesLidded[0].makeVisible();
                    this.shapesLidded[1].makeVisible();
                }
            } else {
                this.isLidded = false;
                this.shapesLidded[0].makeInvisible();
                this.shapesLidded[1].makeInvisible();
            }
            if (this.isLidded != lastState) cup.updateLiddedState();
        } else this.isLidded = false;

        return this.isLidded;
    }

    @Override
    public void setColor(Color color) {
        if (this.color != color) {
            if (TowerItem.isAValidItemColor(color)) {
                this.color = color;

                this.base.changeColor(color);

                Cup cup = this.cup();
                if (cup != null) cup.setColor(color);
            } else {
                throw new IllegalArgumentException("Invalid color");
            }
        }
    }

    @Override
    public void makeVisible() {
        if (this.isActive) {
            this.base.makeVisible();
            this.makeExtraShapesVisible();
            if (this.isLidded) {
                this.shapesLidded[0].makeVisible();
                this.shapesLidded[1].makeVisible();
            }
            this.visible = true;
        } else {
            throw new IllegalStateException("Cannot make the lid visible if it is not active.");
        }
    }

    @Override
    public void makeInvisible() {
        this.base.makeInvisible();
        this.makeExtraShapesInvisible();
        this.shapesLidded[0].makeInvisible();
        this.shapesLidded[1].makeInvisible();
        this.visible = false;
    }

    @Override
    public boolean isVisible() {
        return (
            this.base.isVisible()
        );
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createSides() {
        this.base = new Rectangle(this.width()*BLOCKSIZE, BLOCKSIZE);
        this.base.changeColor(this.color);

        this.shapesLidded = new Circle[2];
        this.shapesLidded[0] = new Circle(BLOCKSIZE/3);
        this.shapesLidded[1] = new Circle(BLOCKSIZE/3);
        this.shapesLidded[0].changeColor(Color.WHITE);
        this.shapesLidded[1].changeColor(Color.BLACK);
    }

    // this is in terms of pixels
    @Override
    protected void centerX() {
        int newX = TOWER_MARGIN + (this.towerWidth - this.width())*BLOCKSIZE/2;
        this.base.moveHorizontallyTo(newX);
        this.centerExtraShapesX();

        int centerShape0 = (int) (newX + BLOCKSIZE * (5f/12));
        this.shapesLidded[0].moveHorizontallyTo(centerShape0);
        int centerShape1 = (int) (newX + BLOCKSIZE * (7f/12));
        this.shapesLidded[1].moveHorizontallyTo(centerShape1);
    }

    @Override
    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
        this.shapesLidded[0].moveVerticallyTo(y + BLOCKSIZE/2);
        this.shapesLidded[1].moveVerticallyTo(y + BLOCKSIZE/2);
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