package ar.edu.unq.remiseria.modelo;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private List<Viaje> viajes = new ArrayList<>();

    public Usuario(String nombre){
        this.nombre = nombre;
        //this.viajes = new ArrayList<>();
    }

    public boolean tieneViajeSolicitado(){
        return viajes.stream().anyMatch(viaje -> viaje.estaSolicitado());
    }

    public void agregarViaje(Viaje viaje) {
        this.viajes.add(viaje);
        viaje.setCliente(this);
    }
}
