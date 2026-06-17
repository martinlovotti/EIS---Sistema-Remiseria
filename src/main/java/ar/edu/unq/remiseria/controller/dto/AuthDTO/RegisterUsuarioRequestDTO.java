package ar.edu.unq.remiseria.controller.dto.AuthDTO;

public record RegisterUsuarioRequestDTO(
        String username,
        String password,
        String nombre
) {
}