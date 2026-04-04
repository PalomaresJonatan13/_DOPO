package tower;
import exceptions.*;

import java.util.*;

class FearfulLid extends Lid {
    private FearfulLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, Lid.FEARFUL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Lid getLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new FearfulLid(index, blockSize, towerMargin, towerWidth, towerHeight);
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

    public boolean updateLiddedState() { // called during pushItem
        super.updateLiddedState();
        this.isRemovable = !this.isLidded;
        return this.isLidded;
    }

    public TowerItem onPush(Tower tower) throws TowerException {
        TowerItem placeholder = null;
        HashMap<String, TowerItem> items = activeItems.get(this.index);
        if (items != null && items.get("cup") != null) {
            placeholder = this;
        } else {
            tower.pop();
            // this.disable();
            throw new TowerException(TowerException.INVALID_PUSH_FEARFUL(index));
        }
        return placeholder;
    }
}
