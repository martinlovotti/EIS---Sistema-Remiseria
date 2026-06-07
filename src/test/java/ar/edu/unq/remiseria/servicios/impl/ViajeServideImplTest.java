package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.*;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import ar.edu.unq.remiseria.testService.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.EN_CURSO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ViajeServideImplTest {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ChoferService choferService;

    @Autowired
    private TestService testService;

    private Usuario cliente;
    private Chofer chofer;
    private Usuario cliente2;

    @BeforeEach
    void setUp() {
        cliente = crearUsuario("Pepe");
        chofer = crearChofer("Raul", "AAA 111");
        cliente2 = crearUsuario("Jaime");
    }

    private Usuario crearUsuario(String nombre) {
        return usuarioService.crear(new Usuario(nombre));
    }

    private Chofer crearChofer(String nombre, String patente) {
        return choferService.crear(new Chofer(nombre, patente));
    }

    private Viaje crearViajeSolicitado(String origen, String destino) {
        return viajeService.crear(new Viaje(cliente, origen, destino));
    }

    @Test
    public void crearViajeTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        assertNotNull(viajeCreado.getId());
        assertEquals(EstadoViaje.PENDIENTE, viajeCreado.getEstadoViaje());
        assertNull(viajeCreado.getChofer());
    }

    @Test
    public void unViajeCreadoTieneUnClienteQuienLoSolicitoTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        assertEquals(cliente.getId(), viajeCreado.getCliente().getId());
        assertNull(viajeCreado.getChofer());
    }

    @Test
    public void unViajeRecienCreadoTieneEstadoPendienteTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        assertEquals(EstadoViaje.PENDIENTE, viajeCreado.getEstadoViaje());
    }

    @Test
    public  void unViajeRecienCreadoNoTieneChoferAsignadoTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        assertNull(viajeCreado.getChofer());
    }

    @Test
    public void crearViajeParaUnClienteQueYaTieneViajeSolicitadoLanzaExcepcionTest() {
        crearViajeSolicitado("Quilmes", "Bernal");
        Viaje viajeSolicitado = new Viaje(usuarioService.recuperar(cliente.getId()), "Ezpeleta", "Berazategui");

        assertThrows(UsuarioConViajeSolicitadoException.class, () -> viajeService.crear(viajeSolicitado));
    }

    @Test
    public void editarViajeSiElViajeEsValidoSeActualizaTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        viajeCreado.setOrigen("Ezpeleta");
        viajeCreado.setDestino("Berazategui");
        viajeCreado.setKilometros(2.5);
        viajeCreado.setPrecioFinal(3300.0);

        Viaje viajeEditado = viajeService.editarViaje(viajeCreado.getId(), viajeCreado);

        assertEquals("Ezpeleta", viajeEditado.getOrigen());
        assertEquals("Berazategui", viajeEditado.getDestino());
    }

    @Test
    public void editarViajeSiElOrigenNoEsValidoLanzaExcepcionTest() {
        Long viajeId = crearViajeSolicitado("Quilmes", "Bernal").getId();

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
        Long viajeId = crearViajeSolicitado("Quilmes", "Bernal").getId();

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
    public void editarViajeSiElViajeFueAceptadoLanzaExcepcion() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");
        viajeService.aceptarViaje(viajeCreado.getId(), chofer.getId());

        assertThrows(ViajeYaIniciadoException.class, () -> {
            viajeService.editarViaje(viajeCreado.getId(), viajeCreado);
        });
    }

    @Test
    public void editarViajeSiElViajeNoExisteLanzaExcepcionTest() {
        Viaje viajeNoExistente = new Viaje();
        viajeNoExistente.setOrigen("Quilmes");
        viajeNoExistente.setDestino("Bernal");

        assertThrows(ViajeNoEncontradoException.class, () -> {
           viajeService.editarViaje(912L, viajeNoExistente);
        });
    }

    @Test
    public void editarViajeSoloModificaOrigenYDestinoTest() {
        Viaje viajeCreado = crearViajeSolicitado("Quilmes", "Bernal");

        Viaje viajeAEditar = new Viaje();
        viajeAEditar.setId(viajeCreado.getId());
        viajeAEditar.setOrigen("Varela");
        viajeAEditar.setDestino("Bernal");
        viajeAEditar.setKilometros(12.0);
        viajeAEditar.setPrecioFinal(6000.0);
        viajeAEditar.setCliente(usuarioService.crear(new Usuario("juan")));
        viajeAEditar.setChofer(choferService.crear(new Chofer("luis", "ABC123")));
        viajeAEditar.setEstadoViaje(EN_CURSO);

        Viaje viajeEditado = viajeService.editarViaje(viajeCreado.getId(), viajeAEditar);

        assertEquals(viajeCreado.getEstadoViaje(), viajeEditado.getEstadoViaje());
        assertEquals(cliente.getId(), viajeEditado.getCliente().getId());
        assertNull(viajeEditado.getChofer());
        assertEquals(viajeCreado.getPrecioFinal(), viajeEditado.getPrecioFinal());
        assertEquals(viajeCreado.getKilometros(), viajeEditado.getKilometros());

    }

    @Test
    public void unViajeEnEstadoPendienteSeCancelaTest() {
        Viaje viaje = crearViajeSolicitado("Quilmes", "Bernal");
        viajeService.cancelarViaje(viaje.getId());

        Viaje viajeCancelado = viajeService.recuperar(viaje.getId());

        assertEquals(EstadoViaje.CANCELADO, viajeCancelado.getEstadoViaje());
    }

    @Test
    public void unViajeEnEstadoAceptadoSeCancelaTest() {
        Viaje viajeAceptado = crearViajeSolicitado("Quilmes", "Bernal");
        viajeService.aceptarViaje(viajeAceptado.getId(), chofer.getId());
        viajeService.cancelarViaje(viajeAceptado.getId());

        Viaje viajeCancelado = viajeService.recuperar(viajeAceptado.getId());

        assertEquals(EstadoViaje.CANCELADO, viajeCancelado.getEstadoViaje());
    }

    @Test
    public void unViajeEnEstadoEnCursoNoPuedeCancelarseTest() {
        Viaje viajeEnCurso = crearViajeSolicitado("Quilmes", "Bernal");
        viajeService.aceptarViaje(viajeEnCurso.getId(), chofer.getId());
        viajeService.iniciarViaje(viajeEnCurso.getId());

        assertThrows(
                ViajeNoPuedeCancelarseException.class, () ->
                        viajeService.cancelarViaje(viajeEnCurso.getId())
        );
    }

    @Test
    public void viajePendienteNoSeFinaliza(){
        Viaje viaje = viajeService.crear(new Viaje(cliente2, "Bernal", "Avellaneda"));

        assertThrows(
                ViajeNoPuedeFinalizarseException.class, () ->
                        viajeService.finalizarViaje(viaje.getId())
        );

    }

    @Test
    public void viajeEnCursoFinaliza(){
        Viaje viaje = viajeService.crear(new Viaje(cliente2, "Bernal", "Avellaneda"));
        viajeService.aceptarViaje(viaje.getId(), chofer.getId());
        viajeService.iniciarViaje(viaje.getId());

        viajeService.finalizarViaje(viaje.getId());

        Viaje viajeActualizado = viajeService.recuperar(viaje.getId());

        assertEquals(EstadoViaje.FINALIZADO, viajeActualizado.getEstadoViaje());
        assertNull(viajeActualizado.getCliente().getViajeActual());
        assertNull(viajeActualizado.getChofer().getViajeActual());
    }

    @Test
    public void viajeAceptadoInicia(){
        Viaje viajeAceptado = crearViajeSolicitado("Quilmes", "Bernal");
        viajeService.aceptarViaje(viajeAceptado.getId(), chofer.getId());

        viajeService.iniciarViaje(viajeAceptado.getId());

        Viaje viajeIniciado = viajeService.recuperar(viajeAceptado.getId());

        assertEquals(EstadoViaje.EN_CURSO, viajeIniciado.getEstadoViaje());
    }

    @Test
    public void viajeNoAceptadoInicia(){
        Viaje viajeP = crearViajeSolicitado("Quilmes", "Bernal");

        assertThrows(
                ViajeNoPuedeInicializarseException.class, () ->
                        viajeService.iniciarViaje(viajeP.getId())
        );

    }
    @Test
    public void viajeSolicitadoEsAceptadoSeLeAsignaUnChoferTest() {
        Viaje viajeSinChoferCreado = crearViajeSolicitado("Quilmes", "Bernal");

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje viajeRecuperado = viajeService.recuperar(viajeSinChoferCreado.getId());
        Chofer choferRecuperado = choferService.recuperar(chofer.getId());

        assertEquals(viajeRecuperado.getChofer().getId(), choferRecuperado.getId());
    }

    @Test
    public void cuandoUnViajeEsAceptadoSuEstadoCambiaAAceptadoTest() {
        Viaje viajeSinChoferCreado = crearViajeSolicitado("Quilmes", "Bernal");

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje viajeRecuperado = viajeService.recuperar(viajeSinChoferCreado.getId());

        assertEquals(EstadoViaje.ACEPTADO, viajeRecuperado.getEstadoViaje());
    }

    @Test
    public void aceptarViajeParaUnViajeQueYaFueAceptadoLanzaExcepcionTest() {
        Viaje viajeSinChoferCreado = crearViajeSolicitado("Quilmes", "Bernal");

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Chofer chofer2 = crearChofer("Nova", "QQQ 666");

        assertThrows(ViajeNoPuedeSerAceptadoException.class, () -> viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer2.getId()));
    }

    @Test
    public void aceptarViajeParaUnChoferQueYaTieneUnViajeAsignadoLanzaExcepcionTest() {
        Viaje viajeSinChoferCreado = crearViajeSolicitado("Quilmes", "Bernal");

        viajeService.aceptarViaje(viajeSinChoferCreado.getId(), chofer.getId());

        Viaje otroViajeSinChoferCreado = viajeService.crear(new Viaje(cliente2, "Bernal", "Avellaneda"));


        assertThrows(ViajeNoPuedeSerAceptadoException.class, () -> viajeService.aceptarViaje(otroViajeSinChoferCreado.getId(), chofer.getId()));
    }

    @Test
    public void unViajeInicialmenteNoTieneCalificacionTest() {
        Viaje viajeSolicitado = crearViajeSolicitado("Quilmes", "Bernal");

        assertNull(viajeSolicitado.getCalificacion());
    }

    @Test
    public void unViajeEsCalificadoTest() {
        Viaje viajeSolicitado = crearViajeSolicitado("Quilmes", "Bernal");
        Chofer chofer = crearChofer("Nova", "QQQ 666");

        viajeService.aceptarViaje(viajeSolicitado.getId(), chofer.getId());

        viajeService.iniciarViaje(viajeSolicitado.getId());

        viajeService.finalizarViaje(viajeSolicitado.getId());

        viajeService.calificarViaje(viajeSolicitado.getId(), viajeSolicitado.getCliente().getId(), 7.5);

        Viaje viajeCalificado = viajeService.recuperar(viajeSolicitado.getId());

        assertEquals(7.5, viajeCalificado.getCalificacion());

    }

    @Test
    public void calificarViajeDeUnClienteDistintoLanzaExcepcionTest() {
        Viaje viajeSolicitado = crearViajeSolicitado("Quilmes", "Bernal");
        Chofer chofer = crearChofer("Nova", "QQQ 666");

        viajeService.aceptarViaje(viajeSolicitado.getId(), chofer.getId());

        viajeService.iniciarViaje(viajeSolicitado.getId());

        viajeService.finalizarViaje(viajeSolicitado.getId());

        Usuario cliente2 = crearUsuario("Juan");

        assertThrows(ViajeNoPuedeSerCalificadoException.class, () -> viajeService.calificarViaje(viajeSolicitado.getId(), cliente2.getId(), 3.5));
    }

    @Test
    public void calificarViajeDeUnViajeQueNoFinalizoLanzaExcepcionTest() {
        Viaje viajeSolicitado = crearViajeSolicitado("Quilmes", "Bernal");
        Chofer chofer = crearChofer("Nova", "QQQ 666");

        viajeService.aceptarViaje(viajeSolicitado.getId(), chofer.getId());

        viajeService.iniciarViaje(viajeSolicitado.getId());

        assertThrows(ViajeNoPuedeSerCalificadoException.class, () -> viajeService.calificarViaje(viajeSolicitado.getId(), viajeSolicitado.getCliente().getId(), 3.5));
    }

    @Test
    public void inicialmenteNoHayViajesSolicitadosTest() {
        assertTrue(viajeService.viajesSolicitados().isEmpty());
    }

    @Test
    public void cuandoUnClienteSolicitaUnViajeSeAgregaAViajesSolicitadosTest() {
        crearViajeSolicitado("Quilmes", "Bernal");

        assertFalse(viajeService.viajesSolicitados().isEmpty());
        assertEquals(viajeService.viajesSolicitados().size(), 1);
    }

    @Test
    public void cuandoUnChoferAceptaUnViajeSolicitadoElViajeDejaDeEstarEnViajesSolicitadosTest() {
        Viaje viajeSolicitado = crearViajeSolicitado("Quilmes", "Bernal");
        Chofer chofer = crearChofer("Nova", "QQQ 666");

        viajeService.aceptarViaje(viajeSolicitado.getId(), chofer.getId());

        assertTrue(viajeService.viajesSolicitados().isEmpty());
    }

    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }


}
