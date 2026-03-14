package shapes;

import java.awt.Color;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000) 
 */

public class Circle extends Shape_ {
    private int radius;
    
    public Circle(int radius) {
        this.radius = radius;
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = Color.BLACK;
        this.isVisible = false;
    }

    public int getWidth() { // ------------
        return this.diameter();
    }

    public int getHeight() { // ------------
        return this.diameter();
    }

    public int radius() {
        return this.radius;
    }

    public int diameter() {
        return 2 * this.radius;
    }

    protected void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition - this.radius, yPosition - this.radius, 
                this.diameter(), this.diameter()));
            canvas.wait(10);
        }
    }
}
