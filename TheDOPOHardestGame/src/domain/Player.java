package domain;

public class Player extends GameObject implements MovableObject {
    public enum PlayerType { DEFAULT, RED, BLUE, GREEN }

    private int deaths;
    private double speedX;
    private double speedY;
    private double speedLimit;
    private boolean isAlive;
    private int extraLives;
    private double initialX, initialY;
    private int coinCount;

    // State Pattern fields
    private PlayerType originalType;
    private PlayerState currentState;
    private double baseWidth;
    private double baseHeight;
    private double baseSpeedLimit;

    public Player(double x, double y, double width, double height) {
        this(x, y, width, height, PlayerType.DEFAULT);
    }

    public Player(double x, double y, double width, double height, PlayerType originalType) {
        super(x, y, width, height);
        this.initialX = x;
        this.initialY = y;
        this.speedLimit = 1;
        this.deaths = 0;
        this.isAlive = true;
        this.extraLives = 0;
        this.coinCount = 0;

        this.baseWidth = width;
        this.baseHeight = height;
        this.baseSpeedLimit = speedLimit;
        this.originalType = originalType;

        this.changeSkin(originalType);
    }

    public void changeSkin(PlayerType type) {
        switch (type) {
            case DEFAULT:
                this.currentState = new DefaultState();
                break;
            case RED:
                this.currentState = new RedState();
                break;
            case GREEN:
                this.currentState = new GreenState();
                break;
            case BLUE:
                this.currentState = new BlueState();
                break;
            default: // otherwise (when it is null) do not change the skin.
                if (this.currentState == null) {
                    this.currentState = new DefaultState();
                }
                break;
        }

        this.currentState.onEnterState(this);
    }

    public PlayerType getOriginalType() {
        return this.originalType;
    }

    public double getBaseWidth() { return this.baseWidth; }
    public double getBaseHeight() { return this.baseHeight; }
    public double getBaseSpeedLimit() { return this.baseSpeedLimit; }
    
    public void setWidth(double w) { this.width = w; }
    public void setHeight(double h) { this.height = h; }
    public void setSpeedLimit(double speedLimit) { this.speedLimit = speedLimit; }

    public void setDirection(double dx, double dy) {
        if (this.isAlive) {
            this.speedX = dx * speedLimit;
            this.speedY = dy * speedLimit;
        }
    }

    @Override
    public void move() {
        if (this.isAlive) {
            this.x += speedX;
            this.y += speedY;
        }
    }

    public void die() {
        if (this.isAlive) {
            this.currentState.handleDie(this);
        }
    }

    public void performDie() {
        this.isAlive = false;
        if (this.extraLives > 0) { // the skin does not change
            this.extraLives--; // the player might not have the time to separate from the enemy
        } else {
            this.deaths++;
            this.changeSkin(this.originalType);
            this.coinCount = 0;
        }
        this.currentState.handleResetPosition(this);
    }

    public void performResetPosition() {
        this.x = initialX;
        this.y = initialY;
        this.isAlive = true;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void addExtraLife(SpecialObject life) {
        this.extraLives++;
        life.disable();
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setCheckpoint(Cell cell) {
        this.initialX = cell.getX();
        this.initialY = cell.getY();
    }

    public void increaseCoinCount() {
        this.coinCount++;
    }

    public int getCoinCount() {
        return this.coinCount;
    }
}
