package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginResponseDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterChoferRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterUsuarioRequestDTO;
import ar.edu.unq.remiseria.servicios.interfaces.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/usuario")
    public ResponseEntity<String> registerUsuario(@RequestBody RegisterUsuarioRequestDTO request) {
        authService.registerUsuario(request);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/register/chofer")
    public ResponseEntity<String> registerChofer(@RequestBody RegisterChoferRequestDTO request) {
        authService.registerChofer(request);
        return ResponseEntity.ok("Chofer registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}