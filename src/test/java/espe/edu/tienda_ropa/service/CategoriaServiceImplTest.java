package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Categoria;
import espe.edu.tienda_ropa.dto.CategoriaRequestData;
import espe.edu.tienda_ropa.dto.CategoriaResponse;
import espe.edu.tienda_ropa.repository.CategoriaDomainRepository;
import espe.edu.tienda_ropa.service.impl.CategoriaServiceImpl;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CategoriaServiceImpl.
 * Verifica la lógica de negocio para operaciones CRUD de categorías.
 */
class CategoriaServiceImplTest {

    private CategoriaDomainRepository repo;
    private CategoriaServiceImpl service;

    /**
     * Configuración inicial antes de cada test.
     * Se crean mocks del repositorio y se inyectan al servicio.
     */
    @BeforeEach
    void setUp() {
        repo = mock(CategoriaDomainRepository.class);
        service = new CategoriaServiceImpl(repo);
    }

    /**
     * Test: Crear categoría exitosamente.
     * Verifica que al crear una categoría con nombre único:
     * - Se guarda correctamente en el repositorio
     * - Se retorna un CategoriaResponse con los datos correctos
     * - La categoría se marca como activa por defecto
     */
    @Test
    void testCreateCategoria_Success() {
        CategoriaRequestData req = new CategoriaRequestData();
        req.setNombre("Ropa");
        req.setDescripcion("Categoria de ropa");

        Categoria saved = new Categoria();
        saved.setId(1L);
        saved.setNombre("Ropa");
        saved.setDescripcion("Categoria de ropa");
        saved.setActiva(true);

        when(repo.existsByNombre("Ropa")).thenReturn(false);
        when(repo.save(any(Categoria.class))).thenReturn(saved);

        CategoriaResponse response = service.create(req);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ropa", response.getNombre());
        assertTrue(response.getActiva());

        verify(repo).existsByNombre("Ropa");
        verify(repo).save(any(Categoria.class));
    }

    /**
     * Test: Conflicto al crear categoría con nombre duplicado.
     * Verifica que al intentar crear una categoría con un nombre
     * que ya existe, se lanza ConflictException con mensaje apropiado.
     */
    @Test
    void testCreateCategoria_Conflict() {
        CategoriaRequestData req = new CategoriaRequestData();
        req.setNombre("Ropa");

        when(repo.existsByNombre("Ropa")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class, () -> service.create(req));
        assertEquals("El nombre de categoria ya esta registrado", ex.getMessage());
    }

    /**
     * Test: Obtener categoría por ID exitosamente.
     * Verifica que al buscar una categoría existente por su ID,
     * se retorna correctamente el CategoriaResponse con todos los datos.
     */
    @Test
    void testGetById_Success() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ropa");
        categoria.setDescripcion("Categoria de ropa");
        categoria.setActiva(true);

        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponse response = service.getById(1L);

        assertNotNull(response);
        assertEquals("Ropa", response.getNombre());
    }

    /**
     * Test: Categoría no encontrada por ID.
     * Verifica que al buscar una categoría con ID inexistente,
     * se lanza NotFoundException con el mensaje "Categoria no encontrada".
     */
    @Test
    void testGetById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getById(1L));
        assertEquals("Categoria no encontrada", ex.getMessage());
    }

    /**
     * Test: Listar todas las categorías.
     * Verifica que el método list() retorna correctamente
     * una lista de CategoriaResponse con todas las categorías del sistema.
     */
    @Test
    void testListCategorias() {
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNombre("Ropa");
        c1.setDescripcion("Categoria de ropa");
        c1.setActiva(true);

        when(repo.findAll()).thenReturn(List.of(c1));

        List<CategoriaResponse> list = service.list();
        assertEquals(1, list.size());
        assertEquals("Ropa", list.get(0).getNombre());
    }
}