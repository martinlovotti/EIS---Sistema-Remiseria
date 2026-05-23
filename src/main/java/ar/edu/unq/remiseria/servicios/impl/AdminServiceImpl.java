package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.NoHayChoferesException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.servicios.interfaces.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final ViajeDAO viajeDAO;

    public AdminServiceImpl(ViajeDAO viajeDAO) {
        this.viajeDAO = viajeDAO;
    }

    @Override
    public Chofer conMasViajes() {
        return viajeDAO.recuperarChoferConMasViajes().orElseThrow(NoHayChoferesException::new);
    }
}
