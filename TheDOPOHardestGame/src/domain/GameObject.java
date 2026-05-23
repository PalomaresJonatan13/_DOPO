package domain;

import java.io.Serializable;

public abstract class GameObject implements Serializable {
    protected double centerX;
    protected double centerY;
    protected double width;
    protected double height;
    protected boolean active;

    public GameObject(double centerX, double centerY, double width, double height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getX() {
        return this.centerX - this.width / 2.0;
    }

    public double getY() {
        return this.centerY - this.height / 2.0;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public boolean intersects(GameObject other) {
        double thisLeft = this.getX();
        double thisRight = this.getX() + this.getWidth();
        double thisTop = this.getY();
        double thisBottom = this.getY() + this.getHeight();

        double otherLeft = other.getX();
        double otherRight = other.getX() + other.getWidth();
        double otherTop = other.getY();
        double otherBottom = other.getY() + other.getHeight();

        return thisLeft < otherRight &&
                thisRight > otherLeft &&
                thisTop < otherBottom &&
                thisBottom > otherTop;
    }

    public boolean isActive() {
        return this.active;
    }
}
