package tower;

import exceptions.TowerException;

public class main_ {
    public static void main(String[] args) {
        TowerItem.clearActiveItems();
        //Lid lid = Lid.getLid(4, Lid.FEARFUL, 10, 10, 40, 100);
            Tower tower = new Tower(5, 10);
            tower.pushCup(3);
            tower.pushLid(2, Lid.FEARFUL);
            // tower.pushCup(4);
            Lid lid = Lid.getLid(2);
            tower.makeVisible();
            System.out.println(tower.stackingItems().length);
            //System.out.println(cup.getType() + " " + cup.isRemovable());

            // tower.makeVisible();
            // tower.removeLid(4);
            //System.out.println(e.getMessage());
    }
}
