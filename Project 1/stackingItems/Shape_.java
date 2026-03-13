import java.awt.*;
 
public abstract class Shape_ {    
    protected int xPosition;
    protected int yPosition;
    protected Color color;
    protected boolean isVisible;

    public int getX() { // ------------------------------------------------------
        return this.xPosition;
    }

    public int getY() { // ------------------------------------------------------
        return this.yPosition;
    }

    public abstract int getWidth();
    public abstract int getHeight();
    
    public void moveHorizontallyTo(int x) { // ------------------------------------------------------
        this.moveHorizontally(x - this.xPosition);
    }

    public void moveVerticallyTo(int y) { // ------------------------------------------------------
        this.moveVertically(y - this.yPosition);
    }

    public void moveTo(int x, int y) { // ------------------------------------------------------
        this.moveHorizontallyTo(x);
        this.moveVerticallyTo(y);
    }

    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    public void moveHorizontally(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertically(int distance){
        erase();
        yPosition += distance;
        draw();
    }
    
    public void changeColor(Color newColor){
        color = newColor;
        draw();
    }

    protected abstract void draw();

    protected void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}

