package ar.edu.unq.remiseria.controller.dto.UsuarioDTO;

import ar.edu.unq.remiseria.modelo.Usuario;

public record RecuperarUsuarioDTO(Long id, String nombre) {

    public static RecuperarUsuarioDTO desdeModelo(Usuario usuario) {
        return new RecuperarUsuarioDTO(usuario.getId(), usuario.getNombre());
    }

}
