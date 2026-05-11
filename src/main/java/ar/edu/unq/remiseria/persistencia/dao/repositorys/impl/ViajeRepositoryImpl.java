package ar.edu.unq.remiseria.persistencia.dao.repositorys.impl;

import ar.edu.unq.remiseria.exception.ViajeNoEncontradoException;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.dao.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.dao.entity.ViajeSQL;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import org.springframework.stereotype.Component;

@Component
public class ViajeRepositoryImpl implements ViajeRepository {

    private ViajeDAO viajeDAO;
    private UsuarioDAO usuarioDAO;

    public ViajeRepositoryImpl(ViajeDAO viajeDAO, UsuarioDAO usuarioDAO){
        this.viajeDAO = viajeDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Viaje crear(Viaje viaje) {
        UsuarioSQL cliente = usuarioDAO.recuperar(viaje.getCliente().getId());

        ViajeSQL viajeSQL = ViajeSQL.from(viaje);

        viajeSQL.setCliente(cliente);

        viajeDAO.save(viajeSQL);

        viaje.setId(viajeSQL.getId());
        return viaje;
    }

    @Override
    public Viaje recuperar(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.findById(viajeId).orElseThrow(ViajeNoEncontradoException::new);
        return ViajeSQL.toModel(viajeSQL);
    }

    @Override
    public void editar(Viaje viaje, Long viajeId) {
        ViajeSQL viajeSQL = ViajeSQL.from(viaje);
        viajeSQL.setId(viajeId);
        viajeDAO.save(viajeSQL);
    }
}
