package ar.edu.unq.remiseria.persistencia.dao.repositorys;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.entity.ChoferSQL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoferRepository{
    Chofer crear(Chofer c);
    Chofer recuperar(Long id);
    void eliminar(Long id);
    List<Chofer> recuperarTodos();
}