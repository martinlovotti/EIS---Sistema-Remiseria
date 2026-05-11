package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {

    private ViajeRepository viajeRepository;

    public ViajeServiceImpl(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @Override
    public void editarViaje(Viaje viaje, Long viajeId) {
        viajeRepository.editar(viaje, viajeId);
    }

    @Override
    public Viaje crear(Viaje viaje) {

        return viajeRepository.crear(viaje);
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
}
