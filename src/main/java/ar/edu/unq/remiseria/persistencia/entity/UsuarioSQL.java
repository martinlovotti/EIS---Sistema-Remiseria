package ar.edu.unq.remiseria.persistencia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static java.util.Objects.isNull;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Usuario")
public class UsuarioSQL {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @OneToOne
    private ViajeSQL viajeActual;

}
