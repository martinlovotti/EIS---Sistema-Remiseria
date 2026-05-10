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

import static jakarta.persistence.GenerationType.AUTO;

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

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ViajeSQL> viajes = new ArrayList<>();

    public static UsuarioSQL crearDesde(Usuario model){
        UsuarioSQL usuarioSQL = new UsuarioSQL();
        usuarioSQL.setNombre(model.getNombre());
        usuarioSQL.setViajes(model.getViajes().stream().map(ViajeSQL::from).toList());



        return usuarioSQL;
    }

    public UsuarioSQL(Usuario model){
        this.id = model.getId();
        this.nombre = model.getNombre();
    }
}
