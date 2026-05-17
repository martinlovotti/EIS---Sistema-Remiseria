package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.ViajeNoPuedeCancelarseException;
import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerAceptadoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    public void editarViaje() {
        Viaje viajeCreado = viajeService.crear(viaje);
        viajeCreado.setOrigen("Ezpeleta");
        viajeCreado.setDestino("Berazategui");
        viajeCreado.setKilometros(2.5);
        viajeCreado.setPrecioFinal(3300.0);

        viajeService.editarViaje(viajeCreado);

        viaje = viajeService.recuperar(viajeCreado.getId());
        assertEquals("Ezpeleta", viaje.getOrigen());
        assertEquals("Berazategui", viaje.getDestino());
        assertEquals(2.5, viaje.getKilometros());
        assertEquals(3300.0, viaje.getPrecioFinal());

    };

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
        viajeEnCurso.setEstadoViaje(EstadoViaje.EN_CURSO);
        Viaje viaje = viajeService.crear(viajeEnCurso);

        viajeService.finalizarViaje(viaje.getId());

        Viaje viajeActualizado = viajeService.recuperar(viaje.getId());

        assertEquals(EstadoViaje.FINALIZADO, viajeActualizado.getEstadoViaje());
        assertEquals(null, viajeActualizado.getCliente().getViajeActual());
        assertEquals(null, viajeActualizado.getChofer().getViajeActual());
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
