package ar.edu.unq.remiseria.controller;


import ar.edu.unq.remiseria.controller.dto.ViajeDTO.CrearViajeDTO;
import ar.edu.unq.remiseria.controller.dto.ViajeDTO.EditarViajeDTO;
import ar.edu.unq.remiseria.controller.dto.ViajeDTO.RecuperarViajeDTO;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/viaje")
public class ViajeController {
    private final ViajeService viajeService;
    private final UsuarioService usuarioService;

    public ViajeController(ViajeService viajeService, UsuarioService usuarioService) {
        this.viajeService = viajeService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<RecuperarViajeDTO> crearViaje(@RequestBody CrearViajeDTO viajeDTO) {
        Usuario usuario = usuarioService.recuperar(viajeDTO.usuarioId());
        Viaje viaje = viajeService.crear(viajeDTO.aModelo(usuario));
        RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<RecuperarViajeDTO> cancelarViaje(@PathVariable Long id) {
            viajeService.cancelarViaje(id);
            Viaje viaje = viajeService.recuperar(id);
            RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
            return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarViajeDTO> editarViaje(@PathVariable Long id, @RequestBody EditarViajeDTO viajeDTO) {
        Viaje viajeEditado = viajeService.editarViaje(id, viajeDTO.aModelo());
        return ResponseEntity.ok(RecuperarViajeDTO.desdeModelo(viajeEditado));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<RecuperarViajeDTO> finalizarViaje(@PathVariable Long id) {
        viajeService.finalizarViaje(id);
        Viaje viaje = viajeService.recuperar(id);
        RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{idViaje}/aceptarViaje/{idChofer}")
    public ResponseEntity<RecuperarViajeDTO> aceptarViaje(@PathVariable Long idViaje, @PathVariable Long idChofer) {
        viajeService.aceptarViaje(idViaje, idChofer);
        Viaje viaje = viajeService.recuperar(idViaje);
        RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<RecuperarViajeDTO> iniciarViaje(@PathVariable Long id) {
        viajeService.iniciarViaje(id);
        Viaje viaje = viajeService.recuperar(id);
        RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{idViaje}/calificar/{usuarioId}/{calificacion}")
    public ResponseEntity<RecuperarViajeDTO> calificarViaje(@PathVariable Long idViaje, @PathVariable Long usuarioId, @PathVariable Double calificacion) {
        viajeService.calificarViaje(idViaje, usuarioId, calificacion);
        Viaje viaje = viajeService.recuperar(idViaje);
        RecuperarViajeDTO dto = RecuperarViajeDTO.desdeModelo(viaje);
        return ResponseEntity.ok(dto);
    }
}
