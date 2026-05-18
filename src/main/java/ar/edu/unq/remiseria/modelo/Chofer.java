package ar.edu.unq.remiseria.modelo;

import lombok.*;


@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Chofer {
    private Long id;
    private String nombre;
    private String patente;
    private Viaje viajeActual;

    public Chofer(String nombre, String patente){
        this.nombre = nombre;
        this.patente = patente;
        this.viajeActual = null;
    }
}
