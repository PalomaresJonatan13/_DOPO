package tower;
import java.util.HashMap;

import exceptions.*;

class HierarchicalCup extends Cup {
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
            Cup cup_ = (Cup) associatedItems.get("cup");
            if (cup_.getType() == Cup.HIERARCHICAL) cup = cup_;
        }
        return cup;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

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
