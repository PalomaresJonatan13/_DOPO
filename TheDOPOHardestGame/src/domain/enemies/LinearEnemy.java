package domain.enemies;

public abstract class LinearEnemy extends Enemy implements Bouncable {
    protected double speedX;
    protected double speedY;

    public LinearEnemy(double centerX, double centerY, boolean horizontal, boolean toRightTop) {
        super(centerX, centerY);
        this.speedX = 0.075 * (horizontal ? (toRightTop ? 1 : -1) : 0);
        this.speedY = 0.075 * (horizontal ? 0 : (toRightTop ? -1 : 1));
    }

    @Override
    public void move() {
        if (this.active) {
            this.centerX += this.speedX;
            this.centerY += this.speedY;
        }
    }

    @Override
    public boolean canBounce() {
        return true;
    }

    @Override
    public void bounceX() {
        this.speedX = -this.speedX;
    }

    @Override
    public void bounceY() {
        this.speedY = -this.speedY;
    }
}
