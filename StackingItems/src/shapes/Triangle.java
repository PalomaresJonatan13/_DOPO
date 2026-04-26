package shapes;

public class Triangle extends Shape {
    private int height;
    private int width;

    public Triangle(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public Triangle(int sideLength) {
        super();
        this.width = sideLength;
        this.height = (int) (sideLength/(Math.sqrt(2)));
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
    protected void draw(){
        if(this.visible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = {
                this.xPosition,
                this.xPosition + (this.width/2),
                this.xPosition - (this.width/2)
            };
            int[] ypoints = {
                this.yPosition,
                this.yPosition + this.height,
                this.yPosition + this.height
            };
            
            canvas.draw(
                this,
                this.color,
                new java.awt.Polygon(xpoints, ypoints, 3)
            );
            // canvas.wait(10);
        }
    }
}

