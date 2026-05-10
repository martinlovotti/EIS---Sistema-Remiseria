package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.repositorys.ViajeRepository;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ViajeServiceImpl implements ViajeService {
    @Autowired
    private ViajeRepository viajeRepository;

    @Override
    public void editarViaje(Viaje viaje, Long viajeId) {
        viajeRepository.editar(viaje, viajeId);
    }

    @Override
    public Viaje crearViaje(Viaje viaje) {
//        TODO
        return null;
    }

    @Override
    public Viaje recuperar(Long viajeId) {
//        TODO
        return null;
    }
}
