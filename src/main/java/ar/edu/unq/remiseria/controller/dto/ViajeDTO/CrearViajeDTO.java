package ar.edu.unq.remiseria.controller.dto.ViajeDTO;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;

public record CrearViajeDTO(String origen, String destino, Long usuarioId) {

    public Viaje aModelo(Usuario usuario) {
        return new Viaje(usuario, origen, destino);
    }
}
