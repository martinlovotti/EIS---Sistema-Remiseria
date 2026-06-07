package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Viaje;

import java.util.List;


public interface ViajeService {
    Viaje editarViaje(Long viajeId, Viaje viaje);

    Viaje crear(Viaje viaje);

    void cancelarViaje(Long viajeId);

    Viaje recuperar(Long viajeId);

    void aceptarViaje(Long viajeId, Long choferId);

    void iniciarViaje(Long viajeId);

    void finalizarViaje(Long viajeId); // Setea en null el viaje del lado del cliente y del chofer

    void calificarViaje(Long viajeId, Long usuarioId, Double calificacion);

    List<Viaje> viajesSolicitados();
}
