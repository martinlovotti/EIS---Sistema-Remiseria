package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChoferServiceImpl implements ChoferService {

    private final ChoferDAO choferDAO;

    public ChoferServiceImpl(ChoferDAO choferDAO) {
        this.choferDAO = choferDAO;
    }

    @Override
    public Chofer crear(Chofer chofer) {
        Chofer choferCreado = choferDAO.save(chofer);
        chofer.setId(choferCreado.getId());
        return chofer;
    }

    @Override
    public Chofer recuperar(Long id) {
        return choferDAO.recuperar(id);
    }

    @Override
    public void eliminar(Long id) {
        choferDAO.eliminar(id);
    }

    @Override
    public List<Chofer> recuperarTodos() {
        return new ArrayList<>(choferDAO.recuperarTodos());
    }
}