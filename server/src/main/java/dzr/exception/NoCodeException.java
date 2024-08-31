package dzr.exception;

public class NoCodeException extends RuntimeException {
    public NoCodeException(String message) {
        super(message);
    }

    public NoCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
