package tower;

import exceptions.TowerException;

public class main_ {
    public static void main(String[] args) {
        Tower tower = new Tower(5, 10);
        // tower.makeInvisible();
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(4);
        tower.pushLid(1);
        tower.pushCup(3);
        tower.cover();
    }
}
