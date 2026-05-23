package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ViajeNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeDAO extends JpaRepository<Viaje, Long> {

    default Viaje recuperar(Long id) {
        return findById(id).orElseThrow(ViajeNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        deleteById(id);
    }

    default List<Viaje> recuperarTodos() {
        return findAll();
    }

    default void editar(Viaje viaje) {
        save(viaje);
    }

    @Query("SELECT v.chofer FROM Viaje v GROUP BY v.chofer ORDER BY COUNT(v) DESC LIMIT 1")
    Optional<Chofer> recuperarChoferConMasViajes();
}
