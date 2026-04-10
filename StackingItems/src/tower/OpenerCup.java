package tower;
import shapes.*;

import java.awt.Color;
import java.util.HashMap;

class OpenerCup extends Cup {
    protected OpenerCup(int index, int towerWidth, int towerHeight) {
        super(index, Cup.OPENER, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new OpenerCup(index, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            Cup cup_ = (Cup) associatedItems.get("cup");
            if (cup_.getType() == Cup.OPENER) cup = cup_;
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void createExtraShapes() {
        this.extraShapes = new Shape_[2];
        this.extraShapes[0] = new Triangle((int) (BLOCKSIZE * 2f/3));
        this.extraShapes[1] = new Triangle((int) (BLOCKSIZE * 2f/3));
        this.extraShapes[0].changeColor(Color.WHITE);
        this.extraShapes[1].changeColor(Color.BLACK);
    }

    protected void centerExtraShapesX() {
        int shapeWidth = this.extraShapes[0].getWidth();
        int center = TOWER_MARGIN + this.towerWidth*BLOCKSIZE/2;
        this.extraShapes[0].moveHorizontallyTo(center - shapeWidth/4);
        this.extraShapes[1].moveHorizontallyTo(center + shapeWidth/4);
    }

    protected void moveExtraShapesVertically() {
        int shapeHeight = this.extraShapes[0].getHeight();
        int newY = TOWER_MARGIN + (this.towerHeight - (this.heightReached - this.height())) * BLOCKSIZE - shapeHeight/2;
        this.extraShapes[0].moveVerticallyTo(newY);
        this.extraShapes[1].moveVerticallyTo(newY);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public TowerItem onPush(Tower tower) {
        tower.pop();
        tower.pushCup(this.index);
        TowerItem placeholder = Cup.getCup(this.index);

        String[][] items = tower.stackingItems();
        int j = items.length - 2;
        boolean lidsToRemove = true;
        while (j >= 0 && lidsToRemove) {
            TowerItem item = TowerItem.fromArray(items[j]);
            if (
                !item.isCup() &&
                item.getHeightReached() == placeholder.getHeightReached() - placeholder.height() &&
                item.isRemovable()
            ) {
                tower.pop();
                tower.pop();
                tower.pushCup(this.index);
                placeholder = Cup.getCup(this.index);
            } else {
                lidsToRemove = false;
            }
            j--;
        }
        return placeholder;        
    }
}
