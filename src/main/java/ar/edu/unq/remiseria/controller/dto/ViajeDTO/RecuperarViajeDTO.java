package ar.edu.unq.remiseria.controller.dto.ViajeDTO;

import ar.edu.unq.remiseria.controller.dto.ChoferDTO.RecuperarChoferDTO;
import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.RecuperarUsuarioDTO;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Viaje;

public record RecuperarViajeDTO(Long id, String origen, String destino, EstadoViaje estado, RecuperarUsuarioDTO usuario, RecuperarChoferDTO chofer, Double calificacion) {

    public static RecuperarViajeDTO desdeModelo(Viaje viaje) {
        return new RecuperarViajeDTO(
                viaje.getId(),
                viaje.getOrigen(),
                viaje.getDestino(),
                viaje.getEstadoViaje(),
                RecuperarUsuarioDTO.desdeModelo(viaje.getCliente()),
                viaje.getChofer() != null ? RecuperarChoferDTO.desdeModelo(viaje.getChofer()) : null,
                viaje.getCalificacion() != null ? viaje.getCalificacion() : null
        );
    }
}
