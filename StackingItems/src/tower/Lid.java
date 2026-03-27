package tower;
import shapes.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

public class Lid extends TowerItem {
    private Rectangle base;
    private Circle[] lidShapes;

    private Lid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.isCup = false;
        this.setColor();
        this.createBase();

        this.setHeightReached(-1);
        this.centerX();

        this.activate();
    }

    public static Lid getLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new Lid(index, blockSize, towerMargin, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            lid = (Lid) associatedItems.get("lid");
        }
        return lid;
    }

    public static Lid getLid(int index) {
        Lid lid = null;
        if (activeItems.containsKey(index)) {
            lid = (Lid) activeItems.get(index).get("lid");
        }
        return lid;
    }

    public void activate() {
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

    private void createBase() {
        this.base = new Rectangle(this.width()*this.blockSize, this.blockSize);
        this.base.changeColor(this.color);

        this.lidShapes = new Circle[2];
        this.lidShapes[0] = new Circle(this.blockSize/3);
        this.lidShapes[1] = new Circle(this.blockSize/3);
        this.lidShapes[0].changeColor(Color.WHITE);
        this.lidShapes[1].changeColor(Color.BLACK);
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

    public String toString() {
        return "Lid(" + this.index + ")";
    }

    public String[] asArray() {
        return new String[]{"lid", this.index+""};
    }

    public int height() {
        return 1;
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

            if (this.heightReached == cup.getHeightReached() + 1) {
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

    public void deactivate() {
        if (this.isActive) {
            TowerItem associatedCup = activeItems.get(this.index).get("cup");
            if (associatedCup == null) {
                activeItems.remove(this.index);
            } else {
                activeItems.get(this.index).put("lid", null);
            }
            this.isActive = false;
        }
    }

    // this is in terms of pixels
    private void centerX() {
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

    public void makeVisible() {
        this.base.makeVisible();
        if (this.isLidded) {
            this.lidShapes[0].makeVisible();
            this.lidShapes[1].makeVisible();
        }
        this.isVisible = true;
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.lidShapes[0].makeInvisible();
        this.lidShapes[1].makeInvisible();
        this.isVisible = false;
    }
}