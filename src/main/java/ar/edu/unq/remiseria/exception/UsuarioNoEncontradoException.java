package ar.edu.unq.remiseria.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

