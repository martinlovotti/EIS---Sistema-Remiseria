package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerAceptadoException;
import ar.edu.unq.remiseria.exception.*;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeInicializarseException;
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

    @Autowired
    private ChoferService choferService;

    private Usuario cliente;
    private Chofer chofer;
    private Viaje viaje;
    private Viaje viajeSinChofer;

    private Viaje viajeEnCurso;
    private Usuario cliente2;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setNombre("Pepe");
        cliente = usuarioService.crear(cliente);
        chofer = new Chofer("Raul", "AAA 111");
        chofer = choferService.crear(chofer);
        viaje = new Viaje(cliente, chofer);
        viajeSinChofer = new Viaje(cliente, "Quilmes", "Bernal");
        viaje.setOrigen("Quilmes");
        viaje.setDestino("Bernal");
        viaje.setKilometros(6.2);
        viaje.setPrecioFinal(4500.0);


        cliente2 = new Usuario();
        cliente2.setNombre("Jaime");
        cliente2 = usuarioService.crear(cliente2);
        viajeEnCurso = new Viaje(cliente2, chofer);



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
        viajeSinChofer.setCliente(cliente);
        Viaje viajeCreado = viajeService.crear(viajeSinChofer);

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
        assertEquals(viajeEditado.getCliente().getId(), cliente.getId());
        assertNull(viajeEditado.getChofer());
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

    @Test
    public void viajeQueNoEstaEnCursoNoSeFinaliza(){
        viajeEnCurso.setEstadoViaje(EstadoViaje.PENDIENTE);
        Viaje viaje = viajeService.crear(viajeEnCurso);

        assertThrows(
                ViajeNoPuedeCancelarseException.class, () ->
                        viajeService.finalizarViaje(viaje.getId())
        );

    }

    @Test
    public void viajeEnCursoFinaliza(){
        Viaje viaje = viajeService.crear(viajeEnCurso);
        viajeService.aceptarViaje(viaje.getId(), chofer.getId());
        viajeService.iniciarViaje(viaje.getId());

        viajeService.finalizarViaje(viaje.getId());

        Viaje viajeActualizado = viajeService.recuperar(viaje.getId());

        assertEquals(EstadoViaje.FINALIZADO, viajeActualizado.getEstadoViaje());
        assertEquals(null, viajeActualizado.getCliente().getViajeActual());
        assertEquals(null, viajeActualizado.getChofer().getViajeActual());
    }

    @Test
    public void viajeAceptadoInicia(){
        Viaje viajeAceptado = viajeService.crear(viaje);
        viajeService.aceptarViaje(viajeAceptado.getId(), chofer.getId());

        viajeService.iniciarViaje(viajeAceptado.getId());

        Viaje viajeIniciado = viajeService.recuperar(viajeAceptado.getId());

        assertEquals(EstadoViaje.EN_CURSO, viajeIniciado.getEstadoViaje());
    }

    @Test
    public void viajeNoAceptadoInicia(){
        viaje.setEstadoViaje(EstadoViaje.PENDIENTE);
        Viaje viajeP = viajeService.crear(viaje);

        assertThrows(
                ViajeNoPuedeInicializarseException.class, () ->
                        viajeService.iniciarViaje(viajeP.getId())
        );

    }
    @Test
    public void viajeSolicitadoEsAceptadoSeLeAsignaUnChoferTest() {
        Viaje viajeSinChoferCreado = viajeService.crear(viajeSinChofer);

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje viajeRecuperado = viajeService.recuperar(viajeSinChoferCreado.getId());
        Chofer choferRecuperado = choferService.recuperar(chofer.getId());

        assertEquals(viajeRecuperado.getChofer().getId(), choferRecuperado.getId());
    }

    @Test
    public void cuandoUnViajeEsAceptadoSuEstadoCambiaAAceptadoTest() {
        Viaje viajeSinChoferCreado = viajeService.crear(viajeSinChofer);

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje viajeRecuperado = viajeService.recuperar(viajeSinChoferCreado.getId());

        assertEquals(EstadoViaje.ACEPTADO, viajeRecuperado.getEstadoViaje());
    }

    @Test
    public void aceptarViajeParaUnViajeQueYaFueAceptadoLanzaExcepcionTest() {
        Viaje viajeSinChoferCreado = viajeService.crear(viajeSinChofer);

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Chofer chofer2 =  choferService.crear(new Chofer("Nova", "QQQ 666"));

        Viaje viajeRecuperado = viajeService.recuperar(viajeSinChoferCreado.getId());

        assertThrows(ViajeNoPuedeSerAceptadoException.class, () -> viajeService.aceptarViaje(viajeRecuperado.getId(), chofer2.getId()));
    }

    @Test
    public void aceptarViajeParaUnChoferQueYaTieneUnViajeAsignadoLanzaExcepcionTest() {
        Viaje viajeSinChoferCreado = viajeService.crear(viajeSinChofer);

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje otroViajeSinChoferCreado = viajeService.crear(new Viaje(cliente2, "Bernal", "Avellaneda"));


        assertThrows(ViajeNoPuedeSerAceptadoException.class, () -> viajeService.aceptarViaje(otroViajeSinChoferCreado.getId(), chofer.getId()));
    }


}
