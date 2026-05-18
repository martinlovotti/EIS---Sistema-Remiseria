package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerAceptadoException;
import lombok.*;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.ACEPTADO;


@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Chofer {
    private Long id;
    private String nombre;
    private String patente;
    private Viaje viajeActual;

    public Chofer(String nombre, String patente){
        this.nombre = nombre;
        this.patente = patente;
        this.viajeActual = null;
    }

    public void aceptarViaje(Viaje viaje) {
        if (viaje.getEstadoViaje() != EstadoViaje.PENDIENTE || getViajeActual() != null) {
            throw new ViajeNoPuedeSerAceptadoException();
        }

        viaje.setChofer(this);
        viaje.setEstadoViaje(ACEPTADO);
        setViajeActual(viaje);
    }
}
