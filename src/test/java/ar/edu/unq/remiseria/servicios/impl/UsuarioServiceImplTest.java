package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import ar.edu.unq.remiseria.testService.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UsuarioServiceImplTest {
    @Autowired
    private UsuarioService service;
    @Autowired
    private TestService testService;

    private Usuario nuevoUsuario() {
        return new Usuario("aaa");
    }

    @Test
    void guardar(){
        Usuario user = service.crear(nuevoUsuario());
        assertNotNull(user.getId());
        assertEquals("aaa", user.getNombre());
    }

    @Test
    void eliminar(){
        Usuario user = service.crear(nuevoUsuario());
        service.eliminar(user.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }
}