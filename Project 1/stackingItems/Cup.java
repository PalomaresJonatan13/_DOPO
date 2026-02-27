
/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------
import java.util.*;
import java.awt.*;

public class Cup {
    private int index;
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    private Color color;
    private Lid lid;
    private int blockSize;
    private static final Random RANDOM = new Random();
    private static final Color[] _COLORS = {
        hexColor("#132A13"), hexColor("#31572C"), hexColor("#4F772D"), hexColor("#90A955"), hexColor("#ECF39E"), hexColor("#1B998B"), hexColor("#2D3047"), hexColor("#BB6B00"), hexColor("#452103"), hexColor("#8C7051"), hexColor("#690500"), hexColor("#053C5E"), hexColor("#F0E5D8"), hexColor("#000000"), hexColor("#84BCDA"), hexColor("#ECC30B"), hexColor("#46237A"), hexColor("#403D58"), hexColor("#4E598C"), hexColor("#D5BF86")
    };

    public Cup(int index, int blockSize) {
        this.index = index;
        this.blockSize = blockSize;
        this.setColor();

        this.lid = new Lid(index, this.color, blockSize);
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

    private void setColor() {
        int colorIndex = RANDOM.nextInt(_COLORS.length);
        this.color = _COLORS[colorIndex];
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
        this.right.moveHorizontallyTo(newX + (this.size() - 1)*this.blockSize);
    }

    public void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y + (this.size() - 1)*this.blockSize);
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

    /* public void moveVerticallyFromTo(int from, int to, boolean show) {
        if (show) {
            this.moveVerticallyTo(from);
            this.makeVisible();
        }
        this.moveVerticallyTo(to);
    } */

    public void makeLidVisible() {
        this.lid.makeVisible();
    }

    public void makeLidInvisible() {
        this.lid.makeInvisible();
    }

    private static Color hexColor(String hex) {
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16)
        );
    }
}