package ar.edu.unq.remiseria.testService;

import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestServiceConfig {
    @Bean
    public TestService testService(ChoferDAO choferDAO, ViajeDAO viajeDAO, UsuarioDAO usuarioDAO, AuthDAO authDAO) {
        return new TestServiceImpl(choferDAO, viajeDAO, usuarioDAO, authDAO);
    }
}