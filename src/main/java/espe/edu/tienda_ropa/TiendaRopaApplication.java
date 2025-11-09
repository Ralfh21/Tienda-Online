package espe.edu.tienda_ropa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"espe.edu.tienda_ropa.domain"})
@EnableJpaRepositories(basePackages = {"espe.edu.tienda_ropa.repository"})
public class TiendaRopaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiendaRopaApplication.class, args);
    }

}
