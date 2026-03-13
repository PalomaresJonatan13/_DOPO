import java.awt.*;
 
/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */
 
public class Rectangle extends Shape_ {
    public static final int EDGES = 4;
    private int height;
    private int width;

    public Rectangle(int width, int height) { // ------------------------------------------------------
        this.width = width;
        this.height = height;
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = Color.black;
        this.isVisible = false;
    }

    public int getWidth() { // ------------------------------------------------------
        return this.width;
    }

    public int getHeight() { // ------------------------------------------------------
        return this.height;
    }

    protected void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }
}

