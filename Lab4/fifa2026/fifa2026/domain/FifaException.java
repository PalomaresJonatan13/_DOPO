package domain;

public class FifaException extends Exception {
    public static String IMPOSSIBLE = "";
    public static String VALUE_UNKNOWN = "";
    public static String MINUTES_UNKNOWN = "";

    public FifaException(String message) {
        super(message);
    }
}