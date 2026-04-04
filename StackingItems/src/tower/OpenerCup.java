package tower;

import java.util.HashMap;

class OpenerCup extends Cup {
    protected OpenerCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, Cup.OPENER, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new OpenerCup(index, blockSize, towerMargin, towerWidth, towerHeight);
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

    public TowerItem onPush(Tower tower) {
        tower.pop();
        // this.disable();
        
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
