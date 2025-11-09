package espe.edu.tienda_ropa.repository;

import espe.edu.tienda_ropa.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaDomainRepository extends JpaRepository<Categoria, Long> {

    // Buscar una categoría por nombre
    Optional<Categoria> findByNombre(String nombre);

    // Responder si existe la categoría con ese nombre
    boolean existsByNombre(String nombre);

    // Buscar categorías activas
    List<Categoria> findByActivaTrue();

    // Buscar categorías inactivas
    List<Categoria> findByActivaFalse();
}
