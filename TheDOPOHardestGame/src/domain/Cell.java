package domain;

public class Cell extends GameObject {
    public enum CellType {
        NORMAL, START, FINAL, SAFE_ZONE
    }

    private CellType type;

    public Cell(double x, double y) {
        this(x, y, CellType.NORMAL);
    }

    public Cell(double x, double y, CellType type) {
        super(x, y, 1.0, 1.0);
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Cell{type=%s, x=%.2f, y=%.2f, active=%b}", type, centerX, centerY, active);
    }

    public CellType getType() {
        return type;
    }

    public boolean isSafe() {
        return type != CellType.NORMAL;
    }
}
