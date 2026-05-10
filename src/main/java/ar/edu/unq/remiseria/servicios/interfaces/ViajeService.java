package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Viaje;import org.springframework.stereotype.Service;

@Service
public interface ViajeService {
    public void editarViaje(Viaje viaje, Long viajeId);

    public Viaje crearViaje(Viaje viaje);

    Viaje recuperar(Long viajeId);
}
