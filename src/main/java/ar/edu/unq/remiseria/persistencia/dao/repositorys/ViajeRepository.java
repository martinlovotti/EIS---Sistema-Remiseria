package ar.edu.unq.remiseria.persistencia.dao.repositorys;

import ar.edu.unq.remiseria.modelo.Viaje;

public interface ViajeRepository {
    Viaje crear(Viaje viaje);
    void editar(Viaje viaje, Long viajeId);
    Viaje recuperar(Long viajeId);
}
