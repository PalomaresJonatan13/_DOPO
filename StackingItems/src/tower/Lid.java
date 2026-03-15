package tower;
import shapes.*;

import java.awt.Color;

public class Lid extends TowerItem {
    private Rectangle base;
    private Circle[] lidShapes;

    private Lid(int index, Color color, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.color = color;
        this.isCup = false;
        this.createBase();

        this.setHeightReached(-1);
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

        this.lidShapes = new Circle[2];
        this.lidShapes[0] = new Circle(this.blockSize/3);
        this.lidShapes[1] = new Circle(this.blockSize/3);
        this.lidShapes[0].changeColor(Color.WHITE);
        this.lidShapes[1].changeColor(Color.BLACK);
    }

    public void setColor(Color color) {
        Cup cup = this.getCup();
        if (cup.getColor() != color) {
            cup.setColor(color);
        }
        this.base.changeColor(color);
        this.lidShapes[0].changeColor(Color.WHITE);
        this.lidShapes[1].changeColor(Color.BLACK);
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