package espe.edu.tienda_ropa.web.advice;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
