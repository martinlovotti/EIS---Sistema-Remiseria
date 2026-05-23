package ar.edu.unq.remiseria.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @OneToMany(orphanRemoval = true, cascade = ALL, mappedBy = "cliente")
    private List<Viaje> viajes;

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.viajes = new ArrayList<>();
    }

    public boolean tieneViajeSolicitado() {
        return !viajes.isEmpty() && !viajes.getFirst().estaFinalizado();
    }

    public void solicitarViaje(Viaje viaje) {
        this.viajes.addFirst(viaje);
        viaje.setCliente(this);
    }
}
