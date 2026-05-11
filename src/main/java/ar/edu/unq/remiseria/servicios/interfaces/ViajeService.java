package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Viaje;


public interface ViajeService {
    void editarViaje(Viaje viaje, Long viajeId);

    Viaje crear(Viaje viaje);

    void cancelarViaje(Long viajeId);

    Viaje recuperar(Long viajeId);

}
