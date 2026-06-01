package ar.edu.unq.remiseria.controller;

import ar.edu.unq.remiseria.controller.dto.ChoferDTO.RecuperarChoferDTO;
import ar.edu.unq.remiseria.servicios.interfaces.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/chofer-mas-viajes")
    public ResponseEntity<RecuperarChoferDTO> recuperarChoferConMasViajes() {
        return ResponseEntity.ok(RecuperarChoferDTO.desdeModelo(adminService.conMasViajes()));
    }

    @GetMapping("/chofer-mas-km")
    public ResponseEntity<RecuperarChoferDTO> recuperarChoferConMasKm() {
        return ResponseEntity.ok(RecuperarChoferDTO.desdeModelo(adminService.conMasKm()));
    }

    @GetMapping("/chofer-mas-facturacion")
    public ResponseEntity<RecuperarChoferDTO> recuperarChoferConMasFacturacion() {
        return ResponseEntity.ok(RecuperarChoferDTO.desdeModelo(adminService.conMasFacturacion()));
    }
}
