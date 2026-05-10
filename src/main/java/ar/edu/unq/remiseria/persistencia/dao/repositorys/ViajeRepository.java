package ar.edu.unq.remiseria.persistencia.dao.repositorys;

import ar.edu.unq.remiseria.modelo.Viaje;

public interface ViajeRepository {
    public void editar(Viaje viaje, Long viajeId);
}
