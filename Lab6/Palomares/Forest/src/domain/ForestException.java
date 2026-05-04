package domain;

public class ForestException extends Exception {
    public static String FILE_METHODS_NOT_IMPLEMENTED(String option, String fileName) {
        return String.format("Opción '%s' en construcción. Archivo '%s'", option, fileName);
    }

    public ForestException(String message) {
        super(message);
    }
}
