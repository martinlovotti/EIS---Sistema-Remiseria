package ar.edu.unq.remiseria.exception;

public class ChoferNoEncontradoException extends RuntimeException {
    public ChoferNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}