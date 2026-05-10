package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {
    @Autowired
    private ViajeRepository viajeRepository;

    @Override
    public void editarViaje(Viaje viaje, Long viajeId) {
        viajeRepository.editar(viaje, viajeId);
    }

    @Override
    public Viaje crear(Viaje viaje) {

        return viajeRepository.crear(viaje);
    }

    @Override
    public Viaje recuperar(Long viajeId) {
//        TODO
        return null;
    }
}
