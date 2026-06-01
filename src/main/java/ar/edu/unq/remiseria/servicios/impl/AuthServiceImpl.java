package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.controller.dto.LoginRequestDTO;
import ar.edu.unq.remiseria.controller.dto.RegisterRequestDTO;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.security.JwtUtil;
import ar.edu.unq.remiseria.servicios.interfaces.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthDAO dao;
    private final BCryptPasswordEncoder encoder;

    public AuthServiceImpl(AuthDAO dao, BCryptPasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }

    public void register(RegisterRequestDTO request) {

        if (dao.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }

        AuthUser user = new AuthUser();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role());

        dao.save(user);
    }

    public String login(LoginRequestDTO request) {

        AuthUser user = dao.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Password incorrecto");
        }

        return JwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}
