package ar.edu.unq.remiseria.servicios.interfaces;

import ar.edu.unq.remiseria.modelo.Chofer;
import java.util.List;

public interface ChoferService {
    Chofer crear(Chofer c);
    Chofer recuperar(Long id);
    void eliminar(Long id);
    List<Chofer> recuperarTodos();
}