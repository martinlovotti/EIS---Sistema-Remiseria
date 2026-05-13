package ar.edu.unq.remiseria.persistencia.dao.entity;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Viaje;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Objects.isNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

    public static ViajeSQL from(Viaje viaje) {
        ViajeSQL viajeSQL = new ViajeSQL();

        viajeSQL.setId(viaje.getId());
        viajeSQL.setOrigen(viaje.getOrigen());
        viajeSQL.setDestino(viaje.getDestino());
        viajeSQL.setEstadoViaje(viaje.getEstadoViaje());
        viajeSQL.setPrecioFinal(viaje.getPrecioFinal());
        viajeSQL.setKilometros(viaje.getKilometros());

        return viajeSQL;
    }

    public Viaje toModel() {
        Viaje viaje = new Viaje();

        viaje.setEstadoViaje(getEstadoViaje());
        //viaje.setCliente(UsuarioSQL.toModel(getCliente()));
        //viaje.setChofer(ChoferSQL.toModel(getChofer()));
        viaje.setOrigen(getOrigen());
        viaje.setDestino(getDestino());
        viaje.setPrecioFinal(getPrecioFinal());
        viaje.setKilometros(getKilometros());

        return viaje;
    }

}
