package tower;
import shapes.*;

import java.util.HashMap;

final class NormalLid extends Lid {
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
            lid = (Lid) associatedItems.get("lid");
            if (!lid.getType().equals(Lid.NORMAL)) lid = null;
        }
        return lid;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void createExtraShapes() {
        this.extraShapes = new Shape[0];
    }

    @Override
    protected void centerExtraShapesX() { /* EMPTY */ }

    @Override
    protected void moveExtraShapesVertically() { /* EMPTY */ }
}
