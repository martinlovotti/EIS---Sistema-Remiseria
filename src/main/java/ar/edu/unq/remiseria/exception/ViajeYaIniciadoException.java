package ar.edu.unq.remiseria.exception;

public class ViajeYaIniciadoException extends RuntimeException {
    public ViajeYaIniciadoException() {
        super("El viaje ya fue iniciado");
    }
}
