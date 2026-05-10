package ar.edu.unq.remiseria.persistencia.dao.repositorys;

import ar.edu.unq.remiseria.modelo.Usuario;

import java.util.List;

public interface UsuarioRepository{
    Usuario crear(Usuario u);
    Usuario recuperar(Long id);
    void eliminar(Long id);
    List<Usuario> recuperarTodos();
}
