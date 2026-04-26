package tower;
import exceptions.*;
import shapes.*;

import java.util.HashMap;
import java.awt.Color;

final class CrazyLid extends Lid {
    private CrazyLid(int index, int towerWidth, int towerHeight) {
        super(index, Lid.CRAZY, towerWidth, towerHeight);
        this.isInverted = true;
    }

    public static Lid getLid(int index, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new CrazyLid(index, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            lid = (Lid) associatedItems.get("lid");
            if (!lid.getType().equals(Lid.CRAZY)) lid = null;
        }
        return lid;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createExtraShapes() {
        int height = this.height()*BLOCKSIZE;

        this.extraShapes = new Shape[2];
        this.extraShapes[0] = new Rectangle(BLOCKSIZE/4, height/2);
        this.extraShapes[1] = new Rectangle(BLOCKSIZE/4, height/2);
        this.extraShapes[0].changeColor(Color.BLACK);
        this.extraShapes[1].changeColor(Color.WHITE);
    }

    @Override
    protected void centerExtraShapesX() {
        int newX = TOWER_MARGIN + (this.towerWidth + this.width())*BLOCKSIZE/2;
        this.extraShapes[0].moveHorizontallyTo(newX);
        this.extraShapes[1].moveHorizontallyTo(newX);
    }

    @Override
    protected void moveExtraShapesVertically() {
        int shapeHeight = this.extraShapes[0].getHeight();
        int lidTop = TOWER_MARGIN + (this.towerHeight - this.heightReached)*BLOCKSIZE;
        this.extraShapes[0].moveVerticallyTo(lidTop);
        this.extraShapes[1].moveVerticallyTo(lidTop + shapeHeight);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    public TowerItem onPush(Tower tower) throws TowerException {
        tower.pop();
        tower.pushLid(this.index);

        String[][] items = tower.stackingItems();
        int j = items.length - 2;
        int itemIndex = 0;
        while (j >= 0 && itemIndex < this.index) {
            TowerItem item = TowerItem.fromArray(items[j]);
            itemIndex = item.getIndex();
            if (itemIndex == this.index && item.updateLiddedState()) {
                tower.removeLid(this.index);
                tower.insert(this.index, true, item.getType(), j);
                tower.insert(this.index, false, this.type, j);
                j = 0;
            }
            j--;
        }
        
        TowerItem placeholder = Lid.getLid(this.index);
        /* if (placeholder == null) {
            tower.pushLid(this.index);
            placeholder = Lid.getLid(this.index);
        } */
        return placeholder;
    }
}
