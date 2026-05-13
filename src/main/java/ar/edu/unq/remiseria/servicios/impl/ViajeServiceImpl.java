package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.UsuarioRepository;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {

    private ViajeRepository viajeRepository;
    private UsuarioRepository usuarioRepository;

    public ViajeServiceImpl(ViajeRepository viajeRepository, UsuarioRepository usuarioRepository) {
        this.viajeRepository = viajeRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public void editarViaje(Viaje viaje, Long viajeId) {
        viajeRepository.editar(viaje, viajeId);
    }

    @Override
    public Viaje crear(Viaje viaje) {
        Usuario usuario = usuarioRepository.recuperar(viaje.getCliente().getId());

        if(usuario.tieneViajeSolicitado()) {
            throw new UsuarioConViajeSolicitadoException("El cliente ya tiene un viaje solicitado");
        }

        usuario.solicitarViaje(viaje);
        Viaje viajeCreado = viajeRepository.crear(viaje);
        usuarioRepository.actualizar(usuario);

        return viajeCreado;
    }

    @Override
    public void cancelarViaje(Long viajeId) {
        Viaje viaje = viajeRepository.recuperar(viajeId);

        viaje.cancelar();

        viajeRepository.editar(viaje, viajeId);
    }

    @Override
    public Viaje recuperar(Long viajeId) {
        return viajeRepository.recuperar(viajeId);
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
