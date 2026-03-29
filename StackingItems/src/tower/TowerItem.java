package tower;

import java.awt.Color;
import java.util.*;


abstract class TowerItem {
    protected int index;
    protected Color color;
    protected int heightReached;
    protected boolean isVisible = false;
    protected boolean isLidded = false;
    protected boolean isActive = false;

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
    protected static HashMap<Integer, HashMap<String, TowerItem>> activeItems = new HashMap<>();

    protected TowerItem(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        this.blockSize = blockSize;
        this.towerMargin = towerMargin;
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.isLidded = false;
        this.setColor();
        this.createSides();

        this.setHeightReached(0);
        this.centerX();

        this.enable();
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int getIndex() {
        return this.index;
    }

    public int width() {
        return 2 * this.index - 1;
    }

    public Color getColor() {
        return this.color;
    }

    protected void setColor() {
        this.color = randomItemColor();
    }

    public int getHeightReached() {
        return this.heightReached;
    }

    public void setHeightReached(int heightReached) {
        if (heightReached >= 0) {
            this.heightReached = heightReached;
            this.moveTo(heightReached - this.height());
        } else {
            throw new IllegalStateException("The value given must be non-negative.");
        }
    }

    public void onPush()   { /* EMPTY */ };
    public void onPop()    { /* EMPTY */ };
    public void onRemove() { /* EMPTY */ };
    public void onCover()  { /* EMPTY */ };

    protected void moveTo(int y) {
        int bottomOfTower = this.towerMargin + this.towerHeight*this.blockSize;
        this.moveVerticallyTo(bottomOfTower - (y + this.height())*this.blockSize);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public abstract void enable();
    public abstract void disable();
    public abstract boolean isCup();
    public abstract int height();
    public abstract String[] asArray();
    public abstract boolean updateLiddedState();
    public abstract void setColor(Color color);
    public abstract void makeVisible();
    public abstract void makeInvisible();
    protected abstract void createSides();
    protected abstract void centerX();
    protected abstract void moveVerticallyTo(int y);

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

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

    public static TowerItem fromArray(String[] array) {
        TowerItem towerItem = null;
        if (isAValidItem(array)) {
            int index = Integer.parseInt(array[1]);
            if (activeItems.containsKey(index)) {
                towerItem = activeItems.get(index).get(array[0]);
            }
        } else {
            throw new IllegalArgumentException("The given array is not a valid array representation of a tower item. It should be a string array of length 2, where the first element is either `cup` or `lid` and the second element is a positive integer representing the size of the item.");
        }
        return towerItem;
    }

    public static void clearActiveItems() {
        Set<Integer> indexes = new HashSet<>(activeItems.keySet());
        for (Integer index : indexes) {
            HashMap<String, TowerItem> items = activeItems.get(index);
            TowerItem cup = items.get("cup");
            if (cup != null) cup.disable();

            if (activeItems.containsKey(index)) {
                TowerItem lid = items.get("lid");
                lid.disable();
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

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

    private static Color hexColor(String hex) {
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16)
        );
    }
}