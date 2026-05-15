package ar.edu.unq.remiseria.persistencia.entity;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private EstadoViaje estadoViaje;

    @OneToOne(optional = false)
    private UsuarioSQL cliente;

    @OneToOne
    private ChoferSQL chofer;
}
