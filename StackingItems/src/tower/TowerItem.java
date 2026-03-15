package tower;

import java.awt.Color;
import java.util.Random;


public abstract class TowerItem {
    protected int index;
    protected Color color;
    protected int heightReached;
    protected boolean isCup;
    protected boolean isVisible = false;
    protected boolean isLidded = false;

    protected int blockSize;
    protected int towerMargin;
    protected int towerWidth;
    protected int towerHeight;

    private static final Random RANDOM = new Random();
    public static final Color[] _COLORS = {
        hexColor("#000000"), hexColor("#053C5E"), hexColor("#132A13"), hexColor("#1B998B"),
        hexColor("#2D3047"), hexColor("#31572C"), hexColor("#403D58"), hexColor("#452103"),
        hexColor("#46237A"), hexColor("#4E598C"), hexColor("#4F772D"), hexColor("#690500"),
        hexColor("#84BCDA"), hexColor("#8C7051"), hexColor("#90A955"), hexColor("#BB6B00"),
        hexColor("#D5BF86"), hexColor("#ECC30B"), hexColor("#ECF39E"), hexColor("#F0E5D8")
    };

    public abstract int height();
    protected abstract void moveVerticallyTo(int y);
    public abstract void makeVisible();
    public abstract void makeInvisible();
    public abstract void setColor(Color color);
    public abstract void deactivate();
    public abstract String[] asArray();
    public abstract boolean updateLiddedState();

    public int getIndex() {
        return this.index;
    }

    public int width() {
        return 2 * this.index - 1;
    }

    public int getHeightReached() {
        return this.heightReached;
    }

    public void setHeightReached(int heightReached) {
        this.heightReached = heightReached;
        this.moveTo(heightReached - this.height());
    }

    public boolean isCup() {
        return this.isCup;
    }

    public Color getColor() {
        return this.color;
    }

    protected void moveTo(int y) {
        int bottomOfTower = this.towerMargin + this.towerHeight*this.blockSize;
        this.moveVerticallyTo(bottomOfTower - (y + this.height())*this.blockSize);
    }

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

    private static Color hexColor(String hex) {
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16)
        );
    }

    public static Color randomItemColor() {
        int colorIndex = RANDOM.nextInt(_COLORS.length);
        return _COLORS[colorIndex];
    }

    public static Color randomItemColor(Color differentColor) {
        int colorIndex = RANDOM.nextInt(_COLORS.length);
        Color color = _COLORS[colorIndex];
        while (color == differentColor) {
            colorIndex = RANDOM.nextInt(_COLORS.length);
            color = _COLORS[colorIndex];
        }
        return color;
    }
}