package ar.edu.unq.remiseria.configuration;

import ar.edu.unq.remiseria.modelo.EstadoViaje;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.persistencia.dao.ChoferDAO;
import ar.edu.unq.remiseria.persistencia.dao.UsuarioDAO;
import ar.edu.unq.remiseria.persistencia.dao.ViajeDAO;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.persistencia.entity.ChoferSQL;
import ar.edu.unq.remiseria.persistencia.entity.UsuarioSQL;
import ar.edu.unq.remiseria.persistencia.entity.ViajeSQL;
import ar.edu.unq.remiseria.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioDAO usuarioDAO;
    private final ChoferDAO choferDAO;
    private final ViajeDAO viajeDAO;
    private final AuthDAO authDAO;
    private final BCryptPasswordEncoder encoder;

    public DataSeeder(
            UsuarioDAO usuarioDAO,
            ChoferDAO choferDAO,
            ViajeDAO viajeDAO,
            AuthDAO authDAO,
            BCryptPasswordEncoder encoder
    ) {
        this.usuarioDAO = usuarioDAO;
        this.choferDAO = choferDAO;
        this.viajeDAO = viajeDAO;
        this.authDAO = authDAO;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioDAO.count() > 0 || choferDAO.count() > 0 || viajeDAO.count() > 0) {
            System.out.println("Seeder omitido: ya existen datos cargados.");
            return;
        }

        Random random = new Random();

        List<UsuarioSQL> usuarios = crearUsuarios(20);
        List<ChoferSQL> choferes = crearChoferes(10);

        crearAuthParaUsuarios(usuarios);
        crearAuthParaChoferes(choferes);

        crearViajes(100, usuarios, choferes, random);

        System.out.println("Seeder ejecutado correctamente: 20 usuarios, 10 choferes, 100 viajes.");
    }

    private List<UsuarioSQL> crearUsuarios(int cantidad) {
        List<UsuarioSQL> usuarios = new ArrayList<>();

        String[] nombres = {
                "Juan", "Ana", "Marcos", "Lucía", "Pedro",
                "Sofía", "Martín", "Carla", "Diego", "Valentina",
                "Tomás", "Camila", "Nicolás", "Florencia", "Julián",
                "Agustina", "Mateo", "Micaela", "Lucas", "Martina"
        };

        String[] apellidos = {
                "Pérez", "Gómez", "Fernández", "López", "Díaz",
                "Romero", "Martínez", "Suárez", "Torres", "Alvarez",
                "Benítez", "Ruiz", "Herrera", "Silva", "Castro",
                "Rojas", "Morales", "Acosta", "Medina", "Sosa"
        };

        for (int i = 0; i < cantidad; i++) {
            UsuarioSQL usuario = new UsuarioSQL();
            usuario.setNombre(nombres[i % nombres.length] + " " + apellidos[i % apellidos.length]);
            usuarios.add(usuarioDAO.save(usuario));
        }

        return usuarios;
    }

    private List<ChoferSQL> crearChoferes(int cantidad) {
        List<ChoferSQL> choferes = new ArrayList<>();

        String[] nombres = {
                "Carlos", "Miguel", "Paula", "Javier", "Laura",
                "Andrés", "Natalia", "Roberto", "Patricia", "Sergio"
        };

        String[] apellidos = {
                "Gómez", "Benítez", "Fernández", "Ruiz", "Silva",
                "Morales", "Suárez", "Acosta", "Castro", "Medina"
        };

        for (int i = 0; i < cantidad; i++) {
            ChoferSQL chofer = new ChoferSQL();
            chofer.setNombre(nombres[i % nombres.length] + " " + apellidos[i % apellidos.length]);
            chofer.setPatente(generarPatente(i));
            choferes.add(choferDAO.save(chofer));
        }

        return choferes;
    }

    private void crearAuthParaUsuarios(List<UsuarioSQL> usuarios) {
        for (int i = 0; i < usuarios.size(); i++) {
            String username = "usuario" + (i + 1);
            crearAuthSiNoExiste(username, "1234", Role.USUARIO, usuarios.get(i).getId());
        }
    }

    private void crearAuthParaChoferes(List<ChoferSQL> choferes) {
        for (int i = 0; i < choferes.size(); i++) {
            String username = "chofer" + (i + 1);
            crearAuthSiNoExiste(username, "1234", Role.CHOFER, choferes.get(i).getId());
        }
    }

    private void crearAuthSiNoExiste(String username, String rawPassword, Role role, Long entidadId) {
        if (authDAO.findByUsername(username).isPresent()) {
            return;
        }

        AuthUser authUser = new AuthUser();
        authUser.setUsername(username);
        authUser.setPassword(encoder.encode(rawPassword));
        authUser.setRole(role);
        authUser.setEntidadId(entidadId);

        authDAO.save(authUser);
    }

    private void crearViajes(
            int cantidad,
            List<UsuarioSQL> usuarios,
            List<ChoferSQL> choferes,
            Random random
    ) {
        String[] origenes = {
                "Quilmes Centro", "Bernal", "Don Bosco", "Ezpeleta", "Villa Argentina",
                "Quilmes Oeste", "Solano", "Hudson", "Berazategui Centro", "Plaza Conesa"
        };

        String[] destinos = {
                "Bernal", "Quilmes Centro", "Ezpeleta", "Don Bosco", "Quilmes Oeste",
                "Berazategui", "Hudson", "Solano", "Avellaneda", "La Plata"
        };

        List<UsuarioSQL> usuariosConViajeActual = new ArrayList<>();
        List<ChoferSQL> choferesConViajeActual = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            UsuarioSQL cliente = usuarios.get(random.nextInt(usuarios.size()));

            String origen = origenes[random.nextInt(origenes.length)];
            String destino = destinos[random.nextInt(destinos.length)];

            while (destino.equals(origen)) {
                destino = destinos[random.nextInt(destinos.length)];
            }

            EstadoViaje estado = generarEstado(random);

            ViajeSQL viaje = new ViajeSQL();
            viaje.setOrigen(origen);
            viaje.setDestino(destino);
            viaje.setKilometros(2.0 + (20.0 - 2.0) * random.nextDouble());
            viaje.setPrecioFinal(1500.0 + (8000.0 - 1500.0) * random.nextDouble());
            viaje.setEstadoViaje(estado);
            viaje.setCliente(cliente);

            // Chofer según estado
            if (estado == EstadoViaje.PENDIENTE) {
                viaje.setChofer(null);
            } else {
                ChoferSQL chofer = choferes.get(random.nextInt(choferes.size()));
                viaje.setChofer(chofer);
            }

            // Calificación solo para finalizados
            if (estado == EstadoViaje.FINALIZADO) {
                viaje.setCalificacion(1.0 + (5.0 - 1.0) * random.nextDouble());
            }

            ViajeSQL viajeGuardado = viajeDAO.save(viaje);

            // viajeActual solo para EN_CURSO
            if (estado == EstadoViaje.EN_CURSO) {
                if (cliente.getViajeActual() == null && !usuariosConViajeActual.contains(cliente)) {
                    cliente.setViajeActual(viajeGuardado);
                    usuarioDAO.save(cliente);
                    usuariosConViajeActual.add(cliente);
                }

                if (viajeGuardado.getChofer() != null) {
                    ChoferSQL chofer = viajeGuardado.getChofer();

                    if (chofer.getViajeActual() == null && !choferesConViajeActual.contains(chofer)) {
                        chofer.setViajeActual(viajeGuardado);
                        choferDAO.save(chofer);
                        choferesConViajeActual.add(chofer);
                    }
                }
            }
        }
    }

    private EstadoViaje generarEstado(Random random) {
        int valor = random.nextInt(100);

        if (valor < 20) {
            return EstadoViaje.PENDIENTE;
        } else if (valor < 40) {
            return EstadoViaje.ACEPTADO;
        } else if (valor < 55) {
            return EstadoViaje.EN_CURSO;
        } else if (valor < 75) {
            return EstadoViaje.CANCELADO;
        } else {
            return EstadoViaje.FINALIZADO;
        }
    }

    private String generarPatente(int index) {
        char a = (char) ('A' + (index % 26));
        char b = (char) ('B' + (index % 26));
        int numero = 100 + index;
        char c = (char) ('C' + (index % 26));
        char d = (char) ('D' + (index % 26));

        return "" + a + b + numero + c + d;
    }
}

