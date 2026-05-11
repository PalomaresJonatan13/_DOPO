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
        super(x, y, 1.0, 1.0, Shape.SQUARE);
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public boolean isSafe() {
        return type == CellType.SAFE_ZONE;
    }
}
