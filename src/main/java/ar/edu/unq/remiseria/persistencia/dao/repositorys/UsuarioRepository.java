package ar.edu.unq.remiseria.persistencia.dao.repositorys;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.dao.entity.UsuarioSQL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository{
    Usuario crear(Usuario u);
    Usuario recuperar(Long id);
    void eliminar(Long id);
    List<Usuario> recuperarTodos();
}
