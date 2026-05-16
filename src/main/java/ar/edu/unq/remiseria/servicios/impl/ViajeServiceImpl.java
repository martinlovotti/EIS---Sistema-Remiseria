package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import ar.edu.unq.remiseria.persistencia.mapper.UsuarioMapper;
import ar.edu.unq.remiseria.persistencia.mapper.ViajeMapper;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {

    private final ViajeDAO viajeDAO;
    private final UsuarioDAO usuarioDAO;
    private final ViajeMapper viajeMapper;
    private final UsuarioMapper usuarioMapper;

    public ViajeServiceImpl(ViajeDAO viajeDAO, UsuarioDAO usuarioDAO, ViajeMapper viajeMapper, UsuarioMapper usuarioMapper) {
        this.viajeDAO = viajeDAO;
        this.usuarioDAO = usuarioDAO;
        this.viajeMapper = viajeMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Viaje crear(Viaje viaje) {
        UsuarioSQL usuarioSQL = usuarioDAO.recuperar(viaje.getCliente().getId());
        Usuario usuarioModelo = usuarioMapper.toModel(usuarioSQL);

        if (usuarioModelo.tieneViajeSolicitado()) {
            throw new UsuarioConViajeSolicitadoException("El cliente ya tiene un viaje solicitado");
        }

        usuarioModelo.solicitarViaje(viaje);

        ViajeSQL viajeSQL = viajeMapper.fromModel(viaje);

        ViajeSQL viajeGuardado = viajeDAO.save(viajeSQL);

        usuarioDAO.save(usuarioMapper.fromModel(usuarioModelo));

        return viajeMapper.toModel(viajeGuardado);
    }

    @Override
    public void cancelarViaje(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        Viaje viaje = viajeMapper.toModel(viajeSQL);

        viaje.cancelar();

        viajeDAO.save(viajeMapper.fromModel(viaje));
    }

    @Override
    public Viaje recuperar(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        return viajeMapper.toModel(viajeSQL);
    }

    @Override
    public void editarViaje(Viaje viaje) {
        ViajeSQL viajeExistente = viajeDAO.recuperar(viaje.getId());

        // aca habria que agregar una logica para chequear que solo se esten modificando campos permitidos

        viajeDAO.save(viajeMapper.fromModel(viaje));
    }

    @Override
    public void aceptarViaje(Long viajeId, Long choferId) {

    }

    @Override
    public void iniciarViaje(Long viajeId) {

    }

    @Override
    public void finalizarViaje(Long viajeId) {

    }
}
