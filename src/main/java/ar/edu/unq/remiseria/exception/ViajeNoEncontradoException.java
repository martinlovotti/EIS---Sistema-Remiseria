package ar.edu.unq.remiseria.exception;

public class ViajeNoEncontradoException extends RuntimeException {
    public ViajeNoEncontradoException() {
        super("Viaje no encontrado");
    }
}
