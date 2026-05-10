package ar.edu.unq.remiseria.persistencia.dao.entity;

import ar.edu.unq.remiseria.modelo.Chofer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ChoferSQL {
    @Id
    @GeneratedValue
    private Long id;

    public static ChoferSQL creadDesde(Chofer chofer) {
        ChoferSQL choferSQL = new ChoferSQL();
        choferSQL.setId(chofer.getId());
        return choferSQL;
    }
}
