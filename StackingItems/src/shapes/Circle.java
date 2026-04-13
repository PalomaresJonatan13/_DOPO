package shapes;

import java.awt.geom.*;

public class Circle extends Shape_ {
    private int radius;
    
    public Circle(int radius) {
        super();
        this.radius = radius;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int getWidth() {
        return this.diameter();
    }

    public int getHeight() {
        return this.diameter();
    }

    public int radius() {
        return this.radius;
    }

    public int diameter() {
        return 2 * this.radius;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void draw(){
        if(this.isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int xCoord = this.xPosition - this.radius;
            int yCoord = this.yPosition - this.radius;
            canvas.draw(
                this,
                this.color, 
                new Ellipse2D.Double(
                    xCoord, yCoord, this.diameter(), this.diameter()
                )
            );
            // canvas.wait(10);
        }
    }
}
