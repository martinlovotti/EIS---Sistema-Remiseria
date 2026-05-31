package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeFinalizarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeInicializarseException;
import lombok.*;

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

    public Viaje(Usuario cliente, Chofer chofer) {
        this.cliente = cliente;
        this.chofer = chofer;
        this.estadoViaje = PENDIENTE;
    }

    public Viaje(Usuario cliente, String origen, String destino) {
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.estadoViaje = PENDIENTE;
    }

    public Viaje(Usuario cliente, String origen, String destino, Double km) {
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.kilometros = km;
        this.estadoViaje = PENDIENTE;
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

}
