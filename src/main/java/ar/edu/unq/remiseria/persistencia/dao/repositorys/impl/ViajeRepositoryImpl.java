package ar.edu.unq.remiseria.persistencia.dao.repositorys.impl;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.dao.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.dao.entity.ViajeSQL;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class ViajeRepositoryImpl implements ViajeRepository {

    @Autowired
    private ViajeDAO viajeDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

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
    public void editar(Viaje viaje, Long viajeId) {
        ViajeSQL viajeSQL = ViajeSQL.from(viaje);
        viajeDAO.save(viajeSQL);
    }
}
