package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.servicios.interfaces.DistanciaService;
import ar.edu.unq.remiseria.testService.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class DistanciaServiceImplTest {

    @Autowired
    private TestService testService;

    @Autowired
    private DistanciaService distanciaService = new DistanciaServiceImpl();

    @Test
    void testCalcularDistanciaKm() {
        // Direcciones reales en Buenos Aires
        String origen = "Av. Corrientes 2000, Buenos Aires, Argentina";
        String destino = "Av. Libertador 750, Buenos Aires, Argentina";

        Double distanciaKm = distanciaService.calcularDistanciaKm(origen, destino);

        assertNotNull(distanciaKm);
        assertTrue(distanciaKm > 0);

        // Rango esperado aproximado (depende del tráfico y ruta)
        assertTrue(distanciaKm > 2 && distanciaKm < 6,
                "La distancia debería estar entre 2 y 6 km");
    }


    @AfterEach
    void cleanup() {
        testService.cleanUp();
    }


}