package ar.edu.unq.remiseria.modelo;

import lombok.*;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private Viaje viajeActual;

    public Usuario(String nombre){
        this.nombre = nombre;
        this.viajeActual = null;
    }

    public boolean tieneViajeSolicitado(){
        return viajeActual != null;
    }

    public void solicitarViaje(Viaje viaje) {
        this.viajeActual = viaje;
        viaje.setCliente(this);
    }
}
