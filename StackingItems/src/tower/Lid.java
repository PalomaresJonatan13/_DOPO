package tower;
import shapes.*;

import java.awt.Color;

public class Lid extends TowerItem {
    private Rectangle base;
    private Circle liddedDistinctive;

    private Lid(int index, Color color, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.color = color;
        this.isCup = false;
        this.isVisible = false;
        this.isLidded = false;
        this.createBase();

        this.setHeightReached(this.height());
        this.centerX();
    }

    public static Lid getLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = Cup.getCup(index, blockSize, towerMargin, towerWidth, towerHeight);
        if (cup.getLid() == null) {
            cup.setLid(new Lid(cup.index, cup.color, cup.blockSize, cup.towerMargin, cup.towerWidth, cup.towerHeight));
        }
        return cup.getLid();
    }

    private void createBase() {
        this.base = new Rectangle(this.width()*this.blockSize, this.blockSize);
        this.base.changeColor(this.color);

        this.liddedDistinctive = new Circle(this.blockSize/2);
        this.liddedDistinctive.changeColor(this.color);
    }

    public void setColor(Color color) {
        Cup cup = this.getCup();
        if (cup.getColor() != color) {
            cup.setColor(color);
        }
        this.base.changeColor(color);
        this.liddedDistinctive.changeColor(color);
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

    public Cup getCup() {
        return Cup.findCup(this.index);
    }

    public boolean updateLiddedState() {
        Cup cup = this.getCup();
        boolean lastState = this.isLidded;

        if (this.heightReached == cup.getHeightReached() + 1) {
            this.isLidded = true;
            if (this.isVisible) this.liddedDistinctive.makeVisible();
        } else {
            this.isLidded = false;
            this.liddedDistinctive.makeInvisible();
        }
        if (this.isLidded != lastState) cup.updateLiddedState();

        return this.isLidded;
    }

    public void deactivate() {
        this.getCup().deactivate();
    }

    /* public void setHeightReached(int heightReached) {
        this.heightReached = heightReached;
        this.moveTo(heightReached - this.height());
        Cup cup = this.getCup();
        if (cup.getHeightReached() != heightReached - 1) { // they will move together
            cup.setHeightReached(heightReached);
        }
    } */

    // this is in terms of pixels
    private void centerX() {
        int newX = this.towerMargin + (this.towerWidth - this.width())*this.blockSize/2;
        this.base.moveHorizontallyTo(newX);
        this.liddedDistinctive.moveHorizontallyTo(newX);
    }

    protected void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
        this.liddedDistinctive.moveVerticallyTo(y);
    }

    public void makeVisible() {
        this.base.makeVisible();
        if (this.isLidded) this.liddedDistinctive.makeVisible();
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.liddedDistinctive.makeInvisible();
    }
}