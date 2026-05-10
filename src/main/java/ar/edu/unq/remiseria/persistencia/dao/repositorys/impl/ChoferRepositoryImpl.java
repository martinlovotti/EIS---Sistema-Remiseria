package ar.edu.unq.remiseria.persistencia.dao.repositorys.impl;

import ar.edu.unq.remiseria.exception.ChoferNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ChoferRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChoferRepositoryImpl implements ChoferRepository {

    private final ChoferDAO choferDAO;

    public ChoferRepositoryImpl(ChoferDAO c){
        this.choferDAO = c;
    }

    @Override
    public Chofer crear(Chofer c) {
        ChoferSQL choferSQL = ChoferSQL.creadDesde(c);
        choferDAO.crear(choferSQL);
        c.setId(choferSQL.getId());
        return c;
    }

    @Override
    public Chofer recuperar(Long id) {
        ChoferSQL sql = choferDAO.recuperar(id);
        return choferToModel(sql);
    }

    @Override
    public void eliminar(Long id) {
        choferDAO.eliminar(id);
    }

    @Override
    public List<Chofer> recuperarTodos() {
        List<ChoferSQL> choferesSQL = choferDAO.recuperarTodos();

        return  choferesSQL.stream()
                .map( sql ->{
                    ChoferSQL ch = choferDAO.findById(sql.getId())
                            .orElseThrow(() -> new ChoferNoEncontradoException("Chofer no encontrado con id: " + sql.getId()));
                    return choferToModel(ch);
                })
                .collect(Collectors.toList());
    }

    public Chofer choferToModel(ChoferSQL c){
        Chofer chofer = new Chofer();
        chofer.setNombre(c.getNombre());
        chofer.setPatente(c.getPatente());
        return chofer;
    }
}