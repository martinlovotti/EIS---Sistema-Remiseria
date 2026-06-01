package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ViajeNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT v.chofer FROM Viaje v GROUP BY v.chofer ORDER BY COUNT(v) DESC LIMIT 1")
    Optional<ChoferSQL> recuperarChoferConMasViajes();

    @Query("SELECT v.chofer FROM Viaje v WHERE v.estadoViaje = 'FINALIZADO' GROUP BY v.chofer ORDER BY SUM(v.kilometros) DESC LIMIT 1")
    Optional<ChoferSQL> recuperarChoferConMasKm();

    @Query("SELECT v.chofer FROM Viaje v WHERE v.estadoViaje = 'FINALIZADO' GROUP BY v.chofer ORDER BY SUM(v.precioFinal) DESC LIMIT 1")
    Optional<ChoferSQL> recuperarChoferConMasFacturacion();
}
