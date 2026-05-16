package ar.edu.unq.remiseria.exception;

public class ChoferNoEncontradoException extends RuntimeException {
    public ChoferNoEncontradoException() {
        super("Chofer no encontrado");
    }
}