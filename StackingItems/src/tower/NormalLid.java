package tower;

import java.util.HashMap;

class NormalLid extends Lid {
    private NormalLid(int index, int towerWidth, int towerHeight) {
        super(index, Lid.NORMAL, towerWidth, towerHeight);
    }

    public static Lid getLid(int index, int towerWidth, int towerHeight) {
        Lid lid = null;
        HashMap<String, TowerItem> associatedItems = activeItems.get(index);

        if (associatedItems == null || associatedItems.get("lid") == null) {
            lid = new NormalLid(index, towerWidth, towerHeight);
            Cup cup = lid.cup();
            if (cup != null) {
                lid.setColor(cup.getColor());
            }
        } else {
            Lid lid_ = (Lid) associatedItems.get("lid");
            if (lid_.getType() == Lid.NORMAL) lid = lid_;
        }
        return lid;
    }
}
