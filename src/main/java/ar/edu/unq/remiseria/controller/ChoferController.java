package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.ChoferDTO.CrearChoferDTO;
import ar.edu.unq.remiseria.controller.dto.ChoferDTO.RecuperarChoferDTO;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/chofer")
public class ChoferController {

    private final ChoferService choferService;
    public ChoferController(ChoferService c){this.choferService = c;}

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarChofer(@PathVariable Long id) {
        Chofer c = choferService.recuperar(id);
        return ResponseEntity.ok(RecuperarChoferDTO.desdeModelo(c));
    }
}