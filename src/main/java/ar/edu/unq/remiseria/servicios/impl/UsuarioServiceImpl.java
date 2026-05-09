package ar.edu.unq.remiseria.servicios.impl;


import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.UsuarioRepository;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.impl.UsuarioRepositoryImpl;
import ar.edu.unq.remiseria.servicios.interfaces.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository u){
        this.usuarioRepository = u;
    }

    @Override
    public Usuario crear(Usuario u) {
        return usuarioRepository.crear(u);
    }

    @Override
    public Usuario recuperar(Long id) {
        return usuarioRepository.recuperar(id);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.eliminar(id);
    }

    @Override
    public List<Usuario> recuperarTodos() {
        return usuarioRepository.recuperarTodos();
    }
}
