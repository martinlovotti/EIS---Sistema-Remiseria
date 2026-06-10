package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.LoginResponseDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterChoferRequestDTO;
import ar.edu.unq.remiseria.controller.dto.AuthDTO.RegisterUsuarioRequestDTO;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.security.JwtUtil;
import ar.edu.unq.remiseria.security.Role;
import ar.edu.unq.remiseria.servicios.interfaces.AuthService;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthDAO authDAO;
    private final UsuarioService usuarioService;
    private final ChoferService choferService;
    private final BCryptPasswordEncoder encoder;

    public AuthServiceImpl(
            AuthDAO authDAO,
            UsuarioService usuarioService,
            ChoferService choferService,
            BCryptPasswordEncoder encoder
    ) {
        this.authDAO = authDAO;
        this.usuarioService = usuarioService;
        this.choferService = choferService;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void registerUsuario(RegisterUsuarioRequestDTO request) {
        validarUsernameDisponible(request.username());

        Usuario usuario = new Usuario(request.nombre());

        Usuario usuarioGuardado = usuarioService.crear(usuario);

        AuthUser authUser = new AuthUser(
                request.username(),
                encoder.encode(request.password()),
                Role.USUARIO,
                usuarioGuardado.getId()
        );
        authDAO.save(authUser);
    }

    @Override
    @Transactional
    public void registerChofer(RegisterChoferRequestDTO request) {
        validarUsernameDisponible(request.username());

        Chofer chofer = new Chofer(request.nombre(), request.patente());

        Chofer choferGuardado = choferService.crear(chofer);

        AuthUser authUser = new AuthUser(
                request.username(),
                encoder.encode(request.password()),
                Role.CHOFER,
                choferGuardado.getId()
        );

        authDAO.save(authUser);
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        AuthUser user = authDAO.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Password incorrecto");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        return new LoginResponseDTO(token, user.getEntidadId(), user.getRole());
    }

    private void validarUsernameDisponible(String username) {
        if (authDAO.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
    }
}
