package espe.edu.tienda_ropa.web.advice;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
