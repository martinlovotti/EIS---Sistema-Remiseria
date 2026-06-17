package ar.edu.unq.remiseria.controller.dto.ChoferDTO;

import ar.edu.unq.remiseria.modelo.Chofer;

    public record CrearChoferDTO(String nombre, String patente){

        public Chofer aModelo(){
            Chofer chofer = new Chofer(
            this.nombre, this.patente);
            return chofer;
        }
    }
