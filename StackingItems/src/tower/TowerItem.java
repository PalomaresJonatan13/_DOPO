package tower;
import shapes.*;

import java.awt.Color;
import java.util.*;

import exceptions.TowerException;


abstract class TowerItem {
    protected int index;
    protected Color color;
    protected int heightReached;
    protected boolean isVisible = false;
    protected boolean isLidded = false;
    protected boolean isActive = false;
    protected boolean isRemovable = true;
    protected String type;
    protected Shape_[] extraShapes;

    protected int towerWidth;
    protected int towerHeight;
    
    private static final Random RANDOM = new Random();
    public static final int TOWER_MARGIN = Tower.MARGIN;
    public static final int BLOCKSIZE = Tower.BLOCKSIZE;
    protected static HashMap<Integer, HashMap<String, TowerItem>> activeItems = new HashMap<>();

    protected TowerItem(int index, String type, int towerWidth, int towerHeight) {
        this.towerWidth = towerWidth;
        this.towerHeight = towerHeight;

        this.index = index;
        this.type = type;
        this.setColor();
        this.createSides();
        this.createExtraShapes();

        this.setHeightReached(0);
        this.centerX();
    }

    public static TowerItem getTowerItem(
        int index, boolean isCup, String type, int towerWidth, int towerHeight
    ) {
        if (isAValidType(isCup, type)) {
            return (isCup ?
                Cup.getCup(index, type, towerWidth, towerHeight) :
                Lid.getLid(index, type, towerWidth, towerHeight)
            );
        } else throw new IllegalArgumentException("The type given is not valid.");
    }

    public static TowerItem getTowerItem(
        int index, boolean isCup, int towerWidth, int towerHeight
    ) {
        return (isCup ?
            Cup.getCup(index, towerWidth, towerHeight) :
            Lid.getLid(index, towerWidth, towerHeight)
        );
    }

    public static TowerItem getTowerItem(int index, boolean isCup) {
        return isCup ? Cup.getCup(index) : Lid.getLid(index);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int getIndex() {
        return this.index;
    }

    public String getType() {
        return this.type;
    }

    public boolean isRemovable() {
        return this.isRemovable;
    }

    public boolean isLidded() {
        return this.updateLiddedState();
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

    public TowerItem onPush(Tower tower) throws TowerException {
        return this;
    };
    public void onPop(Tower tower)    { /* EMPTY */ };
    public void onRemove(Tower tower) { /* EMPTY */ };
    public void onCover(Tower tower)  { /* EMPTY */ };

    protected void moveTo(int y) {
        int bottomOfTower = TOWER_MARGIN + this.towerHeight*BLOCKSIZE;
        this.moveVerticallyTo(bottomOfTower - (y + this.height())*BLOCKSIZE);
    }

    protected void makeExtraShapesVisible() {
        for (Shape_ shape : this.extraShapes) {
            shape.makeVisible();
        }
    }

    protected void makeExtraShapesInvisible() {
        for (Shape_ shape : this.extraShapes) {
            shape.makeInvisible();
        }
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
    protected abstract void createExtraShapes();
    protected abstract void centerX();
    protected abstract void centerExtraShapesX();
    protected abstract void moveVerticallyTo(int y);
    protected abstract void moveExtraShapesVertically();

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

    public static boolean isAValidType(boolean isCup, String type) {
        return isCup ?
            Cup.isAValidType(type) :
            Lid.isAValidType(type);
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
        float lim1 = (140f-1) / 360;
        float lim2 = (200f+1) / 360;
        float lim3 = (280f-1) / 360;
        float lim4 = (330f+1) / 360;
        float hue = ((RANDOM.nextBoolean()) ?
            RANDOM.nextFloat()*lim1 :
            ((RANDOM.nextBoolean()) ?
                lim2 + RANDOM.nextFloat()*(lim3-lim2) :
                lim4 + RANDOM.nextFloat()*(1-lim4)
            )
        );
        float saturation = (0.20f+0.01f) + RANDOM.nextFloat()*(0.3f-0.02f);
        float brightness = (0.15f+0.01f) + RANDOM.nextFloat()*(0.7f-0.02f);
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static Color randomItemColor(Color differentColor) {
        Color color = randomItemColor();
        while (color == differentColor) {
            color = randomItemColor();
        }
        return color;
    }

    public static boolean isAValidItemColor(Color color) {
        float lim1 = 140f / 360;
        float lim2 = 200f / 360;
        float lim3 = 280f / 360;
        float lim4 = 330f / 360;

        float[] hsb = Color.RGBtoHSB(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            null
        );
        
        return (
            (hsb[0] <= lim1 || (lim2 <= hsb[0] && hsb[0] <= lim3) || lim4 <= hsb[0]) &&
            (0.2 <= hsb[1] && hsb[1] <= 0.5) &&
            (0.15 <= hsb[2] && hsb[2] <= 0.85)
        );
    }
}