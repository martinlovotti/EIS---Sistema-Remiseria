package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeFinalizarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeInicializarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerCalificadoException;
import lombok.*;

import java.time.LocalDateTime;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.*;

@Getter
@Setter
@NoArgsConstructor
public class Viaje {
    private Long id;

    private String origen;
    private String destino;

    private Double precioFinal;
    private Double kilometros;

    private EstadoViaje estadoViaje;

    private Usuario cliente;
    private Chofer chofer;

    private Double calificacion;
    private LocalDateTime fechaCreacion;

    public Viaje(Usuario cliente, Chofer chofer) {
        this.cliente = cliente;
        this.chofer = chofer;
        this.estadoViaje = PENDIENTE;
        this.calificacion = null;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Viaje(Usuario cliente, String origen, String destino) {
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.estadoViaje = PENDIENTE;
        this.calificacion = null;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Viaje(Usuario cliente, String origen, String destino, Double km) {
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.kilometros = km;
        this.estadoViaje = PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Viaje(Usuario cliente, String origen, String destino, Double km, Double precioFinal) {
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.kilometros = km;
        this.precioFinal = precioFinal;
        this.estadoViaje = PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void cancelar() {
        if (!(estadoViaje == PENDIENTE || estadoViaje == ACEPTADO)) {
            throw new ViajeNoPuedeCancelarseException(estadoViaje);
        }

        cliente.setViajeActual(null);

        this.setEstadoViaje(CANCELADO);
    }

    public boolean estaSolicitado() {
        return estadoViaje == EstadoViaje.PENDIENTE;
    }

    public boolean estaEnCurso() {
        return estadoViaje == EstadoViaje.EN_CURSO;
    }

    public void finalizarViaje() {
        if (this.estaEnCurso()) {
            setEstadoViaje(FINALIZADO);
            cliente.setViajeActual(null);
            chofer.setViajeActual(null);
        }else{
            throw new ViajeNoPuedeFinalizarseException();
        }
    }

    public boolean estaAceptado() {
        return estadoViaje == EstadoViaje.ACEPTADO;
    }

    public void inicializarViaje() {
        if (this.estaAceptado()) {
            setEstadoViaje(EN_CURSO);
        } else {
            throw new ViajeNoPuedeInicializarseException();
        }
    }

    public void calificar(Long usuarioId, Double calificacion) {
        if(!this.cliente.getId().equals(usuarioId)){
            throw new ViajeNoPuedeSerCalificadoException("El cliente no realizó el viaje");
        }

        if(this.estadoViaje != FINALIZADO) {
            throw new ViajeNoPuedeSerCalificadoException("El viaje no ha sido finalizado");
        }

        this.calificacion = calificacion;
    }

}
