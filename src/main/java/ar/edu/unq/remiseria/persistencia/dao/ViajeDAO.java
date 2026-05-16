package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ViajeNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeDAO extends JpaRepository<ViajeSQL, Long> {

    default ViajeSQL recuperar(Long id) {
        return findById(id).orElseThrow(ViajeNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        deleteById(id);
    }

    default List<ViajeSQL> recuperarTodos() {
        return findAll();
    }

    default void editar(ViajeSQL viaje) {
        save(viaje);
    }
}
