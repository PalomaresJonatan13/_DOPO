package tower;

import java.awt.Color;
import exceptions.TowerException;

public class main_ {
    public static void main(String[] args) {
/*         System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.34f, 0.5f, 0.5f))); // s
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.34f, 0.4f, 0.5f))); // s
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.34f, 0.6f, 0.5f))); // s
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.21f, 0.4f, 0.5f))); // h
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.40f, 0.4f, 0.5f))); // h
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.58f, 0.4f, 0.5f))); // h
        System.out.println(TowerItem.isAValidItemColor(Color.getHSBColor(0.73f, 0.4f, 0.5f))); // h */
        Tower tower = new Tower(5, 10);
        // tower.makeInvisible();
        tower.pushCup(5);
        tower.pushCup(4, Cup.OPENER);
        tower.pushLid(3, Lid.CRAZY);
        tower.pushCup(3, Cup.HIERARCHICAL);
        // tower.pushLid(5);
        tower.pushLid(4, Lid.FEARFUL);

        // tower.makeVisible();
    }
}
