package ar.edu.unq.remiseria.persistencia.mapper;

import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ChoferMapper.class})
public interface ViajeMapper {

    @Mapping(target = "cliente.viajeActual", ignore = true)
    @Mapping(target = "chofer.viajeActual", ignore = true)
    Viaje toModel(ViajeSQL viajeSQL);

    @Mapping(target = "cliente.viajeActual", ignore = true)
    @Mapping(target = "chofer.viajeActual", ignore = true)
    ViajeSQL fromModel(Viaje viaje);
}