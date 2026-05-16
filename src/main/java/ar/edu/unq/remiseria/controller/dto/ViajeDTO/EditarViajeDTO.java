package ar.edu.unq.remiseria.controller.dto.ViajeDTO;

import ar.edu.unq.remiseria.modelo.Viaje;

public record EditarViajeDTO(String origen, String destino) {
    public Viaje aModelo() {
        Viaje viaje = new Viaje();
        viaje.setOrigen(origen);
        viaje.setDestino(destino);

        return viaje;
    }
}
