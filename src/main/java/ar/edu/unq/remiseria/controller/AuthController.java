package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.LoginResponseDTO;
import ar.edu.unq.remiseria.controller.dto.RegisterRequestDTO;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario creado");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}