package ar.edu.unq.remiseria.exception;

public class DomainException extends RuntimeException {
    DomainException(String msg) {
        super(msg);
    }
}
