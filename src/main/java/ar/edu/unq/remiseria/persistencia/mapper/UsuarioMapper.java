package ar.edu.unq.remiseria.persistencia.mapper;

import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "viajeActual.cliente", ignore = true)
    Usuario toModel(UsuarioSQL usuarioSQL);

    @Mapping(target = "viajeActual.cliente", ignore = true)
    UsuarioSQL fromModel(Usuario usuario);
}