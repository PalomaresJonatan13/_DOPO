package tower;
import shapes.*;

import java.util.*;
import java.awt.Color;

public class Cup extends TowerItem {
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    private Lid lid;
    private static HashMap<Integer, Cup> ActiveCups = new HashMap<>();

    private Cup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.isCup = true;
        this.isLidded = false;
        this.setColor();
        this.createSides();

        this.setHeightReached(-1);
        this.centerX();

        ActiveCups.put(index, this);
    }

    public static Cup getCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = findCup(index);
        if (cup == null) {
            cup = new Cup(index, blockSize, towerMargin, towerWidth, towerHeight);
            Lid.getLid(index, blockSize, towerMargin, towerWidth, towerHeight);
        }
        return cup;
    }

    private void createSides() {
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

    private void setColor() {
        this.color = TowerItem.randomItemColor();
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (Arrays.asList(TowerItem._COLORS).contains(color)) {
                this.color = color;

                this.base.changeColor(this.color);
                this.left.changeColor(this.color);
                this.right.changeColor(this.color);

                this.lid.setColor(color);
            } else {
                throw new IllegalArgumentException("Invalid color");
            }
        }
    }

    public String toString() {
        return "Cup(" + this.index + ")";
    }

    public String[] asArray() {
        return new String[]{"cup", this.index+""};
    }

    public int height() {
        return this.width();
    }

    public Lid getLid() {
        return this.lid;
    }

    public void setLid(Lid lid) {
        if (lid.getIndex() == this.index) {
            this.lid = lid;
        } else {
            throw new IllegalArgumentException("The new lid must have the same index as the cup");
        }
    }

    public boolean updateLiddedState() {
        Lid lid = this.getLid();
        boolean lastState = this.isLidded;

        this.isLidded = this.heightReached + 1 == lid.getHeightReached();
        if (this.isLidded != lastState) lid.updateLiddedState();

        return this.isLidded;
    }

    public static Cup findCup(int index) {
        return ActiveCups.get(index);
    }

    public void deactivate() {
        ActiveCups.remove(this.getIndex());
    }

    /* public void setHeightReached(int heightReached) {
        this.heightReached = heightReached;
        this.moveTo(heightReached - this.height());
        Lid lid = this.getLid();
        if (lid.getHeightReached() != heightReached + 1) { // they will move together
            lid.setHeightReached(heightReached);
        }
    } */

    // this is in terms of pixels
    private void centerX() {
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

    public static void clearActiveCups() {
        ActiveCups.clear();
    }
}