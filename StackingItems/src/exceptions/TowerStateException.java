package exceptions;

public class TowerStateException extends IllegalArgumentException {
    public TowerStateException(String message) {
        super(message);
    }
}
