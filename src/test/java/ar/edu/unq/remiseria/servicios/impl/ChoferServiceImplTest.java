package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.testService.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ChoferServiceImplTest {
    @Autowired
    private ChoferService service;

    @Autowired
    private TestService testService;

    private Chofer nuevoChofer() {
        return new Chofer("juanito", "abc123");
    }

    @Test
    void guardar(){
        Chofer chofer = service.crear(nuevoChofer());
        assertNotNull(chofer.getId());
        assertEquals("juanito", chofer.getNombre());
        assertEquals("abc123", chofer.getPatente());
    }

    @Test
    void eliminar(){
        Chofer chofer = service.crear(nuevoChofer());
        service.eliminar(chofer.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }


}