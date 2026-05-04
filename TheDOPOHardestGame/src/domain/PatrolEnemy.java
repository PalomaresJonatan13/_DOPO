package domain;

public class PatrolEnemy extends Enemy {
    private double centerX, centerY;
    private double angle;
    private double radius;
    private double angularSpeed;

    public PatrolEnemy(double centerX, double centerY, double radius, double width, double height, double angularSpeed) {
        super(centerX, centerY - radius, width, height, 0, 0);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angularSpeed = angularSpeed;
        this.angle = 0;
    }

    @Override
    public void move() {
        angle += angularSpeed;
        this.x = centerX + Math.cos(angle) * radius;
        this.y = centerY + Math.sin(angle) * radius;
    }
}
