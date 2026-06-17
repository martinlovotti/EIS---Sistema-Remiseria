package ar.edu.unq.remiseria.persistencia.entity;

import ar.edu.unq.remiseria.security.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Long entidadId;

    public AuthUser(String username, String password, Role role, Long entidadId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.entidadId = entidadId;
    }
}