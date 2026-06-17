package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.mapper.ChoferMapper;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChoferServiceImpl implements ChoferService {

    private final ChoferDAO choferDAO;
    private final ChoferMapper choferMapper;

    public ChoferServiceImpl(ChoferDAO choferDAO, ChoferMapper choferMapper) {
        this.choferDAO = choferDAO;
        this.choferMapper = choferMapper;
    }

    @Override
    public Chofer crear(Chofer chofer) {
        ChoferSQL choferCreado = choferDAO.save(choferMapper.fromModel(chofer));
        chofer.setId(choferCreado.getId());
        return chofer;
    }

    @Override
    public Chofer recuperar(Long id) {
        return choferMapper.toModel(choferDAO.recuperar(id));
    }

    @Override
    public void eliminar(Long id) {
        choferDAO.eliminar(id);
    }

    @Override
    public List<Chofer> recuperarTodos() {
        return choferDAO.recuperarTodos().stream()
            .map(choferMapper::toModel)
                .collect(Collectors.toList());
    }
}