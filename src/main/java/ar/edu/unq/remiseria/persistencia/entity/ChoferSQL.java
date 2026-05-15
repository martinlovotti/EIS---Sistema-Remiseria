package ar.edu.unq.remiseria.persistencia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Data
@Entity(name = "Chofer")
public class ChoferSQL {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotNull(message = "La patente no puede ser nula")
    private String patente;

    @OneToOne
    private ViajeSQL viajeActual;
}
