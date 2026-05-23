package domain;

import domain.DopoHardestGame.*;
import domain.enemies.*;
import domain.exceptions.*;
import domain.players.*;

import java.io.*;
import java.util.*;

public class GameFileHandler {
    private DopoHardestGame.GameMode gameMode;
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<SpecialObject> specialObjects = new ArrayList<>();
    private List<Cell> checkpoints = new ArrayList<>();
    private int width;
    private int height;
    private double timeRemaining;

    private static final Set<String> enemyTypes = Set.of("normal", "fast", "patrol", "slider");

    public GameFileHandler() { /* EMPTY */ }

    public void loadMap(File file) throws DOPOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            throw new DOPOException("Invalid map file. Only .txt files are admitted.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("Enemies: ")) {
                    this.parseEnemies(line);
                } else if (line.startsWith("Coins: ")) {
                    this.parseCoins(line);
                } else if (line.startsWith("Cells: ")) {
                    this.parseCells(line);
                } else if (line.startsWith("SpecialObjects: ")) {
                    this.parseSpecialObjects(line);
                } else if (line.startsWith("Checkpoints: ")) {
                    this.parseCheckpoints(line);
                } else if (line.startsWith("Dimensions: ")) {
                    this.parseDimensions(line);
                } else if (line.startsWith("TimeRemaining: ")) {
                    this.parseTimeRemaining(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new DOPOException(DOPOException.CANNOT_READ_FILE);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSerialized(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        if (!file.getName().toLowerCase().endsWith(".dopo")) {
            throw new IllegalArgumentException("Invalid saved game file. Only .dopo files are admitted.");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.gameMode = (DopoHardestGame.GameMode) ois.readObject();
            this.players = (List<Player>) ois.readObject();
            this.enemies = (List<Enemy>) ois.readObject();
            this.coins = (List<Coin>) ois.readObject();
            this.cells = (List<Cell>) ois.readObject();
            this.specialObjects = (List<SpecialObject>) ois.readObject();
            this.checkpoints = (List<Cell>) ois.readObject();
            this.width = ois.readInt();
            this.height = ois.readInt();
            this.timeRemaining = ois.readDouble();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(DOPOException.CANNOT_READ_FILE);
        }
    }

    public void exportGameFile(File file, DopoHardestGame game) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        if (!file.getName().toLowerCase().endsWith(".dopo")) {
            throw new IllegalArgumentException("Invalid saved game file. Only .dopo files are admitted.");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(game.getGameMode());
            oos.writeObject(new ArrayList<>(game.getPlayers()));
            oos.writeObject(new ArrayList<>(game.getEnemies()));
            oos.writeObject(new ArrayList<>(game.getCoins()));
            oos.writeObject(new ArrayList<>(game.getCells()));
            oos.writeObject(new ArrayList<>(game.getSpecialObjects()));
            oos.writeObject(new ArrayList<>(game.getCheckpoints()));
            oos.writeInt(game.getWidth());
            oos.writeInt(game.getHeight());
            oos.writeDouble(game.getTimeRemaining());
        } catch (IOException e) {
            throw new IllegalArgumentException(DOPOException.CANNOT_WRITE_FILE);
        }
    }

    private void parseEnemies(String line) throws DOPOException {
        String[] enemyTypesData = line.substring(9).toLowerCase().split(" "); // each has the info of some enemy type
        for (String enemyTypeDataString : enemyTypesData) {
            String[] enemyTypeData = enemyTypeDataString.split("_");
            if (enemyTypeData.length != 2)
                throw new DOPOException("Invalid map. Wrong format for enemies' data.");
            String enemyType = enemyTypeData[0];
            if (!enemyTypes.contains(enemyType))
                throw new DOPOException("Invalid map. Unknown enemy type: " + enemyType);

            String[] enemyTypeArgs = enemyTypeData[1].split("/");
            for (String enemyArgsString : enemyTypeArgs) {
                String[] enemyArgs = enemyArgsString.split(",");
                double x = Double.parseDouble(enemyArgs[0]);
                double y = Double.parseDouble(enemyArgs[1]);
                if (enemyType.equals("normal")) {
                    if (enemyArgs.length != 4)
                        throw new DOPOException("Invalid map. Wrong number of arguments for normal enemy. Got: "
                                + enemyArgs.length + ". Expected 4.");
                    boolean horizontal = enemyArgs[2].equals("t");
                    boolean toRightTop = enemyArgs[3].equals("t");
                    this.enemies.add(new NormalEnemy(x, y, horizontal, toRightTop));
                } else if (enemyType.equals("fast")) {
                    if (enemyArgs.length != 4)
                        throw new DOPOException("Invalid map. Wrong number of arguments for fast enemy. Got: "
                                + enemyArgs.length + ". Expected 4.");
                    boolean horizontal = enemyArgs[2].equals("t");
                    boolean toRightTop = enemyArgs[3].equals("t");
                    this.enemies.add(new FastEnemy(x, y, horizontal, toRightTop));
                } else if (enemyType.equals("patrol")) {
                    if (enemyArgs.length != 5)
                        throw new DOPOException("Invalid map. Wrong number of arguments for patrol enemy. Got: "
                                + enemyArgs.length + ". Expected 5.");
                    double radius = Double.parseDouble(enemyArgs[2]);
                    boolean clockwise = enemyArgs[3].equals("t");
                    int initialAngle = Integer.parseInt(enemyArgs[4]);
                    this.enemies.add(new PatrolEnemy(x, y, radius, clockwise, initialAngle));
                } else if (enemyType.equals("slider")) {
                    if (enemyArgs.length != 3)
                        throw new DOPOException("Invalid map. Wrong number of arguments for slider enemy. Got: "
                                + enemyArgs.length + ". Expected 3.");
                    boolean toTop = enemyArgs[2].equals("t");
                    this.enemies.add(new SliderEnemy(x, y, toTop));
                }
            }
        }
    }

    private void parseCoins(String line) throws DOPOException {
        String[] coinsData = line.substring(7).toLowerCase().split(" ");
        for (String coinTypeDataString : coinsData) {
            String[] coinTypeData = coinTypeDataString.split("_");
            if (coinTypeData.length != 2)
                throw new DOPOException("Invalid map. Wrong format for coins' data.");
            Coin.CoinType coinType = switch (coinTypeData[0]) {
                case "normal" -> Coin.CoinType.NORMAL;
                case "red" -> Coin.CoinType.RED;
                case "green" -> Coin.CoinType.GREEN;
                case "blue" -> Coin.CoinType.BLUE;
                default -> throw new DOPOException("Invalid map. Unknown coin type: " + coinTypeData[0]);
            };
            String[] coinTypeArgs = coinTypeData[1].split("/");
            for (String coinArgsString : coinTypeArgs) {
                String[] coinArgs = coinArgsString.split(",");
                if (coinArgs.length != 2)
                    throw new DOPOException("Invalid map. Wrong number of arguments for coin: " + coinTypeData[0]
                            + ". Got: " + coinArgs.length + ". Expected 2.");
                double x = Double.parseDouble(coinArgs[0]);
                double y = Double.parseDouble(coinArgs[1]);
                this.coins.add(new Coin(x, y, coinType));
            }
        }
    }

    private void parseCells(String line) throws DOPOException {
        String[] cellsData = line.substring(7).toLowerCase().split(" ");
        for (String cellTypeDataString : cellsData) {
            String[] cellTypeData = cellTypeDataString.split("_");
            if (cellTypeData.length != 2)
                throw new DOPOException("Invalid map. Wrong format for cell.");
            Cell.CellType cellType = switch (cellTypeData[0]) {
                case "normal" -> Cell.CellType.NORMAL;
                case "start" -> Cell.CellType.START;
                case "final" -> Cell.CellType.FINAL;
                case "safezone" -> Cell.CellType.SAFE_ZONE;
                default -> throw new DOPOException("Invalid map. Unknown cell type: " + cellTypeData[0]);
            };
            String[] cellTypeArgs = cellTypeData[1].split("/");
            for (String cellArgsString : cellTypeArgs) {
                String[] cellArgs = cellArgsString.split(",");
                if (cellArgs.length != 2)
                    throw new DOPOException("Invalid map. Wrong number of arguments for cell: " + cellTypeData[0]
                            + ". Got: " + cellArgs.length + ". Expected 2.");
                double x = Double.parseDouble(cellArgs[0]);
                double y = Double.parseDouble(cellArgs[1]);
                this.cells.add(new Cell(x, y, cellType));
            }
        }
    }

    private void parseSpecialObjects(String line) throws DOPOException {
        String[] specialObjectsData = line.substring(16).toLowerCase().split(" ");
        for (String specialObjectDataString : specialObjectsData) {
            String[] specialObjectTypeData = specialObjectDataString.split("_");
            if (specialObjectTypeData.length != 2)
                throw new DOPOException("Invalid map. Wrong format for special objects' data.");
            SpecialObject.SpecialObjectType specialObjectType = switch (specialObjectTypeData[0]) {
                case "life" -> SpecialObject.SpecialObjectType.LIFE;
                case "bomb" -> SpecialObject.SpecialObjectType.BOMB;
                default ->
                    throw new DOPOException("Invalid map. Unknown special object type: " + specialObjectTypeData[0]);
            };
            String[] specialObjectTypeArgs = specialObjectTypeData[1].split("/");
            for (String specialObjectArgsString : specialObjectTypeArgs) {
                String[] specialObjectArgs = specialObjectArgsString.split(",");
                if (specialObjectArgs.length != 2)
                    throw new DOPOException("Invalid map. Wrong number of arguments for special object: "
                            + specialObjectTypeData[0] + ". Got: " + specialObjectArgs.length + ". Expected 2.");
                double x = Double.parseDouble(specialObjectArgs[0]);
                double y = Double.parseDouble(specialObjectArgs[1]);
                this.specialObjects.add(new SpecialObject(x, y, specialObjectType));
            }
        }
    }

    private void parseCheckpoints(String line) throws DOPOException {
        String[] checkpointData = line.substring(12).toLowerCase().split("/");
        if (checkpointData.length == 0 || checkpointData.length > 2)
            throw new DOPOException("Invalid map. Wrong number of arguments for checkpoint.");
        for (int j = 0; j < checkpointData.length; j++) {
            String[] checkpointArgs = checkpointData[j].split(",");
            if (checkpointArgs.length != 2)
                throw new DOPOException("Invalid map. Wrong number of arguments for checkpoint. Got: "
                        + checkpointArgs.length + ". Expected 2.");
            double x = Double.parseDouble(checkpointArgs[0]);
            double y = Double.parseDouble(checkpointArgs[1]);
            this.checkpoints.add(new Cell(x, y, (j == 0) ? Cell.CellType.START : Cell.CellType.FINAL));
        }
    }

    private void parseDimensions(String line) throws DOPOException {
        String[] dimensionsData = line.substring(12).split(",");
        if (dimensionsData.length != 2)
            throw new DOPOException("Invalid map. Wrong number of arguments for dimensions.");
        int width = Integer.parseInt(dimensionsData[0]);
        int height = Integer.parseInt(dimensionsData[1]);
        if (width <= 0 || height <= 0)
            throw new DOPOException("Invalid map. Dimensions must be positive.");
        if (width > 24 || height > 14)
            throw new DOPOException("Invalid map. Dimensions must be less than or equal to 24x14.");
        this.width = width;
        this.height = height;
    }

    private void parseTimeRemaining(String line) throws DOPOException {
        String timeRemainingData = line.substring(15);
        if (timeRemainingData.isEmpty())
            throw new DOPOException("Invalid map. Wrong number of arguments for time remaining.");
        double timeRemaining = Double.parseDouble(timeRemainingData);
        if (timeRemaining <= 0 || timeRemaining > 180)
            throw new DOPOException("Invalid map. Time remaining must be between 0 and 180.");
        this.timeRemaining = timeRemaining;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(this.enemies);
    }

    public List<Coin> getCoins() {
        return new ArrayList<>(this.coins);
    }

    public List<Cell> getCells() {
        return new ArrayList<>(this.cells);
    }

    public List<SpecialObject> getSpecialObjects() {
        return new ArrayList<>(this.specialObjects);
    }

    public List<Cell> getCheckpoints() {
        return new ArrayList<>(this.checkpoints);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public double getTimeRemaining() {
        return this.timeRemaining;
    }
}
