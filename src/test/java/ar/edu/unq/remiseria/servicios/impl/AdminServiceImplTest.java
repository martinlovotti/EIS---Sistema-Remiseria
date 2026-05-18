package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.NoHayChoferesException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.servicios.interfaces.AdminService;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private ChoferService choferService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ViajeDAO viajeDAO;

    @Autowired
    private ChoferDAO choferDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private Chofer juan;
    private Chofer pedro;
    private Usuario cliente1;
    private Usuario cliente2;
    private Usuario cliente3;

    @BeforeEach
    void prepare() {
        juan = choferService.crear(new Chofer("juan", "abc123"));
        pedro = choferService.crear(new Chofer("pedro", "xyz789"));
        cliente1 = usuarioService.crear(new Usuario("cliente1"));
        cliente2 = usuarioService.crear(new Usuario("cliente2"));
        cliente3 = usuarioService.crear(new Usuario("cliente3"));
    }

    @AfterEach
    void cleanup() {
        viajeDAO.deleteAll();
        choferDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

    //@Test
    void conMasViajesRetornaElChoferConMasViajes() {
        Viaje viaje1 = viajeService.crear(new Viaje(cliente1, "origen", "destino"));
        Viaje viaje2 = viajeService.crear(new Viaje(cliente2, "origen", "destino"));
        viajeService.aceptarViaje(viaje1.getId(), juan.getId());
        viajeService.aceptarViaje(viaje2.getId(), juan.getId());


        Viaje viaje3 = viajeService.crear(new Viaje(cliente3, "origen", "destino"));
        viajeService.aceptarViaje(viaje3.getId(), pedro.getId());

        Chofer resultado = adminService.conMasViajes();

        assertEquals(juan.getId(), resultado.getId());
    }

    @Test
    void conMasViajesTiraExcepcionSiNoHayViajes() {
        assertThrows(NoHayChoferesException.class, () -> adminService.conMasViajes());
    }

    //@Test
    void conMasViajesTiraExcepcionSiNingunViajeTieneChofer() {
        viajeService.crear(new Viaje(cliente1, "origen", "destino")); // queda PENDIENTE, sin chofer

        assertThrows(NoHayChoferesException.class, () -> adminService.conMasViajes());
    }
}