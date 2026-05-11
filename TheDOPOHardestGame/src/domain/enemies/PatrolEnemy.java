package domain.enemies;

public class PatrolEnemy extends Enemy {
    private double circleCenterX, circleCenterY;
    private double angle;
    private double radius;
    private double angularSpeed;

    public PatrolEnemy(double circleCenterX, double circleCenterY, double radius, boolean clockwise, int initialAngle) {
        double angle = initialAngle * Math.PI / 180;
        super(circleCenterX + Math.cos(angle) * radius, circleCenterY + Math.sin(angle) * radius);
        this.circleCenterX = circleCenterX;
        this.circleCenterY = circleCenterY;
        this.radius = radius;
        this.angularSpeed = (clockwise ? 1 : -1) * 0.03;
        this.angle = angle;
    }

    @Override
    public boolean canBounce() {
        return false;
    }

    @Override
    public void move() {
        this.angle += this.angularSpeed;
        this.centerX = this.circleCenterX + Math.cos(this.angle) * this.radius;
        this.centerY = this.circleCenterY + Math.sin(this.angle) * this.radius;
    }
}
