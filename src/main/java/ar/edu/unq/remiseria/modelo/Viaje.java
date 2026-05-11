package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.*;

@Data
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

    public void cancelar() {
        if (!(estadoViaje == PENDIENTE || estadoViaje == ACEPTADO)) {
            throw new ViajeNoPuedeCancelarseException();
        }

        this.setEstadoViaje(CANCELADO);
    }

}
