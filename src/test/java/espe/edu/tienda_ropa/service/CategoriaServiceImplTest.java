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
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CategoriaServiceImpl
 * Incluye:
 * - validaciones
 * - IllegalArgumentException
 * - reglas de negocio
 * - conflictos
 * - not found
 * - uso de captor
 * - estructura Arrange / Act / Assert
 */
class CategoriaServiceImplTest {

    private CategoriaDomainRepository repo;
    private CategoriaServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(CategoriaDomainRepository.class);
        service = new CategoriaServiceImpl(repo);
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createCategoria_validData_shouldSaveAndReturnResponse() {
        // =========================
        // ARRANGE
        // =========================
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

        // =========================
        // ACT
        // =========================
        CategoriaResponse response = service.create(req);

        // =========================
        // ASSERT
        // =========================
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ropa", response.getNombre());
        assertEquals("Categoria de ropa", response.getDescripcion());
        assertTrue(response.getActiva());

        // =========================
        // VERIFY
        // =========================
        verify(repo, times(1)).existsByNombre("Ropa");
        verify(repo, times(1)).save(any(Categoria.class));
    }

    @Test
    void createCategoria_nullRequest_shouldThrowIllegalArgument() {
        // =========================
        // ARRANGE
        // =========================
        CategoriaRequestData req = null;

        // =========================
        // ACT + ASSERT
        // =========================
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(req)
        );

        assertEquals("CategoriaRequestData no puede ser null", ex.getMessage());

        // =========================
        // VERIFY
        // =========================
        verifyNoInteractions(repo);
    }

    @Test
    void createCategoria_emptyName_shouldThrowIllegalArgument() {
        // =========================
        // ARRANGE
        // =========================
        CategoriaRequestData req = new CategoriaRequestData();
        req.setNombre("   ");
        req.setDescripcion("desc");

        // =========================
        // ACT + ASSERT
        // =========================
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(req)
        );

        assertEquals("El nombre de la categoria no puede estar vacío", ex.getMessage());

        verifyNoInteractions(repo);
    }

    @Test
    void createCategoria_duplicateName_shouldThrowConflict() {
        // =========================
        // ARRANGE
        // =========================
        CategoriaRequestData req = new CategoriaRequestData();
        req.setNombre("Ropa");
        req.setDescripcion("desc");

        when(repo.existsByNombre("Ropa")).thenReturn(true);

        // =========================
        // ACT + ASSERT
        // =========================
        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> service.create(req)
        );

        assertEquals("El nombre de categoria ya esta registrado", ex.getMessage());

        verify(repo, times(1)).existsByNombre("Ropa");
        verify(repo, never()).save(any());
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Test
    void getById_validId_shouldReturnCategoria() {
        // =========================
        // ARRANGE
        // =========================
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ropa");
        categoria.setDescripcion("desc");
        categoria.setActiva(true);

        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        // =========================
        // ACT
        // =========================
        CategoriaResponse response = service.getById(1L);

        // =========================
        // ASSERT
        // =========================
        assertNotNull(response);
        assertEquals("Ropa", response.getNombre());

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void getById_nullId_shouldThrowIllegalArgument() {
        // =========================
        // ACT + ASSERT
        // =========================
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.getById(null)
        );

        assertEquals("El id no puede ser null", ex.getMessage());

        verifyNoInteractions(repo);
    }

    @Test
    void getById_notFound_shouldThrowNotFound() {
        // =========================
        // ARRANGE
        // =========================
        when(repo.findById(10L)).thenReturn(Optional.empty());

        // =========================
        // ACT + ASSERT
        // =========================
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.getById(10L)
        );

        assertEquals("Categoria no encontrada", ex.getMessage());
    }

    // =========================================================
    // LIST
    // =========================================================

    @Test
    void listCategorias_shouldReturnList() {
        // =========================
        // ARRANGE
        // =========================
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNombre("Ropa");
        c1.setDescripcion("desc");
        c1.setActiva(true);

        when(repo.findAll()).thenReturn(List.of(c1));

        // =========================
        // ACT
        // =========================
        List<CategoriaResponse> list = service.list();

        // =========================
        // ASSERT
        // =========================
        assertEquals(1, list.size());
        assertEquals("Ropa", list.get(0).getNombre());

        verify(repo, times(1)).findAll();
    }

    // =========================================================
    // UPDATE (con captor)
    // =========================================================

    @Test
    void updateCategoria_validData_shouldUpdateAndSave_usingCaptor() {
        // =========================
        // ARRANGE
        // =========================
        Long id = 1L;

        CategoriaRequestData req = new CategoriaRequestData();
        req.setNombre("Nueva Ropa");
        req.setDescripcion("Nueva desc");

        Categoria existing = new Categoria();
        existing.setId(id);
        existing.setNombre("Ropa");
        existing.setDescripcion("desc");
        existing.setActiva(true);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.existsByNombre("Nueva Ropa")).thenReturn(false);

        // MOCK CORRECTO DEL SAVE
        when(repo.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Categoria> captor = ArgumentCaptor.forClass(Categoria.class);

        // =========================
        // ACT
        // =========================
        CategoriaResponse response = service.update(id, req);

        // =========================
        // ASSERT
        // =========================
        verify(repo).save(captor.capture());
        Categoria saved = captor.getValue();

        assertEquals("Nueva Ropa", saved.getNombre());
        assertEquals("Nueva desc", saved.getDescripcion());
        assertTrue(saved.getActiva());

        // Validación del response
        assertNotNull(response);
        assertEquals("Nueva Ropa", response.getNombre());
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteCategoria_validId_shouldDelete() {
        // =========================
        // ARRANGE
        // =========================
        when(repo.existsById(1L)).thenReturn(true);

        // =========================
        // ACT
        // =========================
        service.delete(1L);

        // =========================
        // ASSERT
        // =========================
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategoria_notFound_shouldThrowNotFound() {
        // =========================
        // ARRANGE
        // =========================
        when(repo.existsById(1L)).thenReturn(false);

        // =========================
        // ACT + ASSERT
        // =========================
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.delete(1L)
        );

        assertEquals("Categoria no encontrada", ex.getMessage());

        verify(repo, never()).deleteById(any());
    }
}