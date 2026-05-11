package domain;

public class SpecialObject extends GameObject {
    public enum SpecialObjectType {
        LIFE, BOMB
    }

    private static final double RADIUS = 0.3;

    private SpecialObjectType type;
    private boolean active;

    public SpecialObject(double x, double y, SpecialObjectType type) {
        super(x, y, 2 * RADIUS, 2 * RADIUS, Shape.CIRCLE);
        this.type = type;
        this.active = true;
    }

    public SpecialObjectType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void disable() {
        this.active = false;
    }
}
