package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Viaje;


public interface ViajeService {
    Viaje editarViaje(Viaje viaje);

    Viaje crear(Viaje viaje);

    void cancelarViaje(Long viajeId);

    Viaje recuperar(Long viajeId);

    void aceptarViaje(Long viajeId, Long choferId);

    void iniciarViaje(Long viajeId);

    void finalizarViaje(Long viajeId); // Setea en null el viaje del lado del cliente y del chofer

}
