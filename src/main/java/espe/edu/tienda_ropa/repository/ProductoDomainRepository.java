package espe.edu.tienda_ropa.repository;

import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDomainRepository extends JpaRepository<Producto, Long> {
    
    boolean existsByNombre(String nombre);
    
    // Buscar por categoria (usando el objeto Categoria)
    List<Producto> findByCategoria(Categoria categoria);
    
    // Buscar por ID de categoria
    List<Producto> findByCategoriaId(Long categoriaId);
    
    // Buscar por nombre de categoria
    List<Producto> findByCategoriaNombre(String categoriaNombre);
    
    // Buscar productos con stock mayor a cero
    List<Producto> findByStockGreaterThan(Integer stock);
    
    // Buscar productos por talla
    List<Producto> findByTalla(String talla);
    
    // Buscar productos por color
    List<Producto> findByColor(String color);
    
    // Buscar productos por rango de precio
    List<Producto> findByPrecioBetween(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax);
}
