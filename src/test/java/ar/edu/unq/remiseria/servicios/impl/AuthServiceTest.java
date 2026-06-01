package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.controller.dto.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.RegisterRequestDTO;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.security.Role;
import ar.edu.unq.remiseria.servicios.interfaces.AuthService;
import ar.edu.unq.remiseria.testService.TestService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthDAO dao;
    @Autowired
    private TestService testService;

    @Test
    void registerGuardaUsuarioEnDB() {

        RegisterRequestDTO req = new RegisterRequestDTO("juan", "1234", Role.USUARIO);

        authService.register(req);

        Optional<AuthUser> user = dao.findByUsername("juan");

        assertTrue(user.isPresent());
        assertEquals("juan", user.get().getUsername());
    }

    @Test
    void registerFallaSiUsuarioExiste() {

        RegisterRequestDTO req = new RegisterRequestDTO("juan", "1234", Role.USUARIO);

        authService.register(req);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(req);
        });

        assertEquals("Usuario ya existe", ex.getMessage());
    }

    @Test
    void loginRetornaUnToken() {

        RegisterRequestDTO reg = new RegisterRequestDTO("ana", "1234", Role.USUARIO);

        authService.register(reg);

        LoginRequestDTO login = new LoginRequestDTO("ana", "1234");

        String token = authService.login(login);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void loginFallaSiUsuarioNoExiste() {

        LoginRequestDTO login = new LoginRequestDTO("inexistente", "1234");

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            authService.login(login);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void loginFallaSiLaContraseñaEsIncorrecta() {

        RegisterRequestDTO reg = new RegisterRequestDTO("carlos", "1234", Role.USUARIO);

        authService.register(reg);

        LoginRequestDTO login = new LoginRequestDTO("carlos", "wrong");


        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(login);
        });

        assertEquals("Password incorrecto", ex.getMessage());
    }

    @AfterEach
    void cleanUp() {
        testService.cleanUp();
    }
}