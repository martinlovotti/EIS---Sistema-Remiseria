package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Usuario crear(Usuario usuario) {
        Usuario usuarioCreado = usuarioDAO.save(usuario);
        usuario.setId(usuarioCreado.getId());
        return usuario;
    }

    @Override
    public Usuario recuperar(Long id) {
        return (usuarioDAO.recuperar(id));
    }

    @Override
    public void eliminar(Long id) {
        usuarioDAO.eliminar(id);
    }

    @Override
    public List<Usuario> recuperarTodos() {
        return new ArrayList<>(usuarioDAO.recuperarTodos());
    }
}
