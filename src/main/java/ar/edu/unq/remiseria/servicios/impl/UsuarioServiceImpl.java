package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.mapper.UsuarioMapper;
import ar.edu.unq.remiseria.persistencia.mapper.ViajeMapper;
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
    private final ViajeMapper viajeMapper;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO, UsuarioMapper usuarioMapper, ViajeMapper viajeMapper) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioMapper = usuarioMapper;
        this.viajeMapper = viajeMapper;
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
        return usuarioDAO.recuperarTodos().stream().map(usuarioMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Viaje> recuperarViajesPorEstado(Long usuarioId, EstadoViaje estado) {
        usuarioDAO.recuperar(usuarioId); // valida que el usuario existe
        return usuarioDAO.findViajesByClienteAndEstado(usuarioId, estado).stream().map(viajeMapper::toModel).collect(Collectors.toList());
    }
}
