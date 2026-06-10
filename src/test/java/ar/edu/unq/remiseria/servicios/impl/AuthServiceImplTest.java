package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginResponseDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterUsuarioRequestDTO;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.security.JwtUtil;
import ar.edu.unq.remiseria.security.Role;
import ar.edu.unq.remiseria.servicios.interfaces.AuthService;
import ar.edu.unq.remiseria.testService.TestService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthDAO authDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private TestService testService;

    @Test
    void registerUsuarioGuardaAuthUserConRoleYEntidadId() {
        String username = "juan_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO req =
                new RegisterUsuarioRequestDTO(username, "1234", "Juan Perez");

        authService.registerUsuario(req);

        Optional<AuthUser> authUserOpt = authDAO.findByUsername(username);

        assertTrue(authUserOpt.isPresent());

        AuthUser authUser = authUserOpt.get();

        assertEquals(username, authUser.getUsername());
        assertEquals(Role.USUARIO, authUser.getRole());
        assertNotNull(authUser.getEntidadId());

        Optional<UsuarioSQL> usuarioOpt = usuarioDAO.findById(authUser.getEntidadId());
        assertTrue(usuarioOpt.isPresent());

        UsuarioSQL usuario = usuarioOpt.get();
        assertEquals("Juan Perez", usuario.getNombre());
    }

    @Test
    void registerUsuarioGuardaPasswordEncriptada() {
        String username = "juan_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO req =
                new RegisterUsuarioRequestDTO(username, "1234", "Juan Perez");

        authService.registerUsuario(req);

        AuthUser authUser = authDAO.findByUsername(username).orElseThrow();

        assertNotEquals("1234", authUser.getPassword());
        assertTrue(encoder.matches("1234", authUser.getPassword()));
    }

    @Test
    void registerUsuarioFallaSiUsernameYaExiste() {
        String username = "juan_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO req =
                new RegisterUsuarioRequestDTO(username, "1234", "Juan Perez");

        authService.registerUsuario(req);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.registerUsuario(req)
        );

        assertEquals("Usuario ya existe", ex.getMessage());
    }

    @Test
    void loginRetornaTokenRoleYEntidadIdCorrectos() {
        String username = "ana_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO reg =
                new RegisterUsuarioRequestDTO(username, "1234", "Ana Lopez");

        authService.registerUsuario(reg);

        AuthUser authUser = authDAO.findByUsername(username).orElseThrow();

        LoginRequestDTO login = new LoginRequestDTO(username, "1234");
        LoginResponseDTO response = authService.login(login);

        assertNotNull(response);
        assertNotNull(response.token());
        assertFalse(response.token().isBlank());

        assertEquals(Role.USUARIO, response.role());
        assertEquals(authUser.getEntidadId(), response.entidadId());
        assertNotNull(response.entidadId());

        Optional<UsuarioSQL> usuarioOpt = usuarioDAO.findById(response.entidadId());
        assertTrue(usuarioOpt.isPresent());
    }

    @Test
    void loginRetornaTokenConUsernameYRoleCorrectos() {
        String username = "ana_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO reg =
                new RegisterUsuarioRequestDTO(username, "1234", "Ana Lopez");

        authService.registerUsuario(reg);

        LoginRequestDTO login = new LoginRequestDTO(username, "1234");
        LoginResponseDTO response = authService.login(login);

        Claims claims = JwtUtil.getClaims(response.token());

        assertEquals(username, claims.getSubject());
        assertEquals("USUARIO", claims.get("role", String.class));
    }

    @Test
    void loginFallaSiUsuarioNoExiste() {
        LoginRequestDTO login = new LoginRequestDTO("inexistente", "1234");

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> authService.login(login)
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void loginFallaSiPasswordEsIncorrecta() {
        String username = "ana_" + UUID.randomUUID();
        RegisterUsuarioRequestDTO reg =
                new RegisterUsuarioRequestDTO(username, "1234", "Ana Lopez");

        authService.registerUsuario(reg);

        LoginRequestDTO login = new LoginRequestDTO(username, "wrong");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(login)
        );

        assertEquals("Password incorrecto", ex.getMessage());
    }

    @AfterEach
    void cleanUp() {
        testService.cleanUp();
    }
}