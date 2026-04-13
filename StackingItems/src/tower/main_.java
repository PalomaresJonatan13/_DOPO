package tower;

import java.awt.Color;
import exceptions.TowerException;
import tests.tower.TestUtils;

public class main_ {
    public static void main(String[] args) {
        Tower tower = new Tower(6, 30);
        // tower.makeInvisible();
        tower.pushLid(4);
        tower.pushCup(6);
        tower.pushLid(1);
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushLid(6);
        tower.pushCup(4, TestUtils.HIERARCHICAL_CUP);
        tower.pushCup(3);
        tower.cover();


        // tower.makeVisible();
    }
}
