package domain;
import domain.exceptions.*;

import java.io.*;
import java.util.*;

public class GameFileLoader {
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<SpecialObject> specialObjects = new ArrayList<>();
    private Cell checkpoint = null;
    
    private int width;
    private int height;
    private DopoHardestGame.GameMode gameMode;
    private double timeRemaining;

    private int cellSize;
    private int playerSize;

    public GameFileLoader(File file) {
        loadFromFile(file);
    }

    private void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // if (line.startsWith("Players:")) { ... }
                // else if (line.startsWith("Enemies:")) { ... }
                // else if (line.startsWith("Coins:")) { ... }
                // else if (line.startsWith("Cells:")) { ... }
                // else if (line.startsWith("SpecialObjects:")) { ... }
                // else if (line.startsWith("Checkpoint:")) { ... }
                // else if (line.startsWith("Width:")) { this.width = Integer.parseInt(...); }
                // else if (line.startsWith("Height:")) { this.height = Integer.parseInt(...); }
                // else if (line.startsWith("GameMode:")) { this.gameMode = ...; }
                // else if (line.startsWith("TimeRemaining:")) { this.timeRemaining = ...; }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(DOPOException.CANNOT_READ_FILE);
        }
    }

    // Returns the lists as arrays, per the requirements
    public Player[] getPlayers() {
        return this.players.toArray(new Player[0]);
    }

    public Enemy[] getEnemies() {
        return this.enemies.toArray(new Enemy[0]);
    }

    public Coin[] getCoins() {
        return this.coins.toArray(new Coin[0]);
    }

    public Cell[] getCells() {
        return this.cells.toArray(new Cell[0]);
    }

    public SpecialObject[] getSpecialObjects() {
        return this.specialObjects.toArray(new SpecialObject[0]);
    }

    public Cell getCheckpoint() {
        return this.checkpoint;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public DopoHardestGame.GameMode getGameMode() {
        return this.gameMode;
    }

    public double getTimeRemaining() {
        return this.timeRemaining;
    }

    public int getCellSize() {
        return this.cellSize;
    }

    public int getPlayerSize() {
        return this.playerSize;
    }
}
