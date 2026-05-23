package domain;

public class SpecialObject extends GameObject {
    public enum SpecialObjectType {
        LIFE, BOMB
    }

    private static final double SIDE_LENGTH = 0.5;

    private SpecialObjectType type;
    private boolean active;

    public SpecialObject(double x, double y, SpecialObjectType type) {
        super(x, y, SIDE_LENGTH, SIDE_LENGTH);
        this.type = type;
        this.active = true;
    }

    @Override
    public String toString() {
        return String.format(
            "SpecialObject{type=%s, x=%.2f, y=%.2f, active=%b}",
            type, centerX, centerY, active
        );
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
