
/**
 * Write a description of class Lid here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------


public class Lid {
    private int index;
    private Rectangle base;
    private String color;
    private int blockSize;

    public Lid(int index, String color, int blockSize) {
        this.index = index;
        this.color = color;
        this.blockSize = blockSize;
        this.createBase();
    }

    private void createBase() {
        this.base = new Rectangle(this.size()*this.blockSize, this.blockSize);
        this.base.changeColor(this.color);
    }

    public int size() {
        return 2 * this.index - 1;
    }

    // this is in terms of pixels
    public void centerX(int leftLimit, int totalWidth) {
        int newX = leftLimit + (totalWidth - this.size()*this.blockSize)/2;
        this.base.moveHorizontallyTo(newX);
    }

    public void moveVerticallyTo(int y) {
        this.base.moveVerticallyTo(y);
    }

    public void makeVisible() {
        this.base.makeVisible();
    }

    public void makeInvisible() {
        this.base.makeInvisible();
    }

    public void moveVerticallyFromTo(int from, int to, boolean show) {
        if (show) {
            this.moveVerticallyTo(from);
            this.makeVisible();
        }
        this.moveVerticallyTo(to);
    }
}