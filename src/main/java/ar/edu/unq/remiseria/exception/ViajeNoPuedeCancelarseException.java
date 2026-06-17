package ar.edu.unq.remiseria.exception;

import ar.edu.unq.remiseria.modelo.EstadoViaje;

public class ViajeNoPuedeCancelarseException extends DomainException {
    public ViajeNoPuedeCancelarseException(EstadoViaje estadoViaje) {
        super("El viaje no puede cancelarse porque está %s".formatted(estadoViaje));
    }
}
