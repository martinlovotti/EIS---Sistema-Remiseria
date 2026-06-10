package ar.edu.unq.remiseria.controller.dto.AuthDTO;

public record RegisterChoferRequestDTO(
        String username,
        String password,
        String nombre,
        String patente
) {
}