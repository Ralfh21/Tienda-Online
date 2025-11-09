package espe.edu.tienda_ropa.repository;

import espe.edu.tienda_ropa.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteDomainRepository extends JpaRepository<Cliente, Long> {

    // Buscar un cliente por email
    Optional<Cliente> findByEmail(String email);

    // Responder si existe el cliente con ese email
    boolean existsByEmail(String email);

    // Buscar clientes por nombre
    Optional<Cliente> findByNombre(String nombre);

    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
}
