package domain.enemies;

public class FastEnemy extends LinearEnemy {
    public FastEnemy(double x, double y, boolean horizontal, boolean toRight) {
        super(x, y, horizontal, toRight);
        this.speedX *= 2.0;
        this.speedY *= 2.0;
    }
}
