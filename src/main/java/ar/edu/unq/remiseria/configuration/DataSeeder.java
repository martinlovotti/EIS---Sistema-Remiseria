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

import java.time.LocalDateTime;
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
        String[] calles = {
                "Av. Mitre", "Av. 12 de Octubre", "Calle 9 de Julio", "Av. Calchaquí",
                "Av. Los Quilmes", "Av. Hipólito Yrigoyen", "Calle Belgrano", "Calle Rivadavia",
                "Av. San Martín", "Calle Sarmiento", "Av. Centenario", "Calle Alsina",
                "Av. Lamadrid", "Calle Moreno", "Calle Lavalle"
        };

        String[] ciudades = {
                "Quilmes", "Bernal", "Don Bosco", "Ezpeleta", "Berazategui",
                "Hudson", "Solano", "Avellaneda", "La Plata", "Villa Domínico"
        };

        List<Long> choferesConViajeActivo = new ArrayList<>();
        List<Long> usuariosConViajeActivo = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            UsuarioSQL cliente = usuarios.get(random.nextInt(usuarios.size()));

            String origen = generarDireccion(random, calles, ciudades);
            String destino = generarDireccion(random, calles, ciudades);

            while (destino.equals(origen)) {
                destino = generarDireccion(random, calles, ciudades);
            }

            EstadoViaje estado = generarEstado(random);

            // Si el usuario ya tiene un viaje activo, evitamos crear otro activo
            if (esEstadoActivo(estado) && usuariosConViajeActivo.contains(cliente.getId())) {
                estado = EstadoViaje.FINALIZADO;
            }

            ViajeSQL viaje = new ViajeSQL();
            viaje.setOrigen(origen);
            viaje.setDestino(destino);
            viaje.setKilometros(redondear(2.0 + (20.0 - 2.0) * random.nextDouble()));
            viaje.setPrecioFinal(redondear(1500.0 + (8000.0 - 1500.0) * random.nextDouble()));
            viaje.setEstadoViaje(estado);
            viaje.setCliente(cliente);
            viaje.setFechaCreacion(generarFechaSegunEstado(random, estado));

            ChoferSQL choferAsignado = null;

            if (estado == EstadoViaje.PENDIENTE) {
                viaje.setChofer(null);
            } else if (estado == EstadoViaje.CANCELADO && random.nextBoolean()) {
                // A veces el viaje cancelado nunca llegó a tener chofer asignado
                viaje.setChofer(null);
            } else {
                EstadoViaje finalEstado = estado;
                List<ChoferSQL> choferesDisponibles = choferes.stream()
                        .filter(c -> !(esEstadoActivo(finalEstado) && choferesConViajeActivo.contains(c.getId())))
                        .toList();

                if (choferesDisponibles.isEmpty()) {
                    // Si no hay chofer disponible para un estado activo, degradamos el viaje a finalizado
                    estado = EstadoViaje.FINALIZADO;
                    viaje.setEstadoViaje(estado);
                    choferAsignado = choferes.get(random.nextInt(choferes.size()));
                } else {
                    choferAsignado = choferesDisponibles.get(random.nextInt(choferesDisponibles.size()));
                }

                viaje.setChofer(choferAsignado);
            }

            // Calificación solo para finalizados y como entero entre 1 y 5
            if (estado == EstadoViaje.FINALIZADO) {
                viaje.setCalificacion((double) (1 + random.nextInt(5)));
            }

            ViajeSQL viajeGuardado = viajeDAO.save(viaje);

            // Registramos activos para no duplicar
            if (esEstadoActivo(estado)) {
                if (!usuariosConViajeActivo.contains(cliente.getId())) {
                    usuariosConViajeActivo.add(cliente.getId());
                }

                if (choferAsignado != null && !choferesConViajeActivo.contains(choferAsignado.getId())) {
                    choferesConViajeActivo.add(choferAsignado.getId());
                }
            }

            // viajeActual solo para EN_CURSO
            if (estado == EstadoViaje.EN_CURSO) {
                if (cliente.getViajeActual() == null) {
                    cliente.setViajeActual(viajeGuardado);
                    usuarioDAO.save(cliente);
                }

                if (choferAsignado != null && choferAsignado.getViajeActual() == null) {
                    choferAsignado.setViajeActual(viajeGuardado);
                    choferDAO.save(choferAsignado);
                }
            }
        }
    }

    private boolean esEstadoActivo(EstadoViaje estado) {
        return estado == EstadoViaje.ACEPTADO || estado == EstadoViaje.EN_CURSO;
    }

    private String generarDireccion(Random random, String[] calles, String[] ciudades) {
        String calle = calles[random.nextInt(calles.length)];
        int numero = 100 + random.nextInt(4900);
        String ciudad = ciudades[random.nextInt(ciudades.length)];

        return calle + " " + numero + ", " + ciudad;
    }

    private Double redondear(double valor) {
        return Math.round(valor * 10.0) / 10.0;
    }

    private LocalDateTime generarFechaSegunEstado(Random random, EstadoViaje estado) {
        return switch (estado) {
            case PENDIENTE, ACEPTADO, EN_CURSO ->
                    LocalDateTime.now()
                            .minusMinutes(random.nextInt(15));

            case CANCELADO, FINALIZADO ->
                    LocalDateTime.now()
                            .minusDays(random.nextInt(30))
                            .minusHours(random.nextInt(24))
                            .minusMinutes(random.nextInt(60));
        };
    }

    private EstadoViaje generarEstado(Random random) {
        int valor = random.nextInt(100);

        if (valor < 20) {
            return EstadoViaje.PENDIENTE;
        } else if (valor < 40) {
            return EstadoViaje.ACEPTADO;
        } else if (valor < 50) {
            return EstadoViaje.EN_CURSO;
        } else if (valor < 70) {
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

