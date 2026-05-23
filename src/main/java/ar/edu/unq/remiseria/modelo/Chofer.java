package ar.edu.unq.remiseria.modelo;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerAceptadoException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.ACEPTADO;
import static jakarta.persistence.CascadeType.ALL;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Chofer")
public class Chofer {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String patente;

    @OneToMany(orphanRemoval = true, cascade = ALL, mappedBy = "chofer")
    private List<Viaje> viajes;

    public Chofer(String nombre, String patente) {
        this.nombre = nombre;
        this.patente = patente;
        this.viajes = new ArrayList<>();
    }

    public void aceptarViaje(Viaje viaje) {
        if (viaje.getEstadoViaje() != EstadoViaje.PENDIENTE || !puedeAceptarViaje()) {
            throw new ViajeNoPuedeSerAceptadoException();
        }

        viaje.setChofer(this);
        viaje.setEstadoViaje(ACEPTADO);
        viajes.addFirst(viaje);
    }

    private boolean puedeAceptarViaje() {
        return viajes.isEmpty() || viajes.getFirst().estaFinalizado();
    }
}
