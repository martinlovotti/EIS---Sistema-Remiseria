package ar.edu.unq.remiseria.modelo;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Chofer {
    private Long id;
    private String nombre;
    private String patente;
    private List<Viaje> viajes;

    public Chofer(String nombre, String patente){
        this.nombre = nombre;
        this.patente = patente;
        this.viajes = new ArrayList<>();
    }
}
