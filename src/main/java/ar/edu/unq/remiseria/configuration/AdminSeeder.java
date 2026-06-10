package ar.edu.unq.remiseria.configuration;
import ar.edu.unq.remiseria.persistencia.dao.AuthDAO;
import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import ar.edu.unq.remiseria.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AuthDAO authDAO;
    private final BCryptPasswordEncoder encoder;

    public AdminSeeder(AuthDAO authDAO, BCryptPasswordEncoder encoder) {
        this.authDAO = authDAO;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String adminUsername = "admin";

        if (authDAO.findByUsername(adminUsername).isEmpty()) {
            AuthUser admin = new AuthUser();
            admin.setUsername(adminUsername);
            admin.setPassword(encoder.encode("1234"));
            admin.setRole(Role.ADMIN);
            admin.setEntidadId(null);

            authDAO.save(admin);

            System.out.println("Admin inicial creado correctamente.");
        } else {
            System.out.println("El admin inicial ya existe.");
        }
    }
}

