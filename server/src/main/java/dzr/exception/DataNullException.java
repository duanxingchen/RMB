package dzr.exception;

public class DataNullException extends RuntimeException {
    public DataNullException(String message) {
        super(message);
    }

    public DataNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
