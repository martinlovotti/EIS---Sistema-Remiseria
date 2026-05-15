package ar.edu.unq.remiseria.persistencia.mapper;

import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChoferMapper {

    @Mapping(target = "viajeActual.chofer", ignore = true)
    Chofer toModel(ChoferSQL choferSQL);

    @Mapping(target = "viajeActual.chofer", ignore = true)
    ChoferSQL fromModel(Chofer chofer);
}
