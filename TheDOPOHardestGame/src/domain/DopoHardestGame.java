package domain;

import java.util.*;
import java.io.File;

public class DopoHardestGame {
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>(); // for the presentation package, give it only the active coins
    private List<Cell> cells = new ArrayList<>();
    private List<SpecialObject> specialObjects = new ArrayList<>();
    private Cell checkpoint;

    private int width, height;
    private double timeRemaining;
    private boolean isPaused, isGameOver, isVictory;
    private GameMode gameMode;

    private int cellSize;
    private int playerSize;

    public enum GameMode { PLAYER, PvsP, PvsM };

    public DopoHardestGame(File savedGameFile) {
        if (savedGameFile == null) throw new IllegalArgumentException("The saved game file cannot be null.");
        GameFileLoader loader = new GameFileLoader(savedGameFile);

        this.players = Arrays.asList(loader.getPlayers());
        this.enemies = Arrays.asList(loader.getEnemies());
        this.coins = Arrays.asList(loader.getCoins());
        this.cells = Arrays.asList(loader.getCells());
        this.specialObjects = Arrays.asList(loader.getSpecialObjects());
        this.checkpoint = loader.getCheckpoint(); // by default it should be some Cell with type START

        this.width = loader.getWidth();
        this.height = loader.getHeight();
        this.gameMode = loader.getGameMode();
        this.timeRemaining = loader.getTimeRemaining();

        this.cellSize = loader.getCellSize();
        this.playerSize = loader.getPlayerSize();

        this.isPaused = false;
        this.isGameOver = false;
        this.isVictory = false;
    }

    public DopoHardestGame(File gameMap, GameMode gameMode, double timeLimit) {
        if (!validGameMode(gameMode)) throw new IllegalArgumentException("Invalid game mode: " + gameMode);
        if (timeLimit <= 0) throw new IllegalArgumentException("Time limit must be positive.");
        if (gameMap == null) throw new IllegalArgumentException("Game map cannot be null.");
        
        this(gameMap);
        this.gameMode = gameMode;
        this.timeRemaining = timeLimit;
        if (gameMode == GameMode.PLAYER) {
            this.placePlayer(true);
        } else {
            this.placePlayer(true);
            this.placePlayer(false);
        }
    }

    private static boolean validGameMode(GameMode gameMode) {
        return gameMode != null;
    }

    private void placePlayer(boolean start) {
        Cell.CellType cellType = start ? Cell.CellType.START : Cell.CellType.FINAL;
        for (Cell cell : this.cells) {
            if (cell.getType() == cellType) {
                this.players.add(new Player(cell.getX(), cell.getY(), this.playerSize, this.playerSize));
            }
        }
    }

    public void update() {
        if (this.isPaused || this.isGameOver || this.isVictory) return;

        if (this.gameMode == GameMode.PLAYER) {
            this.timeRemaining -= 1;
            if (this.timeRemaining <= 0) {
                this.isGameOver = true;
            }
        }

        for (Player player : this.players) {
            player.move();
        }

        for (Enemy enemy : this.enemies) {
            enemy.move();
            if (enemy.getX() <= 0 || enemy.getX() + enemy.getWidth() >= this.width) {
                enemy.bounceX();
            }
            if (enemy.getY() <= 0 || enemy.getY() + enemy.getHeight() >= this.height) {
                enemy.bounceY();
            }
        }

        this.checkCollisions();
    }

    private void checkCollisions() {
        for (Player player : this.players) {
            if (!player.isAlive()) continue;

            boolean inSafeZone = this.isPlayerInSafeZone(player);
            if (!inSafeZone) {
                for (Enemy enemy : this.enemies) {
                    if (enemy.isActive() && player.intersects(enemy)) {
                        this.handlePlayerDeath(player);
                        // break;
                    }
                }
            }

            // Check coins
            for (Coin coin : this.coins) {
                if (coin.isActive() && player.intersects(coin)) {
                    coin.disable();
                    this.applyCoinEffect(player, coin);
                }
            }

            // Check special objects
            for (SpecialObject specialObject : this.specialObjects) {
                if (specialObject.isActive() && player.intersects(specialObject)) {
                    this.applySpecialObjectEffect(player, specialObject);
                }
            }

            this.checkWinCondition(player);
        }

        if (this.gameMode != GameMode.PLAYER) {
            Player p1 = this.players.get(0);
            Player p2 = this.players.get(1);
            boolean p1Safe = this.isPlayerInSafeZone(p1);
            boolean p2Safe = this.isPlayerInSafeZone(p2);

            if (p1.intersects(p2)) {
                if (p1Safe && !p2Safe) {
                    this.handlePlayerDeath(p2);
                } else if (!p1Safe && p2Safe) {
                    this.handlePlayerDeath(p1);
                } else if (!p1Safe && !p2Safe) {
                    this.handlePlayerDeath(p1);
                    this.handlePlayerDeath(p2);
                }
            }
        }
    }

    private boolean isPlayerInSafeZone(Player player) {
        for (Cell cell : this.cells) {
            if (cell.isSafe() && player.intersects(cell)) {
                player.setCheckpoint(cell);
                return true;
            }
        }
        return false;
    }

    private void handlePlayerDeath(Player player) {
        player.die();
        if (player.getCoinCount() == 0) this.resetCoins();
        // this.resetEnemies();
    }

    private void resetCoins() {
        for (Coin coin : coins) {
            coin.reset();
        }
    }

    /* private void resetEnemies() {
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
    } */

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
        if (!specialObject.isActive()) return;

        if (specialObject.getType() == SpecialObject.SpecialObjectType.LIFE) {
            player.addExtraLife(specialObject);
        } else if (specialObject.getType() == SpecialObject.SpecialObjectType.BOMB) {
            for (Enemy enemy : enemies) {
                if (specialObject.intersects(enemy)) enemy.disable();
            }
            specialObject.disable();
            this.handlePlayerDeath(player);
        }
    }

    private void checkWinCondition(Player player) {
        if (player.getCoinCount() == this.coins.size()) {
            if (player.intersects(this.checkpoint)) {
                this.isVictory = true;
            }
        }
    }

    public void playerDirection(int playerIndex, Direction dir) {
        if (this.isPaused || this.isGameOver || this.isVictory) return;

        if (playerIndex < 0 || playerIndex >= players.size()) {
            throw new IllegalArgumentException("Invalid player index.");
        }
        if (dir == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }
        Player p = players.get(playerIndex);
        p.setDirection(dir.getDx(), dir.getDy()); // set direction NONE in the controller
    }

    public void togglePause() {
        this.isPaused = !this.isPaused;
    }

    public void terminateGame() {
        this.isGameOver = true;
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
}
