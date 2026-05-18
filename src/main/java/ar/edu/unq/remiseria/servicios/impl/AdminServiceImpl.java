package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.NoHayChoferesException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.mapper.ChoferMapper;
import ar.edu.unq.remiseria.servicios.interfaces.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final ViajeDAO viajeDAO;
    private final ChoferMapper choferMapper;

    public AdminServiceImpl(ViajeDAO viajeDAO, ChoferMapper choferMapper) {
        this.viajeDAO = viajeDAO;
        this.choferMapper = choferMapper;
    }

    @Override
    public Chofer conMasViajes() {
        return viajeDAO.recuperarChoferConMasViajes()
                .map(choferMapper::toModel)
                .orElseThrow(NoHayChoferesException::new);
    }
}
