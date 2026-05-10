package ar.edu.unq.remiseria.persistencia.dao.entity;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;


import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(name = "Usuario")
@Table(name = "Usuario")
public class UsuarioSQL {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    //@OneToMany
    //private List<Viaje> viajes;

    public static UsuarioSQL crearDesde(Usuario model){
        UsuarioSQL usuarioSQL = new UsuarioSQL();
        usuarioSQL.setNombre(model.getNombre());

        return usuarioSQL;
    }

    public UsuarioSQL(Usuario model){
        this.id = model.getId();
        this.nombre = model.getNombre();
    }
}
