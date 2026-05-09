package ar.edu.unq.remiseria.modelo;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private List<Viaje> viajes;

    public Usuario(String nombre){
        this.nombre = nombre;
        this.viajes = new ArrayList<>();
    }
}
