package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ViajeServideImplTest {

    @Autowired
    private ViajeService viajeService;

    private Usuario cliente;
    private Chofer chofer;
    private Viaje viaje;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        chofer = new Chofer();
        viaje = new Viaje(cliente, chofer);
        viaje.setOrigen("Quilmes");
        viaje.setDestino("Bernal");
        viaje.setKilometros(6.2);
        viaje.setPrecioFinal(4500.0);

    }

    @Test
    public void editarViaje() {
        Viaje viajeCreado = viajeService.crearViaje(viaje);
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
