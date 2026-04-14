package tower;
import shapes.*;

import java.awt.Color;
import java.util.HashMap;

import exceptions.TowerException;

class CoveredCup extends Cup {
    protected CoveredCup(int index, int towerWidth, int towerHeight) {
        super(index, Cup.COVERED, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new CoveredCup(index, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            Cup cup_ = (Cup) associatedItems.get("cup");
            if (cup_.getType().equals(Cup.COVERED)) cup = cup_;
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createExtraShapes() {
        this.extraShapes = new Shape_[2];
        this.extraShapes[0] = new Circle((int) (BLOCKSIZE * 1f/3));
        this.extraShapes[1] = new Circle((int) (BLOCKSIZE * 1f/6));
        this.extraShapes[0].changeColor(Color.WHITE);
        this.extraShapes[1].changeColor(Color.BLACK);
    }

    @Override
    protected void centerExtraShapesX() {
        int left = TOWER_MARGIN + (this.towerWidth - this.width())*BLOCKSIZE/2;
        this.extraShapes[0].moveHorizontallyTo(left);
        this.extraShapes[1].moveHorizontallyTo(left);
    }

    @Override
    protected void moveExtraShapesVertically() {
        int bottom = TOWER_MARGIN + (this.towerHeight - (this.heightReached - this.height())) * BLOCKSIZE;
        this.extraShapes[0].moveVerticallyTo(bottom);
        this.extraShapes[1].moveVerticallyTo(bottom);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    public TowerItem onPush(Tower tower) throws TowerException {
        tower.pop();
        tower.pushCup(this.index);

        TowerItem lid = Lid.getLid(this.index);
        if (lid == null) {
            tower.pushLid(this.index);
        } else {
            tower.remove(this.index, false);
            tower.pushLid(this.index, lid.getType());
        }
        if (TowerItem.getTowerItem(this.index, false) == null) throw new TowerException(TowerException.OVERFLOW(this.index, true, this.type));
        
        TowerItem placeholder = Cup.getCup(this.index);
        return placeholder;        
    }
}
