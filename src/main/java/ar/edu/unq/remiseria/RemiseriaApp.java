package ar.edu.unq.remiseria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RemiseriaApp {

    public static void main(String[] args) {
        SpringApplication.run(RemiseriaApp.class, args);
    }
}