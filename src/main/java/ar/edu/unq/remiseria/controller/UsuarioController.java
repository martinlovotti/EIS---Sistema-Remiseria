package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.CrearUsuarioDTO;
import ar.edu.unq.remiseria.controller.dto.UsuarioDTO.RecuperarUsuarioDTO;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService u){this.usuarioService = u;}

    @PostMapping
    public ResponseEntity<RecuperarUsuarioDTO> crearUsuario(@RequestBody CrearUsuarioDTO usuarioDTO){
        Usuario usuario = usuarioDTO.aModelo();
        usuarioService.crear(usuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RecuperarUsuarioDTO.desdeModelo(usuario));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarUsuario(@PathVariable("id") Long id) {
        Usuario u = usuarioService.recuperar(id);
        return ResponseEntity.ok(RecuperarUsuarioDTO.desdeModelo(u));
    }
}
