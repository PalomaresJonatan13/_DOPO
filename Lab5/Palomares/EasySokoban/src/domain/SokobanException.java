package domain;

/**
 * Domain-specific checked exception for invalid Sokoban operations.
 */
public class SokobanException extends Exception {
    public static String MOVE_FIXED_CELL = "Cannot move a fixed cell.";
    public static String IRREPLACEABLE_CELL = "Cannot replace a cell that is not replaceable.";
    public static String IMPOSSIBLE_MOVE = "Given the current position of the boxes and other objects, its impossible to move in the given direction.";
    public static String INVALID_TARGET_REPLACEMENT = "Targets can only be replaced with boxes.";
    public static String INVALID_BOARD = "Invalid board given.";
    public static String INVALID_DIMENSIONS = "Both width and height of the board must be at least 3.";
    public static String INVALID_DIRECTION = "Invalid direction given.";
    public static String INVALID_POSITION = "Row or column number out of bounds.";

    /**
     * Creates an exception with the given detail message.
     *
     * @param message reason for the failure.
     */
    public SokobanException(String message) {
        super(message);
    }
}
