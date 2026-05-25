package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.UsuarioNoEncontradoException;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
