package domain;

public class FastEnemy extends Enemy {
    public FastEnemy(double x, double y, double width, double height, double speedX, double speedY) {
        super(x, y, width, height, speedX * 2.0, speedY * 2.0);
    }
}
