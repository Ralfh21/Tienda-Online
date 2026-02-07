package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Categoria;
import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.dto.ProductoRequestData;
import espe.edu.tienda_ropa.dto.ProductoResponse;
import espe.edu.tienda_ropa.repository.CategoriaDomainRepository;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import espe.edu.tienda_ropa.service.impl.ProductoServiceImpl;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductoServiceImpl.
 * Verifica la lógica de negocio para la gestión de productos:
 * creación con validación de nombre único y categoría existente.
 */
class ProductoServiceImplTest {

    private ProductoDomainRepository repo;
    private CategoriaDomainRepository categoriaRepo;
    private ProductoServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoDomainRepository.class);
        categoriaRepo = mock(CategoriaDomainRepository.class);
        service = new ProductoServiceImpl(repo, categoriaRepo);
    }

    /**
     * Test: Crear producto exitosamente.
     * Verifica que al crear un producto con nombre único y categoría válida:
     * - Se guarda correctamente en el repositorio
     * - Se retorna ProductoResponse con todos los datos incluido nombre de
     * categoría
     * - Se valida la existencia de la categoría antes de guardar
     */
    @Test
    void testCreateProducto_Success() {
        // Mock request
        ProductoRequestData req = new ProductoRequestData();
        req.setNombre("Polo Azul");
        req.setDescripcion("Camisa ligera");
        req.setPrecio(new BigDecimal("20.5"));
        req.setCategoriaId(1L);
        req.setTalla("M");
        req.setColor("Azul");
        req.setStock(10);
        req.setImagenUrl("imagen.jpg");

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ropa");

        Producto savedProducto = new Producto();
        savedProducto.setId(1L);
        savedProducto.setNombre(req.getNombre());
        savedProducto.setDescripcion(req.getDescripcion());
        savedProducto.setPrecio(req.getPrecio());
        savedProducto.setCategoria(categoria);
        savedProducto.setTalla(req.getTalla());
        savedProducto.setColor(req.getColor());
        savedProducto.setStock(req.getStock());
        savedProducto.setImagenUrl(req.getImagenUrl());

        // Mocks
        when(repo.existsByNombre("Polo Azul")).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(repo.save(ArgumentMatchers.any(Producto.class))).thenReturn(savedProducto);

        // Ejecutar
        ProductoResponse response = service.create(req);

        // Verificar
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Polo Azul", response.getNombre());
        assertEquals("Ropa", response.getCategoriaNombre());

        verify(repo).existsByNombre("Polo Azul");
        verify(categoriaRepo).findById(1L);
        verify(repo).save(ArgumentMatchers.any(Producto.class));
    }

    /**
     * Test: Conflicto al crear producto con nombre duplicado.
     * Verifica que al intentar crear un producto con un nombre
     * que ya existe en el sistema, se lanza ConflictException.
     */
    @Test
    void testCreateProducto_Conflict() {
        ProductoRequestData req = new ProductoRequestData();
        req.setNombre("Polo Azul");

        when(repo.existsByNombre("Polo Azul")).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> service.create(req));
        assertEquals("El nombre del producto ya esta registrado", exception.getMessage());
    }

    /**
     * Test: Crear producto con categoría inexistente.
     * Verifica que al crear un producto referenciando una categoría
     * que no existe, se lanza NotFoundException.
     */
    @Test
    void testCreateProducto_CategoriaNotFound() {
        ProductoRequestData req = new ProductoRequestData();
        req.setNombre("Polo Azul");
        req.setCategoriaId(1L);

        when(repo.existsByNombre("Polo Azul")).thenReturn(false);
        when(categoriaRepo.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.create(req));
        assertEquals("Categoria no encontrada", exception.getMessage());
    }
}