package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UsuarioServiceImplTest {
    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioDAO usuarioDAO;

    private Usuario u;

    @BeforeEach
    void prepare(){
        usuarioDAO.deleteAll();
        u = new Usuario("jaime");
    }

    @Test
    void guardar(){
        Usuario user = service.crear(u);
        assertNotNull(user.getId());
    }

    @Test
    void eliminar(){
        Usuario user = service.crear(u);
        service.eliminar(user.getId());
        assertEquals(0, service.recuperarTodos().size() );
    }
}