package tower;
import shapes.*;

import java.util.HashMap;

class NormalCup extends Cup {
    protected NormalCup(int index, int towerWidth, int towerHeight) {
        super(index, Cup.NORMAL, towerWidth, towerHeight);
        this.extraShapes = new Shape_[]{};
    }

    public static Cup getCup(int index, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new NormalCup(index, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            Cup cup_ = (Cup) associatedItems.get("cup");
            if (cup_.getType().equals(Cup.NORMAL)) cup = cup_;
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createExtraShapes() {
        this.extraShapes = new Shape_[0];
    }

    @Override
    protected void centerExtraShapesX() { /* EMPTY */ }

    @Override
    protected void moveExtraShapesVertically() { /* EMPTY */ }
}
