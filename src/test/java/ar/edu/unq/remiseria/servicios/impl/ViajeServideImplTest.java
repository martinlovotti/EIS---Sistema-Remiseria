package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
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

    private Usuario cliente;
    private Chofer chofer;
    private Viaje viaje;
    private Viaje viajeSinChofer;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setNombre("Pepe");
        usuarioService.crear(cliente);
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
        viajeService.crear(viaje);
        assertNotNull(viaje.getId());
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
    public void editarViaje() {
        Viaje viajeCreado = viajeService.crear(viaje);
        viaje.setOrigen("Ezpeleta");
        viaje.setDestino("Berazategui");
        viaje.setKilometros(2.5);
        viaje.setPrecioFinal(3300.0);

        viajeService.editarViaje(viaje, viajeCreado.getId());

        viaje = viajeService.recuperar(viajeCreado.getId());
        assertEquals("Ezpeleta", viaje.getOrigen());
        assertEquals("Berazategui", viaje.getDestino());
        assertEquals(2.5, viaje.getKilometros());
        assertEquals(3300.0, viaje.getPrecioFinal());

    };

}
