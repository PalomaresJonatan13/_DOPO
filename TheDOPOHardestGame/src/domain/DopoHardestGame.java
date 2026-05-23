package domain;

import domain.enemies.*;
import domain.exceptions.*;
import domain.players.*;

import java.util.*;
import java.io.*;

public class DopoHardestGame implements Serializable {
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<SpecialObject> specialObjects = new ArrayList<>();
    private List<Cell> checkpoints = new ArrayList<>();

    private int width, height;
    private double timeRemaining;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isVictory = false;
    private List<Player> winners = new ArrayList<>();
    private GameMode gameMode;

    public enum GameMode {
        PLAYER, PvsP, PvsM
    };

    public DopoHardestGame(File savedGameFile) { // .dopo
        if (savedGameFile == null)
            throw new IllegalArgumentException("The saved game file cannot be null.");
        GameFileHandler loader = new GameFileHandler();
        loader.loadSerialized(savedGameFile);

        this.players = loader.getPlayers();
        this.enemies = loader.getEnemies();
        this.coins = loader.getCoins();
        this.cells = loader.getCells();
        this.specialObjects = loader.getSpecialObjects();
        this.checkpoints = loader.getCheckpoints();

        this.width = loader.getWidth();
        this.height = loader.getHeight();
        this.gameMode = loader.getGameMode();
        this.timeRemaining = loader.getTimeRemaining();
    }

    public DopoHardestGame(File gameMap, GameMode gameMode) throws DOPOException { // .txt
        this(gameMap, gameMode, Player.PlayerType.DEFAULT, Player.PlayerType.DEFAULT);
    }

    public DopoHardestGame(File gameMap, GameMode gameMode, Player.PlayerType playerType1, Player.PlayerType playerType2) throws DOPOException { // .txt
        if (gameMode == null)
            throw new IllegalArgumentException("The game mode cannot be null.");
        if (gameMap == null)
            throw new IllegalArgumentException("Game map cannot be null.");

        this.loadMap(gameMap);
        this.gameMode = gameMode;
        Cell checkpoint;
        if (gameMode == GameMode.PLAYER) {
            checkpoint = this.checkpoints.get(0);
            this.players.add(new Player(checkpoint.getCenterX(), checkpoint.getCenterY(), playerType1, "P1"));
        } else {
            checkpoint = this.checkpoints.get(0);
            this.players.add(new Player(checkpoint.getCenterX(), checkpoint.getCenterY(), playerType1, "P1"));
            checkpoint = this.checkpoints.get(1);
            this.players.add(new Player(checkpoint.getCenterX(), checkpoint.getCenterY(), playerType2, "P2"));
        }
    }

    private void loadMap(File gameMap) throws DOPOException { // .txt
        if (gameMap == null)
            throw new IllegalArgumentException("The file containing the map cannot be null.");
        GameFileHandler loader = new GameFileHandler();
        loader.loadMap(gameMap);

        this.enemies = loader.getEnemies();
        this.coins = loader.getCoins();
        this.cells = loader.getCells();
        this.specialObjects = loader.getSpecialObjects();
        this.checkpoints = loader.getCheckpoints();

        this.width = loader.getWidth();
        this.height = loader.getHeight();
        this.timeRemaining = loader.getTimeRemaining();
    }

    public void saveGame(File file) {
        if (file == null)
            throw new IllegalArgumentException("The saved game file cannot be null.");
        GameFileHandler handler = new GameFileHandler();
        handler.exportGameFile(file, this);
    }

    public void update() {
        if (this.isPaused || this.isGameOver || this.isVictory)
            return;

        this.timeRemaining -= 0.02;
        if (this.timeRemaining <= 0)
            this.isGameOver = true;

        for (Player player : this.players) {
            double oldCenterX = player.getCenterX();
            double oldCenterY = player.getCenterY();
            player.move();
            // Prevent moving out of the cells bounds
            if (!isRectInCells(player)) {
                player.setCenterX(oldCenterX);
                player.setCenterY(oldCenterY);
            }
        }

        for (Enemy enemy : this.enemies) {
            enemy.move();
            if (enemy.canBounce()) {
                Bouncable bouncable = (Bouncable) enemy;
                if (!isRectInCells(enemy)) {
                    bouncable.bounce();
                }
            }
        }

        this.checkCollisions();
    }

    private void checkCollisions() {
        for (Player player : this.players) {
            if (!player.isAlive())
                continue;

            boolean inSafeZone = this.isPlayerInSafeZone(player);
            if (!inSafeZone) {
                for (Enemy enemy : this.enemies) {
                    if (enemy.isActive() && player.intersects(enemy)) {
                        this.handlePlayerDeath(player);
                    }
                }
            }

            for (Coin coin : this.coins) {
                if (coin.isActive() && player.intersects(coin)) {
                    this.applyCoinEffect(player, coin);
                    coin.disable();
                }
            }

            for (SpecialObject specialObject : this.specialObjects) {
                if (specialObject.isActive() && player.intersects(specialObject)) {
                    this.applySpecialObjectEffect(player, specialObject);
                }
            }

            this.checkWinCondition(player);
        }

        if (this.gameMode != GameMode.PLAYER) {
            Player p1 = this.players.get(0);
            Player p2 = this.players.size() > 1 ? this.players.get(1) : null;
            if (p2 != null) {
                boolean p1Safe = this.isPlayerInSafeZone(p1);
                boolean p2Safe = this.isPlayerInSafeZone(p2);

                if (p1.intersects(p2)) {
                    if (!p1Safe)
                        this.handlePlayerDeath(p1);
                    if (!p2Safe)
                        this.handlePlayerDeath(p2);
                }
            }
        }
    }

