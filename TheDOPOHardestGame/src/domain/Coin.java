package domain;

public class Coin extends GameObject {
    public enum CoinType {
        NORMAL,
        RED,
        GREEN,
        BLUE
    }

    private CoinType type;

    public Coin(double x, double y, double width, double height) {
        this(x, y, width, height, CoinType.NORMAL);
    }

    public Coin(double x, double y, double width, double height, CoinType type) {
        super(x, y, width, height);
        this.type = (type == null)? CoinType.NORMAL : type;
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
