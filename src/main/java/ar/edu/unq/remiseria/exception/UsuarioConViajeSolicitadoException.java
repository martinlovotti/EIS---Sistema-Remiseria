package ar.edu.unq.remiseria.exception;

public class UsuarioConViajeSolicitadoException extends DomainException {
    public UsuarioConViajeSolicitadoException() {
        super("El cliente ya tiene un viaje solicitado");
    }
}
