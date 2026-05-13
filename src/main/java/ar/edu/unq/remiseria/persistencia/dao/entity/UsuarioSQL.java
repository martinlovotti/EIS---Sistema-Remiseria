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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.AUTO;
import static java.util.Objects.isNull;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Usuario")
@Table(name = "Usuario")
public class UsuarioSQL {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @OneToOne
    private ViajeSQL viajeActual;

    public static UsuarioSQL crearDesde(Usuario usuario){
        UsuarioSQL usuarioSQL = new UsuarioSQL();
        usuarioSQL.setNombre(usuario.getNombre());

        if (!isNull(usuario.getViajeActual())) {
            usuarioSQL.setViajeActual(ViajeSQL.from(usuario.getViajeActual()));
        }

        return usuarioSQL;
    }

    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setNombre(getNombre());
        usuario.setId(getId());
        if (!isNull(getViajeActual())) {
            usuario.setViajeActual(getViajeActual().toModel());
        }

        return usuario;
    }

    public UsuarioSQL(Usuario model){
        this.id = model.getId();
        this.nombre = model.getNombre();
    }
}
