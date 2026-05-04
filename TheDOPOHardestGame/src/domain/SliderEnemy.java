package domain;

public class SliderEnemy extends Enemy {
    public SliderEnemy(double x, double y, double width, double height, double speedY) {
        super(x, y, width, height, 0, speedY); // Only vertical movement
    }
}
