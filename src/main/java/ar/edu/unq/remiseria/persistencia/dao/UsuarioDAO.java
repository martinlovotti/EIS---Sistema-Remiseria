package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.UsuarioNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long>{

    default Usuario recuperar(Long id){
        return findById(id).orElseThrow(UsuarioNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new UsuarioNoEncontradoException();
        }
        deleteById(id);
    }

    default List<Usuario> recuperarTodos() {
        return findAll();
    }
}
