package domain;

public class Enemy extends GameObject implements MovableObject {
    protected double speedX;
    protected double speedY;

    public Enemy(double x, double y, double width, double height, double speedX, double speedY) {
        super(x, y, width, height);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public void move() {
        if (this.active) {
            this.x += this.speedX;
            this.y += this.speedY;
        }
    }

    public void bounceX() {
        this.speedX = -this.speedX;
    }
    public void bounceY() {
        this.speedY = -this.speedY;
    }

    public void disable(){
        this.active = false;
    }

    /* public void reset() {
        this.active = true;
    } */
}
