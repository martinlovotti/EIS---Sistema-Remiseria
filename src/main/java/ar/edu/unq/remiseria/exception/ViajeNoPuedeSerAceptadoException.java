package ar.edu.unq.remiseria.exception;

public class ViajeNoPuedeSerAceptadoException extends DomainException {
    public ViajeNoPuedeSerAceptadoException() {
        super("El viaje está solicitado o el chofer ya tiene un viaje asignado");
    }
}
