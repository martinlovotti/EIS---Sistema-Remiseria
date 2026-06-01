package ar.edu.unq.remiseria.controller.dto;

import ar.edu.unq.remiseria.security.Role;

public record RegisterRequestDTO(String username, String password, Role role) {
}
