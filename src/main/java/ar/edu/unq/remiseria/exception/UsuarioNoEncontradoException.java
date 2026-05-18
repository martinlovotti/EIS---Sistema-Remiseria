package ar.edu.unq.remiseria.exception;

public class UsuarioNoEncontradoException extends DomainException {
    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado");
    }
}

