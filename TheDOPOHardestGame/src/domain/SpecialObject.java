package domain;

public class SpecialObject extends GameObject {
    public enum SpecialObjectType {LIFE, BOMB}
    private SpecialObjectType type;
    private boolean active;

    public SpecialObject(double x, double y, double width, double height, SpecialObjectType type) {
        super(x, y, width, height);
        this.type = type;
        this.active = true;
    }

    public SpecialObjectType getType() { return type; }

    public boolean isActive() { return active; }

    public void disable() {
        this.active = false;
    }
}
