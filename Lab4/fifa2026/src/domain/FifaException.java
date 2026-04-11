package domain;

public class FifaException extends Exception {
    public static String IMPOSSIBLE = "Impossible";
    public static String VALUE_UNKNOWN = "Value unknown";
    public static String MINUTES_UNKNOWN = "Minutes unknown";

    public FifaException(String message) {
        super(message);
    }
}