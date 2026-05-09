package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Usuario;

import java.util.List;

public interface UsuarioService {
     Usuario crear(Usuario e);
     Usuario recuperar(Long id);
     void eliminar(Long id);
     List<Usuario> recuperarTodos();
}
