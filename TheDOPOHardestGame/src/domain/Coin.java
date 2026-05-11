package domain;

public class Coin extends GameObject {
    public enum CoinType {
        NORMAL, RED, GREEN, BLUE
    }

    private CoinType type;
    private static final double RADIUS = 0.25;

    public Coin(double x, double y) {
        this(x, y, CoinType.NORMAL);
    }

    public Coin(double x, double y, CoinType type) {
        super(x, y, 2 * RADIUS, 2 * RADIUS, Shape.CIRCLE);
        this.type = type;
    }

    public CoinType getType() {
        return this.type;
    }

    public void disable() {
        this.active = false;
    }

    public void reset() {
        this.active = true;
    }
}
