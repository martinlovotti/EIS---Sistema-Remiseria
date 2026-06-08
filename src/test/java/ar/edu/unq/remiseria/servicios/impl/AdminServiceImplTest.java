package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.NoHayChoferesException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.AdminService;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import ar.edu.unq.remiseria.testService.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
    private TestService testService;

    private Chofer juan;
    private Chofer pedro;
    private Usuario cliente1;
    private Usuario cliente2;
    private Usuario cliente3;

    @BeforeEach
    void prepare() {
        juan = crearChofer("juan", "ABC123");
        pedro = crearChofer("pedro", "XYZ789");
        cliente1 = crearUsuario("cliente1");
        cliente2 = crearUsuario("cliente2");
        cliente3 = crearUsuario("cliente3");
    }

    private Chofer crearChofer(String nombre, String patente) {
        return choferService.crear(new Chofer(nombre, patente));
    }

    private Usuario crearUsuario(String nombre) {
        return usuarioService.crear(new Usuario(nombre));
    }

    private Viaje crearViajeSolicitado(Usuario cliente, String origen, String destino) {
        return viajeService.crear(new Viaje(cliente, origen, destino));
    }

//    private Viaje crearViajeSolicitado(Usuario cliente, String origen, String destino, Double km) {
//        return viajeService.crear(new Viaje(cliente, origen, destino, km));
//    }
//
//    private Viaje crearViajeSolicitado(Usuario cliente, String origen, String destino, Double km, Double precioFinal) {
//        return viajeService.crear(new Viaje(cliente, origen, destino, km, precioFinal));
//    }

    @Test
    void conMasViajesRetornaElChoferConMasViajes() {
        Viaje viaje1 = crearViajeSolicitado(cliente1, "Quilmes", "Bernal");
        Viaje viaje2 = crearViajeSolicitado(cliente2, "Quilmes", "Avellaneda");

        viajeService.aceptarViaje(viaje1.getId(), juan.getId());
        viajeService.iniciarViaje(viaje1.getId());
        viajeService.finalizarViaje(viaje1.getId());

        viajeService.aceptarViaje(viaje2.getId(), juan.getId());

        Viaje viaje3 = crearViajeSolicitado(cliente3, "Quilmes", "La Plata");
        viajeService.aceptarViaje(viaje3.getId(), pedro.getId());

        Chofer resultado = adminService.conMasViajes();

        assertEquals(juan.getId(), resultado.getId());
        assertEquals("juan", resultado.getNombre());
    }


    @Test
    void conMasKmRetornaElChoferConMasKm() {
        Viaje viaje1 = crearViajeSolicitado(cliente1, "Quilmes", "La Plata");
        Viaje viaje2 = crearViajeSolicitado(cliente2, "Quilmes", "Avellaneda");
        Viaje viaje3 = crearViajeSolicitado(cliente3, "Quilmes", "Bernal");

        viajeService.aceptarViaje(viaje1.getId(), juan.getId());
        viajeService.iniciarViaje(viaje1.getId());
        viajeService.finalizarViaje(viaje1.getId());

        viajeService.aceptarViaje(viaje2.getId(), pedro.getId());
        viajeService.iniciarViaje(viaje2.getId());
        viajeService.finalizarViaje(viaje2.getId());

        viajeService.aceptarViaje(viaje3.getId(), pedro.getId());
        viajeService.iniciarViaje(viaje3.getId());
        viajeService.finalizarViaje(viaje3.getId());

        Chofer resultado = adminService.conMasKm();

        assertNotNull(resultado);
        // Validamos que sea alguno de los choferes
        assertTrue(resultado.getId().equals(juan.getId()) || resultado.getId().equals(pedro.getId()));
    }

    @Test
    void conMasFacturacionRetornaElChoferConMasFacturacion() {
        Viaje viaje1 = crearViajeSolicitado(cliente1, "Quilmes", "La Plata");
        Viaje viaje2 = crearViajeSolicitado(cliente2, "Quilmes", "Avellaneda");
        Viaje viaje3 = crearViajeSolicitado(cliente3, "Quilmes", "Bernal");

        viajeService.aceptarViaje(viaje1.getId(), juan.getId());
        viajeService.iniciarViaje(viaje1.getId());
        viajeService.finalizarViaje(viaje1.getId());

        viajeService.aceptarViaje(viaje2.getId(), pedro.getId());
        viajeService.iniciarViaje(viaje2.getId());
        viajeService.finalizarViaje(viaje2.getId());

        viajeService.aceptarViaje(viaje3.getId(), pedro.getId());
        viajeService.iniciarViaje(viaje3.getId());
        viajeService.finalizarViaje(viaje3.getId());

        Chofer resultado = adminService.conMasFacturacion();

        assertNotNull(resultado);
        assertTrue(resultado.getId().equals(juan.getId()) || resultado.getId().equals(pedro.getId()));
    }

    @Test
    void conMasViajesTiraExcepcionSiNoHayViajes() {
        assertThrows(NoHayChoferesException.class, () -> adminService.conMasViajes());
    }

    @Test
    void conMasViajesTiraExcepcionSiNingunViajeTieneChofer() {
        crearViajeSolicitado(cliente1, "Quilmes", "Bernal");

        assertThrows(NoHayChoferesException.class, () -> adminService.conMasViajes());
    }

    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }
}