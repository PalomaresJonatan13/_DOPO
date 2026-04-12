package domain;

public class FifaException extends Exception {
    public static String IMPOSSIBLE = "Impossible";
    public static String VALUE_UNKNOWN = "Value unknown";
    public static String MINUTES_UNKNOWN = "Minutes unknown";
    public static String NAME_ALREADY_USED = "Name of the participant was already used";
    public static String INVALID_NATURAL_VALUE = "Invalid value, it should represent a natural number";
    public static String INVALID_PLAYER_FOR_TEAM = "One of the team players listed does not exist, and therefore, the team cannot be created.";

    public FifaException(String message) {
        super(message);
    }
}