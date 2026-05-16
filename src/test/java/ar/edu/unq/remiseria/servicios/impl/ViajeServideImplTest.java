package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.*;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.EN_CURSO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ViajeServideImplTest {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario cliente;
    private Chofer chofer;
    private Viaje viaje;
    private Viaje viajeSinChofer;
    @Autowired
    private ChoferService choferService;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setNombre("Pepe");
        cliente = usuarioService.crear(cliente);
        chofer = new Chofer();
        viaje = new Viaje(cliente, chofer);
        viajeSinChofer = new Viaje(cliente, "Quilmes", "Bernal");
        viaje.setOrigen("Quilmes");
        viaje.setDestino("Bernal");
        viaje.setKilometros(6.2);
        viaje.setPrecioFinal(4500.0);

    }

    @Test
    public void crearViajeTest() {
        assertNull(viaje.getId());
        Viaje viajeCreado = viajeService.crear(viaje);
        assertNotNull(viajeCreado.getId());
    }

    @Test
    public void unViajeCreadoTieneUnClienteQuienLoSolicitoTest() {
        Viaje viajeCreado = viajeService.crear(viajeSinChofer);
        assertEquals(viajeCreado.getCliente().getId(), cliente.getId());
    }

    @Test
    public void unViajeRecienCreadoTieneEstadoPendienteTest() {
        viajeService.crear(viajeSinChofer);
        assertEquals(EstadoViaje.PENDIENTE, viajeSinChofer.getEstadoViaje());
    }

    @Test
    public  void unViajeRecienCreadoNoTieneChoferAsignadoTest() {
        viajeService.crear(viajeSinChofer);
        assertNull(viajeSinChofer.getChofer());
    }

    @Test
    public void crearViajeParaUnClienteQueYaTieneViajeSolicitadoLanzaExcepcionTest() {
        viajeService.crear(viajeSinChofer);
        Usuario clienteRecuperado = usuarioService.recuperar(cliente.getId());
        Viaje viajeSolicitado = new Viaje(clienteRecuperado, "Ezpeleta", "Berazategui");

        assertThrows(UsuarioConViajeSolicitadoException.class, () -> viajeService.crear(viajeSolicitado));
    }

    @Test
    public void editarViajeSiElViajeEsValidoSeActualizaTest() {
        Viaje viajeCreado = viajeService.crear(viaje);
        viajeCreado.setOrigen("Ezpeleta");
        viajeCreado.setDestino("Berazategui");
        viajeCreado.setKilometros(2.5);
        viajeCreado.setPrecioFinal(3300.0);

        viajeService.editarViaje(viajeCreado.getId(), viajeCreado);

        viaje = viajeService.recuperar(viajeCreado.getId());
        assertEquals("Ezpeleta", viaje.getOrigen());
        assertEquals("Berazategui", viaje.getDestino());
    };

    @Test
    public void editarViajeSiElOrigenNoEsValidoLanzaExcepcionTest() {
        Long viajeId = viajeService.crear(viaje).getId();

        Viaje viajeOrigenVacio = new Viaje();
        viajeOrigenVacio.setOrigen("");
        viajeOrigenVacio.setDestino("Varela");

        Viaje viajeOrigenNull = new Viaje();
        viajeOrigenNull.setDestino("Varela");

        assertThrows(OrigenInvalidoException.class, () -> {
            viajeService.editarViaje(viajeId, viajeOrigenVacio);
        });

        assertThrows(OrigenInvalidoException.class, () -> {
            viajeService.editarViaje(viajeId, viajeOrigenNull);
        });
    }

    @Test
    public void editarViajeSiElDestinoNoEsValidoLanzaExcepcionTest() {
        Long viajeId = viajeService.crear(viaje).getId();

        Viaje viajeDestinoVacio = new Viaje();
        viajeDestinoVacio.setOrigen("Varela");
        viajeDestinoVacio.setDestino("");

        Viaje viajeDestinoNull = new Viaje();
        viajeDestinoNull.setOrigen("Varela");

        assertThrows(DestinoInvalidoException.class, () -> {
            viajeService.editarViaje(viajeId, viajeDestinoVacio);
        });

        assertThrows(DestinoInvalidoException.class, () -> {
            viajeService.editarViaje(viajeId, viajeDestinoNull);
        });
    }

    @Test
    public void editarViajeSiElViajeEstaEnCursoLanzaExcepcion() {
        // TODO implementar viajeService.aceptarViaje para poder testear este caso
        Viaje viajeCreado = viajeService.crear(viaje);
        viajeService.aceptarViaje(viajeCreado.getId(), choferService.crear(chofer).getId());

        assertThrows(ViajeYaIniciadoException.class, () -> {
            viajeService.editarViaje(viajeCreado.getId(), viajeCreado);
            throw new RuntimeException("falta implementar viajeService.aceptarViaje");
        });
    }

    @Test
    public void editarViajeSiElViajeNoExisteLanzaExcepcionTest() {
        Viaje viajeNoExistente = viaje;

        assertThrows(ViajeNoEncontradoException.class, () -> {
           viajeService.editarViaje(912L, viajeNoExistente);
        });
    }

    @Test
    public void editarViajeSoloModificaOrigenYDestinoTest() {
        Viaje viajeCreado = viajeService.crear(viaje);

        Viaje viajeAEditar = new Viaje();
        viajeAEditar.setId(viajeCreado.getId());
        viajeAEditar.setOrigen("Varela");
        viajeAEditar.setDestino("Bernal");
        viajeAEditar.setKilometros(12.0);
        viajeAEditar.setPrecioFinal(6000.0);
        viajeAEditar.setCliente(usuarioService.crear(new Usuario()));
        viajeAEditar.setChofer(choferService.crear(new Chofer()));
        viajeAEditar.setEstadoViaje(EN_CURSO);

        Viaje viajeEditado = viajeService.editarViaje(viajeCreado.getId(), viajeAEditar);

        assertEquals(viajeCreado.getEstadoViaje(), viajeEditado.getEstadoViaje());
        assertEquals(viajeCreado.getCliente().getId(), viajeEditado.getCliente().getId());
        assertEquals(viajeCreado.getChofer().getId(), viajeEditado.getChofer().getId());
        assertEquals(viajeCreado.getPrecioFinal(), viajeEditado.getPrecioFinal());
        assertEquals(viajeCreado.getKilometros(), viajeEditado.getKilometros());

    }

    @Test
    public void unViajeEnEstadoPendienteSeCancelaTest() {
        Viaje viaje = viajeService.crear(viajeSinChofer);
        viajeService.cancelarViaje(viaje.getId());

        Viaje viajeCancelado = viajeService.recuperar(viaje.getId());

        assertEquals(EstadoViaje.CANCELADO, viajeCancelado.getEstadoViaje());
    }

    @Test
    public void unViajeEnEstadoAceptadoSeCancelaTest() {
        viaje.setEstadoViaje(EstadoViaje.ACEPTADO);
        Viaje viajeAceptado = viajeService.crear(viaje);
        viajeService.cancelarViaje(viajeAceptado.getId());

        Viaje viajeCancelado = viajeService.recuperar(viajeAceptado.getId());

        assertEquals(EstadoViaje.CANCELADO, viajeCancelado.getEstadoViaje());
    }

    @Test
    public void unViajeEnEstadoEnCursoNoPuedeCancelarseTest() {
        viaje.setEstadoViaje(EstadoViaje.EN_CURSO);
        Viaje viajeEnCurso = viajeService.crear(viaje);

        assertThrows(
                ViajeNoPuedeCancelarseException.class, () ->
                        viajeService.cancelarViaje(viajeEnCurso.getId())
        );
    }


}
