package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ChoferNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.dao.entity.ChoferSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ChoferDAO extends JpaRepository<ChoferSQL, Long> {

    default ChoferSQL crear(ChoferSQL e) {
        return save(e);
    }

    default ChoferSQL recuperar(Long id) {
        return findById(id).orElseThrow();
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new ChoferNoEncontradoException("chofer not found");
        }
        deleteById(id);
    }

    default List<ChoferSQL> recuperarTodos() {
        return findAll();
    }
}