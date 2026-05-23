package domain.enemies;

import domain.GameObject;
import domain.MovableObject;

public abstract class Enemy extends GameObject implements MovableObject {
    private static final double RADIUS = 0.25;

    public Enemy(double centerX, double centerY) {
        super(centerX, centerY, 2 * RADIUS, 2 * RADIUS);
    }

    @Override
    public String toString() {
        return String.format("Enemy{type=%s, x=%.2f, y=%.2f, active=%b}", this.getClass().getSimpleName(), centerX, centerY, active);
    }

    public abstract boolean canBounce();

    public void disable() {
        this.active = false;
    }

    /*
     * public void reset() {
     * this.active = true;
     * }
     */
}
