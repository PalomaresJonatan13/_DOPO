
/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------
import java.util.*;
import java.awt.*;

public class Cup extends TowerItem {
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    private Lid lid;
    private static HashMap<Integer, Cup> ActiveCups = new HashMap<>();
    private static final Random RANDOM = new Random();
    private static final Color[] _COLORS = {
        hexColor("#132A13"), hexColor("#31572C"), hexColor("#4F772D"), hexColor("#90A955"), hexColor("#ECF39E"), hexColor("#1B998B"), hexColor("#2D3047"), hexColor("#BB6B00"), hexColor("#452103"), hexColor("#8C7051"), hexColor("#690500"), hexColor("#053C5E"), hexColor("#F0E5D8"), hexColor("#000000"), hexColor("#84BCDA"), hexColor("#ECC30B"), hexColor("#46237A"), hexColor("#403D58"), hexColor("#4E598C"), hexColor("#D5BF86")
    };

    private Cup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.isCup = true;
        this.setColor();

        // this.lid = new Lid(index, this.color, blockSize, towerMargin, towerWidth, towerHeight);
        this.createSides();

        this.setHeightReached(this.height());
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
        int colorIndex = RANDOM.nextInt(_COLORS.length);
        this.color = _COLORS[colorIndex];
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (Arrays.asList(_COLORS).contains(color)) {
                this.color = color;

                this.base.changeColor(this.color);
                this.left.changeColor(this.color);
                this.right.changeColor(this.color);

                this.lid.setColor(color);
            } else {
                throw new IllegalArgumentException("invalid color");
            }
        }
    }

    public int height() {
        return this.width();
    }

    public String[] asArray() {
        return new String[]{"cup", this.index+""};
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

    public void lidItem() {
        Lid lid = this.getLid();
        if (!lid.isLidded()) lid.lidItem();
    }

    public void unlidItem() {
        Lid lid = this.getLid();
        if (lid.isLidded()) lid.unlidItem();
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
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.left.makeInvisible();
        this.right.makeInvisible();
    }

    private static Color hexColor(String hex) {
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16)
        );
    }

    public static Cup findCup(int index) {
        return ActiveCups.get(index);
    }

    public void deactivate() {
        ActiveCups.remove(this.getIndex());
    }
}