package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.UsuarioNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioDAO extends JpaRepository<UsuarioSQL, Long>{

    default UsuarioSQL recuperar(Long id){
        return findById(id).orElseThrow(UsuarioNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new UsuarioNoEncontradoException();
        }
        deleteById(id);
    }

    default List<UsuarioSQL> recuperarTodos() {
        return findAll();
    }

    @Modifying
    @Query("UPDATE Usuario u SET u.viajeActual = null")
    void desvincularViajes();

    @Query("SELECT v FROM Viaje v WHERE v.cliente.id = :usuarioId AND v.estadoViaje = :estado ORDER BY v.fechaCreacion DESC")
    List<ViajeSQL> findViajesByClienteAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") EstadoViaje estado);

    @Query("SELECT v FROM Viaje v WHERE v.cliente.id = :usuarioId ORDER BY v.fechaCreacion DESC")
    List<ViajeSQL> findViajesByCliente(@Param("usuarioId") Long usuarioId);
}
