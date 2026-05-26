package ar.edu.unq.remiseria.testService;

import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TestServiceImpl implements TestService {
    private final ChoferDAO choferDAO;
    private final ViajeDAO viajeDAO;
    private final UsuarioDAO usuarioDAO;

    public TestServiceImpl(ChoferDAO choferDAO, ViajeDAO viajeDAO, UsuarioDAO usuarioDAO) {
        this.choferDAO = choferDAO;
        this.viajeDAO = viajeDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public void cleanUp() {
        usuarioDAO.desvincularViajes();
        choferDAO.desvincularViajes();
        viajeDAO.deleteAll();
        usuarioDAO.deleteAll();
        choferDAO.deleteAll();
    }
}
