package ar.edu.unq.remiseria.persistencia.dao.entity;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Viaje;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Data
@Entity(name = "Chofer")
@Table(name = "Chofer")
public class ChoferSQL {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotNull(message = "La patente no puede ser nula")
    private String patente;

    //@OneToMany
    //private List<Viaje> viajes;

    public static ChoferSQL creadDesde(Chofer model) {
        ChoferSQL choferSQL = new ChoferSQL();
        choferSQL.setNombre(model.getNombre());
        choferSQL.setPatente(model.getPatente());
        return choferSQL;
    }

    public ChoferSQL(Chofer model) {
        this.id = model.getId();
        this.nombre = model.getNombre();
        this.patente = model.getPatente();
    }
}
