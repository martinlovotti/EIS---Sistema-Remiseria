package ar.edu.unq.remiseria.controller.dto.AuthDTO;

import ar.edu.unq.remiseria.security.Role;

public record LoginResponseDTO(String token, Long entidadId, Role role) {
}
