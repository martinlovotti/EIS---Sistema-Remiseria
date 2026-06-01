package ar.edu.unq.remiseria.persistencia.entity;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Viaje")
public class ViajeSQL {
    @Id
    @GeneratedValue
    private Long id;

    private String origen;
    private String destino;

    private Double precioFinal;
    private Double kilometros;
    private Double calificacion;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estadoViaje;

    @ManyToOne(optional = false)
    private UsuarioSQL cliente;

    @ManyToOne
    private ChoferSQL chofer;
}
