package ar.edu.unq.remiseria.controller.dto.UsuarioDTO;

import ar.edu.unq.remiseria.modelo.Usuario;

    public record CrearUsuarioDTO(String nombre){

        public Usuario aModelo(){
            Usuario user = new Usuario(
            this.nombre);
            return user;
        }
    }

