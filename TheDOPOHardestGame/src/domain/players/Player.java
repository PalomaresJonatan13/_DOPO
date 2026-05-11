package domain.players;

import domain.*;

public class Player extends GameObject implements MovableObject {
    public enum PlayerType {
        DEFAULT, RED, GREEN, BLUE
    }

    private int deaths = 0;
    private double speedX, speedY, speed;
    private boolean isAlive = true;
    private int extraLives = 0;
    private double checkpointX, checkpointY;
    private int coinCount = 0;

    // State Pattern fields
    private PlayerType originalType;
    private PlayerState currentState;
    private static final double BASE_SPEED = 0.075;
    private static final double SIDE_LENGTH = 0.6;

    public Player(double x, double y) {
        this(x, y, PlayerType.DEFAULT);
    }

    public Player(double x, double y, PlayerType originalType) {
        super(x, y, SIDE_LENGTH, SIDE_LENGTH, Shape.SQUARE);
        this.checkpointX = x;
        this.checkpointY = y;

        this.speed = BASE_SPEED;
        this.originalType = originalType;

        this.changeSkin(originalType);
    }

    public void changeSkin(PlayerType type) {
        switch (type) {
            case DEFAULT -> this.currentState = new DefaultState();
            case RED -> this.currentState = new RedState();
            case GREEN -> this.currentState = new GreenState();
            case BLUE -> this.currentState = new BlueState();
            default -> { // otherwise (when it is null) do not change the skin.
                if (this.currentState == null)
                    this.currentState = new DefaultState();
            }
        }

        this.currentState.onEnterState(this);
    }

    public PlayerType getOriginalType() {
        return this.originalType;
    }

    public PlayerState getCurrentState() {
        return this.currentState;
    }

    public double getBaseSpeed() {
        return BASE_SPEED;
    }

    public double getSideLength() {
        return SIDE_LENGTH;
    }

    public void setWidth(double w) {
        this.width = w;
    }

    public void setHeight(double h) {
        this.height = h;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(double dx, double dy) {
        if (this.isAlive) {
            this.speedX = dx * speed;
            this.speedY = dy * speed;
        }
    }

    @Override
    public void move() {
        if (this.isAlive) {
            this.centerX += speedX;
            this.centerY += speedY;
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
        this.centerX = checkpointX;
        this.centerY = checkpointY;
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
        this.checkpointX = cell.getX();
        this.checkpointY = cell.getY();
    }

    public void increaseCoinCount() {
        this.coinCount++;
    }

    public int getCoinCount() {
        return this.coinCount;
    }
}
