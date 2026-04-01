package shapes;

import java.awt.*;
 
public abstract class Shape_ {
    protected int xPosition;
    protected int yPosition;
    protected Color color;
    protected boolean isVisible;

    protected Shape_() {
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = Color.BLACK;
        this.isVisible = false;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public int getX() {
        return this.xPosition;
    }

    public int getY() {
        return this.yPosition;
    }

    public void changeColor(Color newColor){
        color = newColor;
        this.draw();
    }

    public void makeVisible() {
        if (!this.isVisible) {
            isVisible = true;
            this.draw();
        }
    }
    
    public void makeInvisible(){
        if (this.isVisible) {
            this.erase();
            isVisible = false;
        }
    }

    public void moveHorizontally(int distance){
        this.erase();
        this.xPosition += distance;
        this.draw();
    }

    public void moveVertically(int distance){
        this.erase();
        this.yPosition += distance;
        this.draw();
    }
    
    public void moveHorizontallyTo(int x) {
        this.moveHorizontally(x - this.xPosition);
    }

    public void moveVerticallyTo(int y) {
        this.moveVertically(y - this.yPosition);
    }

    public void moveTo(int x, int y) {
        this.moveHorizontallyTo(x);
        this.moveVerticallyTo(y);
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    public abstract int getWidth();
    public abstract int getHeight();
    protected abstract void draw();

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    protected void erase(){
        if(this.isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}

