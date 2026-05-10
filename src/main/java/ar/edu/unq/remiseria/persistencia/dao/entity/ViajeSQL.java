package ar.edu.unq.remiseria.persistencia.dao.entity;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Viaje;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
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

    @ManyToOne
    private UsuarioSQL cliente;

    @ManyToOne
    private ChoferSQL chofer;

    public static ViajeSQL from(Viaje viaje) {
        ViajeSQL viajeSQL = new ViajeSQL();

        viajeSQL.setEstadoViaje(viaje.getEstadoViaje());
        viajeSQL.setCliente(UsuarioSQL.crearDesde(viaje.getCliente()));
        viajeSQL.setChofer(ChoferSQL.creadDesde(viaje.getChofer()));
        viajeSQL.setPrecioFinal(viaje.getPrecioFinal());
        viajeSQL.setKilometros(viaje.getKilometros());

        return viajeSQL;
    }
}
