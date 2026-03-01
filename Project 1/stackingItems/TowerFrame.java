
/**
 * Write a description of class TowerFrame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */ // ------------------------------------------------------ // ------------------------------------------------------ // ------------------------------------------------------

import java.util.*;

// CONSIDER ADDING A VERTICAL PADDING TO THE FRAME
public class TowerFrame {
    private Rectangle leftBorder;
    private Rectangle topBorder;
    private Rectangle rightBorder;
    private Rectangle bottomBorder;
    private List<Rectangle> ticks;
    private static int THICKNESS = 1;
    private static int TICKLENGTH = 5;
    private int width;
    private int height;
    private int space;
    private int margin;

    // width, height, space and margin in px
    public TowerFrame(int width, int height, int space, int margin) {
        this.width = width;
        this.height = height;
        this.space = space;
        this.margin = margin;
        this.createBorder();
        this.createTicks();
    }

    private void createBorder() {
        this.leftBorder = new Rectangle(THICKNESS, this.height + 1);
        this.leftBorder.moveTo(this.margin - 1, this.margin);

        this.topBorder = new Rectangle(this.width + 1, THICKNESS);
        this.topBorder.moveTo(this.margin, this.margin - 1);

        this.rightBorder = new Rectangle(THICKNESS, this.height + 1);
        this.rightBorder.moveTo(this.margin + this.width + 1, this.margin);

        this.bottomBorder = new Rectangle(this.width + 1, THICKNESS);
        this.bottomBorder.moveTo(this.margin, this.margin + this.height + 1);
    }

    private void createTicks() {
        this.ticks = new ArrayList<>();
        int xPosition = this.margin - TICKLENGTH - 1;
        for (int i=0; i <= this.height/this.space; i++) {
            Rectangle tick = new Rectangle(TICKLENGTH, THICKNESS);
            int yPosition = this.margin + this.height - this.space*(i);
            tick.moveTo(xPosition, yPosition);
            this.ticks.add(tick);
        }
    }

    public void makeVisible() {
        this.leftBorder.makeVisible();
        // this.topBorder.makeVisible();
        this.rightBorder.makeVisible();
        this.bottomBorder.makeVisible();

        for (Rectangle tick : this.ticks) {
            tick.makeVisible();
        }
    }

    public void makeInvisible() {
        this.leftBorder.makeInvisible();
        this.topBorder.makeInvisible();
        this.rightBorder.makeInvisible();
        this.bottomBorder.makeInvisible();

        for (Rectangle tick : this.ticks) {
            tick.makeInvisible();
        }
    }
}