package shapes;

public class Rectangle extends Shape_ {
    private int height;
    private int width;

    public Rectangle(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------

    @Override
    protected void draw() {
        if(this.visible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(
                this,
                this.color,
                new java.awt.Rectangle(
                    this.xPosition, this.yPosition, this.width, this.height
                )
            );
            // canvas.wait(10);
        }
    }
}

