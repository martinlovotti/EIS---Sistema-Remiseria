package ar.edu.unq.remiseria.exception;

public class ViajeNoPuedeInicializarseException extends RuntimeException {
    public ViajeNoPuedeInicializarseException() {
        super("El viaje no puede inicializarse porque no fua aceptado");
    }
}
