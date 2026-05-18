package ar.edu.unq.remiseria.exception;

public class ViajeNoPuedeFinalizarseException extends DomainException {
    public ViajeNoPuedeFinalizarseException() {
        super("El viaje no puede finalizarse");
    }
}
