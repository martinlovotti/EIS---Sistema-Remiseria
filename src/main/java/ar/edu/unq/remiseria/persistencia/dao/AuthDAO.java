package ar.edu.unq.remiseria.persistencia.dao;

import ar.edu.unq.remiseria.persistencia.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthDAO extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByUsername(String username);
}

