package tower;
import exceptions.*;
import shapes.*;

import java.util.HashMap;
import java.awt.Color;

final class HierarchicalCup extends Cup {
    protected HierarchicalCup(int index, int towerWidth, int towerHeight) {
        super(index, Cup.HIERARCHICAL, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new HierarchicalCup(index, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            cup = (Cup) associatedItems.get("cup");
            if (!cup.getType().equals(Cup.HIERARCHICAL)) cup = null;
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createExtraShapes() {
        int width = this.width()*BLOCKSIZE;

        this.extraShapes = new Shape[2];
        this.extraShapes[0] = new Rectangle(width/4, BLOCKSIZE/4);
        this.extraShapes[1] = new Rectangle(width/4, BLOCKSIZE/4);
        this.extraShapes[0].changeColor(Color.BLACK);
        this.extraShapes[1].changeColor(Color.WHITE);
    }

    @Override
    protected void centerExtraShapesX() {
        int center = TOWER_MARGIN + this.towerWidth*BLOCKSIZE/2;
        Shape shape1 = this.extraShapes[0];
        Shape shape2 = this.extraShapes[1];
        shape1.moveHorizontallyTo(center - shape1.getWidth());
        shape2.moveHorizontallyTo(center);
    }

    @Override
    protected void moveExtraShapesVertically() {
        int newY = TOWER_MARGIN + (this.towerHeight - (this.heightReached - this.height())) * BLOCKSIZE;
        this.extraShapes[0].moveVerticallyTo(newY);
        this.extraShapes[1].moveVerticallyTo(newY);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    public TowerItem onPush(Tower tower) throws TowerException {
        tower.pop();

        String[][] items = tower.stackingItems();
        int j = items.length - 1;
        boolean foundGreaterIndex = false;
        while (j >= 0 && !foundGreaterIndex) {
            TowerItem item = TowerItem.fromArray(items[j]);
            if (item.getIndex() > this.index) {
                foundGreaterIndex = true;
                j++;
            }
            j--;
        }
        
        tower.insert(this.index, true, NORMAL, ++j);
        if (j == 0) this.isRemovable = false;
        return Cup.getCup(this.index);
    }
}
