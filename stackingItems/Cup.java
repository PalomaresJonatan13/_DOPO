
/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------
import java.util.*;

public class Cup {
    private int index;
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    private String color;
    private Lid lid;
    private int blockSize;

    public Cup(int index, String color, int blockSize) {
        this.index = index;
        this.color = color;
        this.blockSize = blockSize;
        this.lid = new Lid(index, color, blockSize);
        this.createSides();
    }

    private void createSides() {
        // the sides will overlap, but it wont matter
        int sideLengthPx = this.size()*this.blockSize;
        this.base = new Rectangle(sideLengthPx, this.blockSize);
        this.left = new Rectangle(this.blockSize, sideLengthPx);
        this.right = new Rectangle(this.blockSize, sideLengthPx);

        this.base.changeColor(this.color);
        this.left.changeColor(this.color);
        this.right.changeColor(this.color);

        this.base.moveVerticallyTo(sideLengthPx - this.blockSize);
        this.right.moveHorizontallyTo(sideLengthPx - this.blockSize);
    }
    
    public int size() {
        return 2 * this.index - 1;
    }

    public Lid getLid() {
        return this.lid;
    }

    // this is in terms of pixels
    public void centerX(int leftLimit, int totalWidth) {
        int newX = leftLimit + (totalWidth - this.size()*this.blockSize)/2;
        this.base.moveHorizontallyTo(newX);
        this.left.moveHorizontallyTo(newX);
        this.right.moveHorizontallyTo(newX);
    }

    public void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
        this.left.moveVerticallyTo(y);
        this.right.moveVerticallyTo(y);
    }

    public void makeVisible() {
        this.base.makeVisible();
        this.left.makeVisible();
        this.right.makeVisible();
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.left.makeInvisible();
        this.right.makeInvisible();
    }

    public void moveVerticallyFromTo(int from, int to, boolean show) {
        if (show) {
            this.moveVerticallyTo(from);
            this.makeVisible();
        }
        this.moveVerticallyTo(to);
    }

    public void makeLidVisible() {
        this.lid.makeVisible();
    }

    public void makeLidInvisible() {
        this.lid.makeInvisible();
    }
}