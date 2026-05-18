package ar.edu.unq.remiseria.exception;

public class ChoferNoEncontradoException extends DomainException {
    public ChoferNoEncontradoException() {
        super("Chofer no encontrado");
    }
}