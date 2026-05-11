package ar.edu.unq.remiseria.exception;

public class ViajeNoPuedeCancelarseException extends RuntimeException {
    public ViajeNoPuedeCancelarseException() {
        super("El viaje no puede cancelarse porque ya fue iniciado");
    }
}
