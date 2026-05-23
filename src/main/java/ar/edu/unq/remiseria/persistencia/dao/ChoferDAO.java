package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.exception.ChoferNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ChoferDAO extends JpaRepository<Chofer, Long> {

    default Chofer recuperar(Long id) {
        return findById(id).orElseThrow(ChoferNoEncontradoException::new);
    }

    default void eliminar(Long id) {
        if (!existsById(id)) {
            throw new ChoferNoEncontradoException();
        }
        deleteById(id);
    }

    default List<Chofer> recuperarTodos() {
        return findAll();
    }
}