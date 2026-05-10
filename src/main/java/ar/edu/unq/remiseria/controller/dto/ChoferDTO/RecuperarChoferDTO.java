package ar.edu.unq.remiseria.controller.dto.ChoferDTO;

import ar.edu.unq.remiseria.modelo.Chofer;

public record RecuperarChoferDTO(Long id, String nombre, String patente){

    public static RecuperarChoferDTO desdeModelo(Chofer chofer){
        return new RecuperarChoferDTO(chofer.getId(), chofer.getNombre(), chofer.getPatente());
    }
}    