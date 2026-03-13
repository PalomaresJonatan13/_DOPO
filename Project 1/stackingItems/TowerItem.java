import java.awt.*;


public abstract class TowerItem {
    protected int index;
    protected Color color;
    protected int heightReached;
    protected boolean isCup;
    protected boolean isVisible;
    protected boolean isLidded;

    protected int blockSize;
    protected int towerMargin;
    protected int towerWidth;
    protected int towerHeight;

    public void setHeightReached(int heightReached) {
        this.heightReached = heightReached;
        this.moveTo(heightReached - this.height());
    }

    protected void moveTo(int y) {
        int bottomOfTower = this.towerMargin + this.towerHeight*this.blockSize;
        this.moveVerticallyTo(bottomOfTower - (y + this.height())*this.blockSize);
    }

    public int getIndex() {
        return this.index;
    }

    public int width() {
        return 2 * this.index - 1;
    }

    public int getHeightReached() {
        return this.heightReached;
    }

    public boolean isCup() {
        return this.isCup;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isLidded() {
        return this.isLidded;
    }

    public abstract int height();
    protected abstract void moveVerticallyTo(int y);
    public abstract void makeVisible();
    public abstract void makeInvisible();
    public abstract void setColor(Color color);
    public abstract void deactivate();
    public abstract String[] asArray();
    public abstract void lidItem();
    public abstract void unlidItem();

    public static boolean isAValidItem(String[] item) {
        boolean isValid = false;
        if (item.length == 2) {
            if (item[0].equals("cup") || item[0].equals("lid")) {
                try {
                    if (Integer.parseInt(item[1]) > 0) {
                        isValid = true;
                    }
                } catch (NumberFormatException e) {}
            } 
        }
        return isValid;
    }
}