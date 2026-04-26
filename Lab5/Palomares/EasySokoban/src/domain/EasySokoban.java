package domain;

import java.util.*;

/**
 * Main domain model for an Easy Sokoban game board and interactions.
 */
public class EasySokoban {
    public static final char UP    = 'U';
    public static final char RIGHT = 'R';
    public static final char DOWN  = 'D';
    public static final char LEFT  = 'L';
    public static Character[] DIRECTIONS = {UP, RIGHT, DOWN, LEFT};

    private int width = 7;
    private int height = 9;
    private int[][] originalBoard;
    private Cell[][] board;

    private static final Map<String, Integer> types = Cell.types;
    public static final int EMPTY     = Cell.EMPTY;
    public static final int BLOCK     = Cell.BLOCK;
    public static final int BOX       = Cell.BOX;
    public static final int FIXED_BOX = Cell.FIXED_BOX;
    public static final int TARGET    = Cell.TARGET;
    public static final int PLAYER    = Cell.PLAYER;
    
    private static final int[][] defaultBoard;
    static {
        int em = EMPTY;
        int bl = BLOCK;
        int bo = BOX;
        int fi = FIXED_BOX;
        int ta = TARGET;
        int pl = PLAYER;
        defaultBoard = new int[][] {
            {bl, bl, bl, bl, bl, bl, bl},
            {bl, bl, bl, em, em, em, bl},
            {bl, fi, em, em, em, em, bl},
            {bl, bl, bl, em, em, fi, bl},
            {bl, ta, bl, em, em, em, bl},
            {bl, em, bl, em, fi, em, bl},
            {bl, bo, bo, ta, em, em, bl},
            {bl, em, pl, em, fi, em, bl},
            {bl, bl, bl, bl, bl, bl, bl}
        };
    }

    /**
     * Creates a game with default dimensions and random valid layout.
     */
    public EasySokoban() {
        this.createBoard();
    }

    /**
     * Creates a game with custom dimensions and random valid layout.
     *
     * @param width board width.
     * @param height board height.
     */
    public EasySokoban(int width, int height) {
        checkValidDimensions(width, height);
        this.width = width;
        this.height = height;
        this.createBoard();
    }

    /**
     * Creates a game directly from a board type matrix.
     *
     * @param board matrix with domain cell type ids.
     */
    private EasySokoban(int[][] board) {
        this.createFromBoard(board);
    }

    /**
     * Builds the predefined default game board.
     *
     * @return game instance initialized from the default board.
     */
    public static EasySokoban createDefaultSokoban() {
        checkValidDimensions(defaultBoard[0].length, defaultBoard.length);
        try {
            checkHasBorderAndCorrectNumberOfObjects(defaultBoard);
            return new EasySokoban(defaultBoard);
        } catch (SokobanException e) {
            return new EasySokoban();
        }
    }

