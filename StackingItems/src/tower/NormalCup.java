package tower;

import java.util.HashMap;

class NormalCup extends Cup {
    protected NormalCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, Cup.NORMAL, blockSize, towerMargin, towerWidth, towerHeight);
    }

    public static Cup getCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        Cup cup = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("cup") == null) {
            cup = new NormalCup(index, blockSize, towerMargin, towerWidth, towerHeight);
            Lid lid = cup.lid();
            if (lid != null) {
                cup.setColor(lid.getColor());
            }
        } else {
            Cup cup_ = (Cup) associatedItems.get("cup");
            if (cup_.getType() == Cup.NORMAL) cup = cup_;
        }
        return cup;
    }
}
