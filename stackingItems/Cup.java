
/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------
import java.util.List;
import java.util.ArrayList;

public class Cup
{
    private int i;
    private Rectangle base;
    private Rectangle left;
    private Rectangle right;
    private String color;
    private Lid lid;
    private int blockSize;

    public Cup(int i, String color, int blockSize) {
        this.i = i;
        this.base = new Rectangle();
        this.left = new Rectangle();
        this.right = new Rectangle();
        this.color = color;
        this.blockSize = blockSize;
        this.lid = new Lid(i, color);
        
        this.base.changeSize(size, this.height());
        this.left.changeSize((this.height()-1) * blockSize, blockSize);
        this.right.changeSize((this.height()-1) * blockSize, blockSize);
    }
    
    public int height() {
        return this.i * 2 - 1;
    }
    
    // IS THIS USEFUL?
    private void moveToOrigin() {
        this.left.moveToOrigin();
        this.base.moveTo(0, (this.height() - 1)*this.blockSize);
        this.right.moveTo((this.height() - 1)*this.blockSize, 0);
    }

    private void centerX(int leftLimit, int totalWidth) {                     // USE IN THE CONSTRUCTOR: no
        int halfWidth = this.base.getWidth()/2;
        int center = leftLimit + totalWidth/2;
        this.base.moveHorizontallyTo(center - halfWidth);
        this.left.moveHorizontallyTo(center - halfWidth);
        this.right.moveHorizontallyTo(center + halfWidth - this.blockSize);
    }

    public void moveVertical(int y) {
        this.base.moveVertical(y);
        this.left.moveVertical(y);
        this.right.moveVertical(y);
    }

    public void makeVisible() {
        this.base.makeVisible();
        this.left.makeVisible();
        this.right.makeVisible();
    }

    public void makeInvisible() {
        this.base.makeInvisible();
        this.left.makeInvisible();
        this.right.makeInvisible();
    }
}