package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.controller.dto.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.RegisterRequestDTO;

public interface AuthService {
    String login(LoginRequestDTO request);
    void register(RegisterRequestDTO request);
}
