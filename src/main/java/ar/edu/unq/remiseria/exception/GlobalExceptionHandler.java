package ar.edu.unq.remiseria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ViajeNoPuedeCancelarseException.class)
    public ResponseEntity<String> handleViajeNoPuedeCancelarse(ViajeNoPuedeCancelarseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioConViajeSolicitadoException.class)
    public ResponseEntity<String> manejarUsuarioConViaje(UsuarioConViajeSolicitadoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NoHayChoferesException.class)
    public ResponseEntity<String> handleNoHayChoferes(NoHayChoferesException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
