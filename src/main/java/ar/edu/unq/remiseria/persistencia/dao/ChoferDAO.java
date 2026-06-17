package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ChoferNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ChoferDAO extends JpaRepository<ChoferSQL, Long> {

    default ChoferSQL recuperar(Long id) {
        return findById(id).orElseThrow(ChoferNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new ChoferNoEncontradoException();
        }
        deleteById(id);
    }

    default List<ChoferSQL> recuperarTodos() {
        return findAll();
    }

    @Modifying
    @Query("UPDATE Chofer c SET c.viajeActual = null")
    void desvincularViajes();
}