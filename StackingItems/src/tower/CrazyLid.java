package tower;
import java.util.HashMap;

import exceptions.*;

class CrazyLid extends Lid {
    private CrazyLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, Lid.CRAZY, blockSize, towerMargin, towerWidth, towerHeight);
        this.isInverted = true;
    }

    public static Lid getLid(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new CrazyLid(index, blockSize, towerMargin, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            Lid lid_ = (Lid) associatedItems.get("lid");
            if (lid_.getType() == Lid.CRAZY) lid = lid_;
        }
        return lid;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public TowerItem onPush(Tower tower) throws TowerException {
        tower.pop();
        // this.disable();

        tower.pushLid(this.index);
        // tower.makeVisible(); ///////
        String[][] items = tower.stackingItems();
        int j = items.length - 2;
        int itemIndex = 0;
        while (j >= 0 && itemIndex < this.index) {
            TowerItem item = TowerItem.fromArray(items[j]);
            itemIndex = item.getIndex();
            if (itemIndex == this.index && item.updateLiddedState()) {
                tower.removeLid(this.index);
                tower.insert(this.index, true, item.getType(), j);
                // tower.makeVisible(); ///////
                tower.insert(this.index, false, this.type, j);
                // tower.makeVisible(); ///////
                j = 0;
            }
            j--;
        }
        // tower.makeVisible(); ///////
        TowerItem placeholder = Lid.getLid(this.index);
        if (placeholder == null) {
            tower.pushLid(this.index);
            placeholder = Lid.getLid(this.index);
        }
        return placeholder;
    }
}
