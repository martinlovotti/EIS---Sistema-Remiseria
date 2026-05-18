package ar.edu.unq.remiseria.exception;

public class ViajeNoPuedeInicializarseException extends DomainException {
    public ViajeNoPuedeInicializarseException() {
        super("El viaje no puede inicializarse porque no esta aceptado");
    }
}
