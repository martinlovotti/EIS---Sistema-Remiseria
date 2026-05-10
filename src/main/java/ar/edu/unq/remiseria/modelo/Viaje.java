package ar.edu.unq.remiseria.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.PENDIENTE;

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

}
