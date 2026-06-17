package ar.edu.unq.remiseria.exception;

public class ViajeNoEncontradoException extends DomainException {
    public ViajeNoEncontradoException() {
        super("Viaje no encontrado");
    }
}
