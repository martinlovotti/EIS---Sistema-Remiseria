package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.CrearUsuarioDTO;
import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.RecuperarUsuarioDTO;
import ar.edu.unq.remiseria.controller.dto.ViajeDTO.RecuperarViajeDTO;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService u){this.usuarioService = u;}

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarUsuario(@PathVariable Long id) {
        Usuario u = usuarioService.recuperar(id);
        return ResponseEntity.ok(RecuperarUsuarioDTO.desdeModelo(u));
    }

    @GetMapping("/{id}/viajes")
    public ResponseEntity<List<RecuperarViajeDTO>> recuperarViajesPorEstado(
            @PathVariable Long id,
            @RequestParam EstadoViaje estado) {

        List<Viaje> viajes = usuarioService.recuperarViajesPorEstado(id, estado);
        List<RecuperarViajeDTO> dtos = viajes.stream()
                .map(RecuperarViajeDTO::desdeModelo)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
