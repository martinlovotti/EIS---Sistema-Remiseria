package ar.edu.unq.remiseria.exception;

public class NoHayChoferesException extends RuntimeException {
    public NoHayChoferesException() {
        super("No hay choferes con viajes asignados");
    }
}
