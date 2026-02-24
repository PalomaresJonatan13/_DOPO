import java.util.*;

public class TowerItem {
    private boolean isCup;
    private int index;
    private int heightReached;

    private int blockSize;
    private int towerMargin;
    private int towerWidth;
    private int towerHeight;

    private Cup associatedCup;
    private static HashMap<Integer, Cup> associatedCups = new HashMap<>();

    // margin and blockSize in px, the rest in blocks
    public TowerItem(boolean isCup, int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.isCup = isCup;
        this.index = index;
        this.associatedCup = this.associatedCup(index);
        this.setHeightReached(2*index - 1);
    }

    private Cup associatedCup(int index) {
        Cup cup = associatedCups.get(index);
        if (cup == null) {
            cup = new Cup(index, this.blockSize);
            associatedCups.put(index, cup);
        }
        if (this.isCup()) cup.centerX(this.towerMargin, this.blockSize*this.towerWidth);
        else cup.getLid().centerX(this.towerMargin, this.blockSize*this.towerWidth);
        return cup;
    }

    public void setHeightReached(int heightReached) {
        this.heightReached = heightReached;
        this.moveTo(heightReached - this.height());
    }

    // y is the coordinate of the bottom of the item
    private void moveTo(int y) {
        int bottomOfTower = this.towerMargin + this.towerHeight*this.blockSize;
        if (this.isCup()) {
            this.associatedCup.moveVerticallyTo(bottomOfTower - (y + this.height())*this.blockSize);
        } else {
            this.associatedCup.getLid().moveVerticallyTo(bottomOfTower - (y + this.height())*this.blockSize);
        }
    }

    public boolean isCup() {
        return this.isCup;
    }

    public int getIndex() {
        return this.index;
    }

    public int height() {
        return (this.isCup ? 2*this.index - 1 : 1);
    }

    public int getHeightReached() {
        return this.heightReached;
    }

    public void makeVisible() {
        if (this.isCup) {
            this.associatedCup.makeVisible();
        } else {
            this.associatedCup.getLid().makeVisible();
        }
    }

    public void makeInvisible() {
        if (this.isCup) {
            this.associatedCup.makeInvisible();
        } else {
            this.associatedCup.getLid().makeInvisible();
        }
    }

    public void deleteAssociatedCup() {
        associatedCups.remove(this.index);
    }
}