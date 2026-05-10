package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ChoferRepository;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.impl.ChoferRepositoryImpl;
import ar.edu.unq.remiseria.servicios.interfaces.ChoferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class ChoferServiceImpl implements ChoferService {

    private final ChoferRepository choferRepository;

    public ChoferServiceImpl(ChoferRepository c){
        this.choferRepository = c;
    }

    @Override
    public Chofer crear(Chofer c) {
        return choferRepository.crear(c);
    }

    @Override
    public Chofer recuperar(Long id) {
        return choferRepository.recuperar(id);
    }

    @Override
    public void eliminar(Long id) {
        choferRepository.eliminar(id);
    }

    @Override
    public List<Chofer> recuperarTodos() {
        return choferRepository.recuperarTodos();
    }
}