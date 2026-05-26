package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.mapper.ChoferMapper;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.testService.TestService;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UsuarioServiceImplTest {
    @Autowired
    private UsuarioService service;
    @Autowired
    private TestService testService;
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private ViajeService viajeService;
    @Autowired
    private ChoferDAO choferDAO;
    @Autowired
    private ChoferMapper choferMapper;

    private Usuario nuevoUsuario() {
        return new Usuario("aaa");
    }

    private Usuario u;
    private Chofer chofer;

    @BeforeEach
    void prepare() {
        usuarioDAO.deleteAll();
        choferDAO.deleteAll();
        u = new Usuario("aaa");
        chofer = new Chofer();
        chofer.setNombre("Juan");
        chofer.setPatente("ABC123");
        choferDAO.save(choferMapper.fromModel(chofer));
    }

    @Test
    void guardar() {
        Usuario user = service.crear(nuevoUsuario());
        assertNotNull(user.getId());
        assertEquals("aaa", user.getNombre());
    }

    @Test
    void eliminar() {
        Usuario user = service.crear(nuevoUsuario());
        service.eliminar(user.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }

    @Test
    void recuperarViajesPorEstado() {
        Usuario user = service.crear(u);

        Viaje viaje1 = new Viaje(user, "Quilmes", "Bernal");
        viajeService.crear(viaje1);

        Usuario user2 = service.crear(new Usuario("bbb"));
        Viaje viaje2 = new Viaje(user2, "Avellaneda", "Lanus");
        Viaje viaje2Creado = viajeService.crear(viaje2);
        viajeService.cancelarViaje(viaje2Creado.getId());

        List<Viaje> viajesPendientes = service.recuperarViajesPorEstado(user.getId(), EstadoViaje.PENDIENTE);
        List<Viaje> viajesCancelados = service.recuperarViajesPorEstado(user2.getId(), EstadoViaje.CANCELADO);

        assertEquals(1, viajesPendientes.size());
        assertEquals(EstadoViaje.PENDIENTE, viajesPendientes.getFirst().getEstadoViaje());

        assertEquals(1, viajesCancelados.size());
        assertEquals(EstadoViaje.CANCELADO, viajesCancelados.getFirst().getEstadoViaje());
    }
}