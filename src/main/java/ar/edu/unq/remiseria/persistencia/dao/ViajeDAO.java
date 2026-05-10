package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.entity.ViajeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeDAO extends JpaRepository<ViajeSQL, Long> {
}
