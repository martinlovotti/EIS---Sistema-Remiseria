package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.DestinoInvalidoException;
import ar.edu.unq.remiseria.exception.OrigenInvalidoException;
import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.exception.ViajeYaIniciadoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.servicios.interfaces.ViajeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static ar.edu.unq.remiseria.modelo.EstadoViaje.ACEPTADO;
import static java.util.Objects.isNull;

@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {

    private final ViajeDAO viajeDAO;
    private final UsuarioDAO usuarioDAO;
    private final ChoferDAO choferDAO;

    public ViajeServiceImpl(ViajeDAO viajeDAO, UsuarioDAO usuarioDAO, ChoferDAO choferDAO) {
        this.viajeDAO = viajeDAO;
        this.usuarioDAO = usuarioDAO;
        this.choferDAO = choferDAO;
    }

    @Override
    public Viaje crear(Viaje viaje) {
        Usuario usuario = usuarioDAO.recuperar(viaje.getCliente().getId());

        if (usuario.tieneViajeSolicitado()) {
            throw new UsuarioConViajeSolicitadoException();
        }

        usuario.solicitarViaje(viaje);
        viajeDAO.save(viaje);
        usuarioDAO.save(usuario);

        return viaje;
    }

    @Override
    public void cancelarViaje(Long viajeId) {
        Viaje viaje = viajeDAO.recuperar(viajeId);
        viaje.cancelar();
        viajeDAO.save(viaje);
    }

    @Override
    public Viaje recuperar(Long viajeId) {
        Viaje viaje = viajeDAO.recuperar(viajeId);
        return viaje;
    }

    @Override
    public Viaje editarViaje(Long viajeId, Viaje viaje) {
        Viaje viajeExistente = viajeDAO.recuperar(viajeId);

        if (viajeExistente.getEstadoViaje() == ACEPTADO) {
            throw new ViajeYaIniciadoException();
        }

        if (isNull(viaje.getDestino()) || viaje.getDestino().isBlank()) {
            throw new DestinoInvalidoException();
        }

        if (isNull(viaje.getOrigen()) || viaje.getOrigen().isBlank()) {
            throw new OrigenInvalidoException();
        }

        // Solo se modifican las direcciones de origen y destino, los kilómetros y el precio del viaje se calcularían en
        // base a estos datos que, en un futuro, se volverían mas complejos que un String (Spoiler: no va a pasar)

        viajeExistente.setOrigen(viaje.getOrigen());
        viajeExistente.setDestino(viaje.getDestino());

        return viajeDAO.save(viajeExistente);
    }

    @Override
    public void aceptarViaje(Long viajeId, Long choferId) {
        Viaje viaje = viajeDAO.recuperar(viajeId);
        Chofer chofer = choferDAO.recuperar(choferId);

        chofer.aceptarViaje(viaje);

        viajeDAO.save(viaje);
        choferDAO.save(chofer);
    }

    @Override
    public void iniciarViaje(Long viajeId) {
        Viaje viaje = viajeDAO.recuperar(viajeId);
        viaje.inicializarViaje();
        viajeDAO.save(viaje);
    }

    @Override
    public void finalizarViaje(Long viajeId) {
        Viaje viaje = viajeDAO.recuperar(viajeId);
        viaje.finalizarViaje();
        viajeDAO.save(viaje);
    }
}
