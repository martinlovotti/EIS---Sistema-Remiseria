package ar.edu.unq.remiseria.controller.dto.ViajeDTO;

import ar.edu.unq.remiseria.controller.dto.ChoferDTO.RecuperarChoferDTO;
import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.RecuperarUsuarioDTO;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Viaje;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RecuperarViajeDTO(Long id, String origen, String destino, Double distancia, Double precio, EstadoViaje estado, RecuperarUsuarioDTO usuario, RecuperarChoferDTO chofer, Double calificacion, @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime fechaCreacion) {

    public static RecuperarViajeDTO desdeModelo(Viaje viaje) {
        return new RecuperarViajeDTO(
                viaje.getId(),
                viaje.getOrigen(),
                viaje.getDestino(),
                viaje.getKilometros(),
                viaje.getPrecioFinal(),
                viaje.getEstadoViaje(),
                RecuperarUsuarioDTO.desdeModelo(viaje.getCliente()),
                viaje.getChofer() != null ? RecuperarChoferDTO.desdeModelo(viaje.getChofer()) : null,
                viaje.getCalificacion() != null ? viaje.getCalificacion() : null,
                viaje.getFechaCreacion()

        );
    }
}
