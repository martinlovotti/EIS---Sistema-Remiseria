package ar.edu.unq.remiseria.exception;

public class ViajeYaIniciadoException extends DomainException {
    public ViajeYaIniciadoException() {
        super("El viaje ya fue iniciado");
    }
}
