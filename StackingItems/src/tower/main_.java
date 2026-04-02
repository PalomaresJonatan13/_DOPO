package tower;

import exceptions.TowerException;

public class main_ {
    public static void main(String[] args) {
        TowerItem.clearActiveItems();
        //Lid lid = Lid.getLid(4, Lid.FEARFUL, 10, 10, 40, 100);
            Tower tower = new Tower(5, 10);
            String cupType = "normal";
            String lidType = "crazy";
            tower.pushCup(1, cupType);
            tower.pushCup(4, cupType);
            tower.pushLid(3, lidType);
            tower.pushLid(2, lidType);
            tower.pushLid(4, lidType);
            System.out.println("next: pushLid 1");
            tower.pushLid(1, lidType);
            System.out.println(tower.stackingItems());
    }
}
