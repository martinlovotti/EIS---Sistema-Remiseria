package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.exception.DestinoInvalidoException;
import ar.edu.unq.remiseria.exception.OrigenInvalidoException;
import ar.edu.unq.remiseria.exception.UsuarioConViajeSolicitadoException;
import ar.edu.unq.remiseria.exception.ViajeNoPuedeSerAceptadoException;
import ar.edu.unq.remiseria.exception.ViajeYaIniciadoException;
import ar.edu.unq.remiseria.modelo.Chofer;
import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.modelo.Usuario;
import ar.edu.unq.remiseria.modelo.Viaje;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import ar.edu.unq.remiseria.persistencia.mapper.ChoferMapper;
import ar.edu.unq.remiseria.persistencia.mapper.UsuarioMapper;
import ar.edu.unq.remiseria.persistencia.mapper.ViajeMapper;
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
    private final ViajeMapper viajeMapper;
    private final UsuarioMapper usuarioMapper;
    private final ChoferMapper choferMapper;

    public ViajeServiceImpl(ViajeDAO viajeDAO, UsuarioDAO usuarioDAO, ChoferDAO choferDAO, ViajeMapper viajeMapper, UsuarioMapper usuarioMapper, ChoferMapper choferMapper) {
        this.viajeDAO = viajeDAO;
        this.usuarioDAO = usuarioDAO;
        this.choferDAO = choferDAO;
        this.viajeMapper = viajeMapper;
        this.usuarioMapper = usuarioMapper;
        this.choferMapper = choferMapper;
    }

    @Override
    public Viaje crear(Viaje viaje) {
        UsuarioSQL usuarioSQL = usuarioDAO.recuperar(viaje.getCliente().getId());
        Usuario usuarioModelo = usuarioMapper.toModel(usuarioSQL);

        if (usuarioModelo.tieneViajeSolicitado()) {
            throw new UsuarioConViajeSolicitadoException();
        }

        //guardar el viaje (sin la referencia circular del usuario)
        ViajeSQL viajeSQL = viajeMapper.fromModel(viaje);
        ViajeSQL viajeGuardado = viajeDAO.save(viajeSQL);

        //después asociar el viaje al usuario y guardarlo
        Viaje viajeConId = viajeMapper.toModel(viajeGuardado);
        usuarioModelo.solicitarViaje(viajeConId);
        usuarioDAO.save(usuarioMapper.fromModel(usuarioModelo));

        return viajeConId;
    }

    @Override
    public void cancelarViaje(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        Viaje viaje = viajeMapper.toModel(viajeSQL);

        viaje.cancelar();

        UsuarioSQL usuarioSQL = usuarioDAO.save(usuarioMapper.fromModel(viaje.getCliente()));
        viajeSQL = viajeMapper.fromModel(viaje);
        viajeSQL.setCliente(usuarioSQL);
        viajeDAO.save(viajeSQL);
    }

    @Override
    public Viaje recuperar(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        return viajeMapper.toModel(viajeSQL);
    }

    @Override
    public Viaje editarViaje(Long viajeId, Viaje viaje) {
        ViajeSQL viajeExistente = viajeDAO.recuperar(viajeId);

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

        return viajeMapper.toModel(viajeDAO.save(viajeExistente));
    }

    @Override
    public void aceptarViaje(Long viajeId, Long choferId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        Viaje viaje = viajeMapper.toModel(viajeSQL);

        ChoferSQL choferSQL = choferDAO.recuperar(choferId);
        Chofer chofer = choferMapper.toModel(choferSQL);

        chofer.aceptarViaje(viaje);

        viajeDAO.save(viajeMapper.fromModel(viaje));

        choferDAO.save(choferMapper.fromModel(chofer));

    }

    @Override
    public void iniciarViaje(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        Viaje viaje = viajeMapper.toModel(viajeSQL);
        viaje.inicializarViaje();
        viajeSQL = viajeMapper.fromModel(viaje);
        viajeSQL.setCliente(usuarioDAO.recuperar(viajeSQL.getCliente().getId()));
        viajeDAO.save(viajeSQL);
    }

    @Override
    public void finalizarViaje(Long viajeId) {
        ViajeSQL viajeSQL = viajeDAO.recuperar(viajeId);
        Viaje viaje = viajeMapper.toModel(viajeSQL);

        viaje.finalizarViaje();
        //Si lo puede finalizar el viajeActual de chofer y user queda null y
        // continua con las lineas de debajo donde guarda las entidades actualizadas por el metodo

        ChoferSQL choferSQL = choferMapper.fromModel(viaje.getChofer());
        UsuarioSQL usuarioSQL = usuarioMapper.fromModel(viaje.getCliente());
        viajeSQL = viajeMapper.fromModel(viaje);
        viajeSQL.setChofer(choferDAO.save(choferSQL));
        viajeSQL.setCliente(usuarioDAO.save(usuarioSQL));
        //Se podrian no usar estas 2 variables y pasarla directamente al dao con el get pero para
        //que quede mas legible lo que guarda cada dao

        viajeDAO.save(viajeSQL);

    }
}
