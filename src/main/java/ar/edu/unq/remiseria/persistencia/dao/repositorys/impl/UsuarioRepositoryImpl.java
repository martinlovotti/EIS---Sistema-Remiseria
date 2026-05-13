package ar.edu.unq.remiseria.persistencia.dao.repositorys.impl;

import ar.edu.unq.remiseria.exception.UsuarioNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.dao.entity.ViajeSQL;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioDAO usuarioDAO;

    public UsuarioRepositoryImpl(UsuarioDAO u){
        this.usuarioDAO = u;
    }

    @Override
    public Usuario crear(Usuario u) {
        UsuarioSQL usuarioSQL = UsuarioSQL.crearDesde(u);
        usuarioDAO.crear(usuarioSQL);
        u.setId(usuarioSQL.getId());
        return u;
    }

    @Override
    public Usuario recuperar(Long id) {
        UsuarioSQL sql = usuarioDAO.recuperar(id);
        return sql.toModel();
    }

    @Override
    public void eliminar(Long id) {
        usuarioDAO.eliminar(id);
    }

    @Override
    public List<Usuario> recuperarTodos() {
        List<UsuarioSQL> usuariosSQL = usuarioDAO.recuperarTodos();

        return  usuariosSQL.stream()
                .map( sql ->{
                    UsuarioSQL usuario = usuarioDAO.findById(sql.getId())
                            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id: " + sql.getId()));
                    return usuario.toModel();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Usuario usuario) {
        UsuarioSQL usuarioSQL = usuarioDAO.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException("No existe usuario"));

        usuarioSQL.setNombre(usuario.getNombre());
        ViajeSQL viajeSQL = ViajeSQL.from(usuario.getViajeActual());
        viajeSQL.setCliente(usuarioSQL);
        usuarioSQL.setViajeActual(viajeSQL);

        usuarioDAO.save(usuarioSQL);
    }
}