    private boolean isPlayerInSafeZone(Player player) {
        for (Cell cell : this.cells) {
            boolean isSafeForPlayer = false;
            if (cell.getType() != Cell.CellType.NORMAL) {
                isSafeForPlayer = true;
            }

            if (isSafeForPlayer && player.intersects(cell)) {
                player.setCheckpoint(cell);
                return true;
            }
        }
        return false;
    }

    private void handlePlayerDeath(Player player) {
        player.die();
        if (player.getCoinCount() == 0)
            this.resetCoins();
        // this.resetEnemies();
    }

    private void resetCoins() {
        for (Coin coin : coins) {
            coin.reset();
        }
    }

    private void applyCoinEffect(Player player, Coin coin) {
        if (!coin.isActive()) return;

        if (coin.getType() == Coin.CoinType.NORMAL) {
            player.changeSkin(null);
        } else if (coin.getType() == Coin.CoinType.RED) {
            player.changeSkin(Player.PlayerType.RED);
        } else if (coin.getType() == Coin.CoinType.GREEN) {
            player.changeSkin(Player.PlayerType.GREEN);
        } else if (coin.getType() == Coin.CoinType.BLUE) {
            player.changeSkin(Player.PlayerType.BLUE);
        }
        player.increaseCoinCount();
    }

    private void applySpecialObjectEffect(Player player, SpecialObject specialObject) {
        if (!specialObject.isActive())
            return;

        if (specialObject.getType() == SpecialObject.SpecialObjectType.LIFE) {
            player.addExtraLife(specialObject);
        } else if (specialObject.getType() == SpecialObject.SpecialObjectType.BOMB) {
            for (Enemy enemy : enemies) {
                if (specialObject.intersects(enemy))
                    enemy.disable();
            }
            specialObject.disable();
            this.handlePlayerDeath(player);
        }
    }

    private void checkWinCondition(Player player) {
        Cell.CellType targetType = Cell.CellType.FINAL;
        if (this.players.indexOf(player) == 1) { // Player 2 (P2)
            targetType = Cell.CellType.START;
        }
        for (Cell cell : this.cells) {
            if (cell.getType() == targetType &&
                    player.intersects(cell) &&
                    this.allCoinsCollected()) {
                this.isVictory = true;
                if (!this.winners.contains(player)) {
                    this.winners.add(player);
                }
            }
        }
    }

    public boolean allCoinsCollected() {
        return this.coins.stream().allMatch(coin -> !coin.isActive());
    }

    public void setPlayerDirection(int playerIndex, int dx, int dy) {
        if (this.isPaused || this.isGameOver || this.isVictory)
            return;

        if (playerIndex < 0 || playerIndex >= players.size())
            throw new IllegalArgumentException("Invalid player index.");

        int j = 0;
        Player p = null;
        for (Player pl : players) {
            if (j == playerIndex) {
                p = pl;
                break;
            }
            j++;
        }
        if (p != null) p.setDirection(dx, dy);
    }

    private boolean isRectInCells(GameObject object) {
        double eps = 0.0001;
        double rectX = object.getX();
        double rectY = object.getY();
        double rectW = object.getWidth();
        double rectH = object.getHeight();

        return isPointInCells(rectX + eps, rectY + eps) && // left top
                isPointInCells(rectX + rectW - eps, rectY + eps) && // right top
                isPointInCells(rectX + eps, rectY + rectH - eps) && // left bottom
                isPointInCells(rectX + rectW - eps, rectY + rectH - eps); // right bottom
    }

    private boolean isPointInCells(double px, double py) {
        for (Cell cell : this.cells) {
            if (cell.getX() <= px && px <= cell.getX() + cell.getWidth() &&
                    cell.getY() <= py && py <= cell.getY() + cell.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public void togglePause() {
        this.isPaused = !this.isPaused;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public boolean isVictory() {
        return this.isVictory;
    }

    public double getTimeRemaining() {
        return this.timeRemaining;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(this.enemies);
    }

    public List<Coin> getCoins() {
        return Collections.unmodifiableList(this.coins);
    }

    public List<Cell> getCells() {
        return Collections.unmodifiableList(this.cells);
    }

    public List<Cell> getCheckpoints() {
        return Collections.unmodifiableList(this.checkpoints);
    }

    public List<SpecialObject> getSpecialObjects() {
        return Collections.unmodifiableList(this.specialObjects);
    }

    public List<Player> getWinners() {
        return Collections.unmodifiableList(this.winners);
    }
}
