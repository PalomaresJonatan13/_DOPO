package domain;

public class Cell extends GameObject {
    public enum CellType { START, FINAL, SAFE_ZONE }
    private CellType type;

    public Cell(double x, double y, double width, double height, CellType type) {
        super(x, y, width, height);
        this.type = type;
    }

    public CellType getType() { return type; }
    public boolean isSafe() { return type == CellType.SAFE_ZONE; }
}
