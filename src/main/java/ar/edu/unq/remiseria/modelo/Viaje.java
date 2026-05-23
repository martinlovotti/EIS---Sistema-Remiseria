package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeFinalizarseException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeInicializarseException;
import jakarta.persistence.*;
import lombok.*;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.*;
import static jakarta.persistence.CascadeType.ALL;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Viaje")
public class Viaje {
    @Id
    @GeneratedValue
    private Long id;

    private String origen;
    private String destino;

    private Double precioFinal;
    private Double kilometros;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estadoViaje;

    @ManyToOne(optional = false, cascade = ALL)
    private Usuario cliente;

    @ManyToOne(cascade = ALL)
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

    public void cancelar() {
        if (!(estadoViaje == PENDIENTE || estadoViaje == ACEPTADO)) {
            throw new ViajeNoPuedeCancelarseException(estadoViaje);
        }

        cliente.setViajes(null);

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
            cliente.setViajes(null);
            chofer.setViajes(null);
        } else {
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

    public boolean estaFinalizado() {
        return estadoViaje == FINALIZADO;
    }
}
