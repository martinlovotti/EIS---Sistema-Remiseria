package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.mapper.UsuarioMapper;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO, UsuarioMapper usuarioMapper) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario crear(Usuario usuario) {
        UsuarioSQL usuarioCreado = usuarioDAO.save(usuarioMapper.fromModel(usuario));
        usuario.setId(usuarioCreado.getId());
        return usuario;
    }

    @Override
    public Usuario recuperar(Long id) {
        return usuarioMapper.toModel(usuarioDAO.recuperar(id));
    }

    @Override
    public void eliminar(Long id) {
        usuarioDAO.eliminar(id);
    }

    @Override
    public List<Usuario> recuperarTodos() {
        return usuarioDAO.recuperarTodos().stream()
            .map(usuarioMapper::toModel)
                .collect(Collectors.toList());
    }
}
