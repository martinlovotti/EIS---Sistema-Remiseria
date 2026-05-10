package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.UsuarioNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.dao.entity.UsuarioSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioDAO extends JpaRepository<UsuarioSQL, Long>{

    default UsuarioSQL crear(UsuarioSQL e){
        return save(e);
    }

    default UsuarioSQL recuperar(Long id){
        return findById(id).orElseThrow();
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new UsuarioNoEncontradoException("user not found");
        }
        deleteById(id);
    }

    default List<UsuarioSQL> recuperarTodos() {
        return findAll();
    };
}
