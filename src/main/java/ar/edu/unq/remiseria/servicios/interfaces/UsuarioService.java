package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;

import java.util.List;

public interface UsuarioService {
     Usuario crear(Usuario e);
     Usuario recuperar(Long id);
     void eliminar(Long id);
     List<Usuario> recuperarTodos();
    List<Viaje> recuperarViajesPorEstado(Long usuarioId, EstadoViaje estado);
}
