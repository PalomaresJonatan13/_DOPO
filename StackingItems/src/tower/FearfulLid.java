package tower;
import exceptions.*;
import shapes.*;

import java.util.*;
import java.awt.Color;

class FearfulLid extends Lid {
    private FearfulLid(int index, int towerWidth, int towerHeight) {
        super(index, Lid.FEARFUL, towerWidth, towerHeight);
        // this.extraShapes = new Shape_[]{};
    }

    public static Lid getLid(int index, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new FearfulLid(index, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            Lid lid_ = (Lid) associatedItems.get("lid");
            if (lid_.getType() == Lid.FEARFUL) lid = lid_;
        }
        return lid;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void createExtraShapes() {
        int height = this.height()*BLOCKSIZE;

        this.extraShapes = new Shape_[2];
        this.extraShapes[0] = new Rectangle(BLOCKSIZE/4, height/2);
        this.extraShapes[1] = new Rectangle(BLOCKSIZE/4, height/2);
        this.extraShapes[0].changeColor(Color.BLACK);
        this.extraShapes[1].changeColor(Color.WHITE);
    }

    protected void centerExtraShapesX() {
        int thickness = this.extraShapes[0].getWidth();
        int newX = TOWER_MARGIN + (this.towerWidth - this.width())*BLOCKSIZE/2 - thickness;
        this.extraShapes[0].moveHorizontallyTo(newX);
        this.extraShapes[1].moveHorizontallyTo(newX);
    }

    protected void moveExtraShapesVertically() {
        int shapeHeight = this.extraShapes[0].getHeight();
        int lidTop = TOWER_MARGIN + (this.towerHeight - this.heightReached)*BLOCKSIZE;
        this.extraShapes[0].moveVerticallyTo(lidTop);
        this.extraShapes[1].moveVerticallyTo(lidTop + shapeHeight);
    }

    public boolean updateLiddedState() { // called during pushItem
        super.updateLiddedState();
        this.isRemovable = !this.isLidded;
        return this.isLidded;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public TowerItem onPush(Tower tower) throws TowerException {
        TowerItem placeholder = null;

        HashMap<String, TowerItem> items = activeItems.get(this.index);
        if (items != null && items.get("cup") != null) {
            placeholder = this;
        } else {
            tower.pop();
            throw new TowerException(TowerException.INVALID_PUSH_FEARFUL(index));
        }
        
        return placeholder;
    }
}
