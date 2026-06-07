package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginResponseDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterChoferRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterUsuarioRequestDTO;

public interface AuthService {

    void registerUsuario(RegisterUsuarioRequestDTO request);

    void registerChofer(RegisterChoferRequestDTO request);

    LoginResponseDTO login(LoginRequestDTO request);
}