    /**
     * Builds a game from a custom board after validating it.
     *
     * @param board matrix with domain cell type ids.
     * @return game instance initialized from the provided board.
     * @throws SokobanException when the board does not satisfy game constraints.
     */
    public static EasySokoban createSokobanFromBoard(int[][] board) throws SokobanException {
        checkValidDimensions(board[0].length, board.length);
        checkHasBorderAndCorrectNumberOfObjects(board);
        return new EasySokoban(board);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Creates a randomized valid board with borders, objects and player.
     */
    private void createBoard() { // throws SokobanException when exceeding some limit (better in the GUI)
        this.createEmptyBoard();

        for (int j=0; j<this.getCellsPerType(); j++) {
            this.assignRandomPosition(Cell.BLOCK);
            this.assignRandomPosition(Cell.BOX);
            this.assignRandomPosition(Cell.TARGET);
        }
        this.assignRandomPosition(Cell.PLAYER);
        this.originalBoard = this.getBoard();
    }

    /**
     * Initializes an empty board and surrounds it with block cells.
     */
    private void createEmptyBoard() {
        this.board = new Cell[this.height][this.width];

        for (int r=0; r<this.height; r++) {
            for (int c=0; c<this.width; c++) {
                int type = Cell.EMPTY;
                if (r==0 || r==this.height-1 || c==0 || c==this.width-1) {
                    type = Cell.BLOCK;
                }
                this.board[r][c] = Cell.getCell(this, r, c, type);
            }
        }
    }

    /**
     * Initializes this game from a board matrix.
     *
     * @param board matrix with domain cell type ids.
     */
    private void createFromBoard(int[][] board) {
        this.height = board.length;
        this.width = board[0].length;
        this.board = new Cell[this.height][this.width];

        for (int r=0; r<this.height; r++) {
            for (int c=0; c<this.width; c++) {
                int cellType = board[r][c];
                this.board[r][c] = Cell.getCell(this, r, c, cellType);
            }
        }
        this.originalBoard = this.getBoard();
    }

    /**
     * Places a cell of a given type in a random empty position.
     *
     * @param type cell type id to place.
     * @return created cell instance.
     */
    private Cell assignRandomPosition(int type) {
        Random random = new Random();
        int row = random.nextInt(0, this.height);
        int column = random.nextInt(0, this.width);

        while (!this.isCellEmptyOrNull(row, column)) {
            row = random.nextInt(0, this.height);
            column = random.nextInt(0, this.width);
        }
        Cell cell = Cell.getCell(this, row, column, type);
        this.setCell(cell);
        return cell;
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Validates that a board position is inside current bounds.
     *
     * @param row row index to validate.
     * @param column column index to validate.
     */
    void checkValidPosition(int row, int column) {
        if (!(0<=row && row<this.height) && (0<=column && column<this.width)) {
            throw new IllegalArgumentException(SokobanException.INVALID_POSITION);
        }
    }

    /**
     * Returns the cell at a board position.
     *
     * @param row row index.
     * @param column column index.
     * @return cell at the requested position.
     */
    Cell getCell(int row, int column) {
        this.checkValidPosition(row, column);
        return this.board[row][column];
    }

    /**
     * Sets a board cell at its current row/column.
     *
     * @param cell cell to place.
     */
    void setCell(Cell cell) {
        int row = cell.getRow();
        int column = cell.getColumn();
        this.checkValidPosition(row, column);
        this.board[row][column] = cell;
    }

    /**
     * Checks whether a board position is empty or not initialized yet.
     *
     * @param row row index.
     * @param column column index.
     * @return {@code true} when the position has no cell or an empty cell.
     */
    boolean isCellEmptyOrNull(int row, int column) {
        this.checkValidPosition(row, column);
        Cell cell = this.board[row][column];
        return cell == null || cell.getType() == Cell.EMPTY;
    }

    /**
     * Returns the player cell for this game instance.
     *
     * @return player cell.
     */
    private Cell getPlayerCell() {
        return PlayerCell.getCell(this);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Counts how many boxes are already placed in targets.
     *
     * @return number of fixed boxes.
     */
    public int boxesInTarget() {
        int targets = 0;
        for (int r=0; r<this.height; r++) {
            for (int c=0; c<this.width; c++) {
                int cellType = this.getCell(r, c).getType();
                if (cellType==Cell.FIXED_BOX) targets++;
            }
        }
        return targets;
    }

    /**
     * Moves the player up.
     *
     * @throws SokobanException when movement is not possible.
     */
    public void moveUp() throws SokobanException {
        this.move(UP);
    }

    /**
     * Moves the player right.
     *
     * @throws SokobanException when movement is not possible.
     */
    public void moveRight() throws SokobanException {
        this.move(RIGHT);
    }

    /**
     * Moves the player down.
     *
     * @throws SokobanException when movement is not possible.
     */
    public void moveDown() throws SokobanException {
        this.move(DOWN);
    }

    /**
     * Moves the player left.
     *
     * @throws SokobanException when movement is not possible.
     */
    public void moveLeft() throws SokobanException {
        this.move(LEFT);
    }

    /**
     * Moves the player in a specific direction.
     *
     * @param direction one of {@link #UP}, {@link #RIGHT}, {@link #DOWN}, {@link #LEFT}.
     * @throws SokobanException when movement is not possible.
     */
    public void move(char direction) throws SokobanException {
        checkValidDirection(direction);
        this.getPlayerCell().move(direction);
    }

    /**
     * Returns board width.
     *
     * @return board width.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns board height.
     *
     * @return board height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the expected count per object type for a board size.
     *
     * @return number of blocks/boxes/targets to place.
     */
    public int getCellsPerType() {
        return (int) (0.1f*this.width*this.height);
    }

    /**
     * Returns the current board encoded as type ids.
     *
     * @return matrix of cell types.
     */
    public int[][] getBoard() {
        int[][] board = new int[this.height][this.width];
        for (int r=0; r<this.height; r++) {
            for (int c=0; c<this.width; c++) {
                board[r][c] = this.board[r][c].getType();
            }
        }
        return board;
    }

    /**
     * Returns current player coordinates.
     *
     * @return map with {@code row} and {@code column}.
     */
    public Map<String, Integer> getPlayerPosition() {
        return this.getPlayerCell().getPosition();
    }

    /**
     * Restores the game to its initial board state.
     */
    public void reset() {
        this.createFromBoard(this.originalBoard);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns available symbolic cell types.
     *
     * @return map from type names to type ids.
     */
    public static Map<String, Integer> getTypes() {
        return types;
    }

    /**
     * Returns the predefined default board.
     *
     * @return default board matrix.
     */
    public static int[][] getDefaultBoard() {
        return defaultBoard;
    }

    /**
     * Validates a movement direction character.
     *
     * @param direction direction to validate.
     */
    public static void checkValidDirection(char direction) {
        if (!Arrays.asList(EasySokoban.DIRECTIONS).contains(direction)) {
            throw new IllegalArgumentException(SokobanException.INVALID_DIRECTION);
        }
    }

    /**
     * Validates minimal board dimensions.
     *
     * @param width board width.
     * @param height board height.
     */
    private static void checkValidDimensions(int width, int height) {
        if (!(width >= 4 && height >= 4)) {
            throw new IllegalArgumentException(SokobanException.INVALID_DIMENSIONS);
        }
    }

    /**
     * Validates border and object counts for a board matrix.
     *
     * @param board matrix to validate.
     * @return number of fixed boxes found in targets.
     * @throws SokobanException when the board is invalid.
     */
    public static int checkHasBorderAndCorrectNumberOfObjects(int[][] board) throws SokobanException {
        int width = board[0].length;
        int height = board.length;
        int correctNumber = (int) (0.1f*width*height);
        boolean isCorrect = true;
        HashMap<Integer, Integer> counts = new HashMap<>(Map.of(
            Cell.EMPTY, 0, Cell.BLOCK, 0, Cell.BOX, 0, Cell.FIXED_BOX, 0, Cell.TARGET, 0, Cell.PLAYER, 0
        ));

        for (int r=0; r<height && isCorrect; r++) {
            for (int c=0; c<width && isCorrect; c++) {
                if (r==0 || r==height-1 || c==0 || c==width-1) {
                    isCorrect = board[r][c] == Cell.BLOCK;
                } else {
                    isCorrect = Cell.isAValidType(board[r][c]);
                    counts.put(board[r][c], counts.get(board[r][c])+1);
                }
            }
        }

        int countBoxesInTarget = counts.get(Cell.FIXED_BOX);
        int countNonEmptyInnerCells = (3*correctNumber-countBoxesInTarget) + 1;
        if (
            isCorrect &&
            (counts.get(Cell.EMPTY)  == (width-2)*(height-2) - countNonEmptyInnerCells)  &&
            (counts.get(Cell.BLOCK)  == correctNumber)                      &&
            (counts.get(Cell.BOX)    == correctNumber - countBoxesInTarget) &&
            (counts.get(Cell.TARGET) == correctNumber - countBoxesInTarget) &&
            (counts.get(Cell.PLAYER) == 1)
        ) return countBoxesInTarget;
        else throw new SokobanException(SokobanException.INVALID_BOARD);
    }
}
