package domain.exceptions;

public class DOPOException extends Exception {
    public static final String CANNOT_READ_FILE = "Cannot read the loaded game file.";
    public static final String CANNOT_WRITE_FILE = "Cannot write the saved game file.";

    public DOPOException(String message) {
        super(message);
    }

}