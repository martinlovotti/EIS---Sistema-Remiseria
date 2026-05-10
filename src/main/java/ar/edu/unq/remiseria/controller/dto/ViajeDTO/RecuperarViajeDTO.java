package ar.edu.unq.remiseria.controller.dto.ViajeDTO;

import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.RecuperarUsuarioDTO;
import ar.edu.unq.remiseria.modelo.Viaje;

public record RecuperarViajeDTO(Long id, String origen, String destino, RecuperarUsuarioDTO usuario) {

    public static RecuperarViajeDTO desdeModelo(Viaje viaje) {
        return new RecuperarViajeDTO(
                viaje.getId(),
                viaje.getOrigen(),
                viaje.getDestino(),
                RecuperarUsuarioDTO.desdeModelo(viaje.getCliente())
        );
    }
}
