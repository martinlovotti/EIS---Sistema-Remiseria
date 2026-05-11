package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChoferServiceImplTest {
    @Autowired
    private ChoferService service;

    private Chofer c;
    @Autowired
    private ChoferDAO choferDAO;

    @BeforeEach
    void prepare(){
        choferDAO.deleteAll();
        c = new Chofer("juanito", "abc123");
    }

    @Test
    void guardar(){
        Chofer chofer = service.crear(c);
        assertNotNull(chofer.getId());
    }

    @Test
    void eliminar(){
        Chofer chofer = service.crear(c);
        service.eliminar(chofer.getId());
        assertEquals(0, service.recuperarTodos().size() );
    }

}