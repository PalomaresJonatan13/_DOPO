package tests;
import tower.*;

import javax.swing.JOptionPane;
import java.util.*;

public class AcceptanceTests {
    public void test1() {
        JOptionPane.showMessageDialog(
            null,
            "Marc is just trying to see what the Tower class is capable of, so he first creates a tower with the width enought for a cup of index 7 to fit into the tower, and a height of 15 units."
        );
        Tower tower = new Tower(7, 15); // 20
        
        JOptionPane.showMessageDialog(
            null,
            "After creating the tower, Marc wants to add some items, so he pushes a new cup of index 3."
        );
        tower.pushCup(3); // 30

        JOptionPane.showMessageDialog(
            null,
            "He wants to know what is the height reached by the items."
        );
        System.out.println("Height reached: "+tower.height()); // 35
        
        JOptionPane.showMessageDialog(
            null,
            "Marc wants to push a cup of index 6, but somehow he type '-' before typing the 6 by accident, but one cannot push cups with nonpositive indexes, so the operation wont be accepted and a message will be shown."
        );
        tower.pushCup(-6); // 40
        
        JOptionPane.showMessageDialog(
            null,
            "Trying again to push a cup of index 6, a message is shown and the cup is not pushed. Marc just found out that if the tower is visible one cannot push items so that the new height reached by the items is greater than the maximum height of the tower."
        );
        tower.pushCup(6); // 50
        
        JOptionPane.showMessageDialog(
            null,
            "As the operation was not valid, he still wants to push that cup but when the tower is invisible, so he changes the visibility."
        );
        tower.makeInvisible(); // 60
        
        JOptionPane.showMessageDialog(
            null,
            "Now, Marc tries again to push the cup of index 6, and the operation is successful!"
        );
        tower.pushCup(6); // 70
        
        JOptionPane.showMessageDialog(
            null,
            "As the tower is invisible, Marc is not completely sure about whether the height reached by the items changed or not, so he ask for the height reached by the items in the tower."
        );
        System.out.println("Height reached: "+tower.height()); // 75
        
        JOptionPane.showMessageDialog(
            null,
            "It seems that the height indeed changed, and now tries to make the tower visible again so that he can see the two cups."
        );
        tower.makeVisible(); // 80
        
        JOptionPane.showMessageDialog(
            null,
            "Marc is now confused because he tried to make the tower visible but there was no message saying that it isn't possible to do so, but the tower remains invisible. So he decides to check if there was a bug in the code by asking if the tower is ok, because of it is ok it will mean something is wrong with the program."
        );
        System.out.println("Tower is ok: "+tower.ok()); // 85
        
        JOptionPane.showMessageDialog(
            null,
            "It's so weird that the last operation was not ok (which makes sense since the tower's visibility was not changed), but there was no message. At that point, Marc remembers his friend Amalia told him that while the tower is invisible, no message will be shown. 'That explains everything', he says out loud. So he tries to pop the cup to make the tower visible again."
        );
        tower.popLid(); // 90
        System.out.println("Height reached: "+tower.height());
        
        JOptionPane.showMessageDialog(
            null,
            "Marc pressed the button to pop a lid accidentally, but there is not even a lid in the tower to be popped, that's why the height reached by the items didnt change. He is gonna try to remove that last cup again."
        );
        tower.popCup(); // 100
        System.out.println("Height reached: "+tower.height());
        
        JOptionPane.showMessageDialog(
            null,
            "Now that the last cup was deleted from the tower, he makes the tower visible again."
        );
        tower.makeVisible(); // 110
        
        JOptionPane.showMessageDialog(
            null,
            "As everything is visible again, Marc tries to push more items, like a cup of index 5."
        );
        tower.pushCup(5); // 120
        
        JOptionPane.showMessageDialog(
            null,
            "Now a lid of index 2."
        );
        tower.pushLid(2); // 130

        JOptionPane.showMessageDialog(
            null,
            "He wants another lid of index 2."
        );
        tower.pushLid(2); // 135
        
        JOptionPane.showMessageDialog(
            null,
            "Marc remembers that you can only have one cup and one lid for a given index. And before leaving, he removes the cup with index 3 he previously added to the tower."
        );
        tower.removeCup(3); // 140
        
        JOptionPane.showMessageDialog(
            null,
            "Now he leaves."
        );
        tower.exit(); // 150
    }



    public void test2() {
        JOptionPane.showMessageDialog(
            null,
            "Briana has some familiarity with the Tower class, but still wants to test some methods added recently, like the ones to reverse the tower or cover the cups. So she starts by creating a new Tower with 5 cups."
        );
        Tower tower = new Tower(5); // 10

        JOptionPane.showMessageDialog(
            null,
            "With the tower created she continues to reverse the tower."
        );
        tower.reverseTower(); // 20

        JOptionPane.showMessageDialog(
            null,
            "Now she adds a lid of index 1."
        );
        tower.pushLid(1); //30

        JOptionPane.showMessageDialog(
            null,
            "She will try to reverse the tower."
        );
        tower.reverseTower(); // 40

        JOptionPane.showMessageDialog(
            null,
            "It seems that the cup of index 5 was removed from the tower because it didn't fit after reversing the tower, but she wants that cup back, so she pops the lid ..."
        );
        tower.popLid();
        JOptionPane.showMessageDialog(
            null,
            "Pushes it again ..."
        );
        tower.pushLid(1);
        JOptionPane.showMessageDialog(
            null,
            "Reverses the tower ..."
        );
        tower.reverseTower();
        JOptionPane.showMessageDialog(
            null,
            "And add the cup"
        );
        tower.pushCup(5);

        JOptionPane.showMessageDialog(
            null,
            "Now she will reverse the tower without any item being unexpectedly removed."
        );
        tower.reverseTower(); // 60

        JOptionPane.showMessageDialog(
            null,
            "She pushes another lid, this time of index 3."
        );
        tower.pushLid(3); // 70

        JOptionPane.showMessageDialog(
            null,
            "But she want to swap it with the other lid in the tower"
        );
        tower.swap(new String[] {"lid", "3"}, new String[] {"lid", "1"}); // 50

        JOptionPane.showMessageDialog(
            null,
            "Until now she has been visualizing all the changes in the tower, but she wants to have a written version of the order of the items, so she calls stackingItems."
        );
        System.out.println("Items in the tower: "+Arrays.deepToString(tower.stackingItems())); // 75

        JOptionPane.showMessageDialog(
            null,
            "She removes the cup of index 2."
        );
        tower.removeCup(2); // 80

        JOptionPane.showMessageDialog(
            null,
            "Now the cover method will be tested. She will swap the first with the last item, and then cover the cups. So, she swaps"
        );
        tower.swap(new String[] {"cup", "5"}, new String[] {"lid", "1"}); // 90
        JOptionPane.showMessageDialog(
            null,
            "Then cover and obtain the items."
        );
        tower.cover(); // 90
        System.out.println("Items in the tower: "+Arrays.deepToString(tower.stackingItems()));

        JOptionPane.showMessageDialog(
            null,
            "One of the last things she will do is to order the items."
        );
        tower.orderTower(); // 110

        JOptionPane.showMessageDialog(
            null,
            "And wants to know what was the height reached by the items, and verify if the order from stackingItems is the same as the visualized."
        );
        System.out.println("Height reached: "+tower.height()); // 120
        System.out.println("Items in the tower: "+Arrays.deepToString(tower.stackingItems())); // 125

        JOptionPane.showMessageDialog(
            null,
            "Finally, she exits."
        );
        tower.exit(); // 130
    }
}