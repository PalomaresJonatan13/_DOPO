package domain;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected boolean active;

    public GameObject(double x, double y, double width, double height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive.");
        if (x < 0 || y < 0) throw new IllegalArgumentException("x and y must be non-negative.");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public boolean intersects(GameObject other) { // maybe change this to make it depend on the shape of the object
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y;
    }

    public boolean isActive() {
        return this.active;
    }
}
