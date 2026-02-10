package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.Categoria;
import espe.edu.tienda_ropa.dto.CategoriaRequestData;
import espe.edu.tienda_ropa.dto.CategoriaResponse;
import espe.edu.tienda_ropa.repository.CategoriaDomainRepository;
import espe.edu.tienda_ropa.service.CategoriaService;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDomainRepository repo;

    public CategoriaServiceImpl(CategoriaDomainRepository repo) {
        this.repo = repo;
    }

    /**
     * Crea una nueva categoría en el sistema
     * Validaciones:
     * - request no nulo
     * - nombre no nulo ni vacío
     * - descripción no nula
     * - nombre único
     */
    @Override
    public CategoriaResponse create(CategoriaRequestData request) {

        System.out.println("[CREATE] Iniciando creación de categoría...");

        // Validación básica del objeto request
        if (request == null) {
            System.out.println("[ERROR] Request nulo");
            throw new IllegalArgumentException("CategoriaRequestData no puede ser null");
        }

        // Validación del nombre
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] Nombre de categoría vacío o nulo");
            throw new IllegalArgumentException("El nombre de la categoria no puede estar vacío");
        }

        // Validación de descripción
        if (request.getDescripcion() == null) {
            System.out.println("[ERROR] Descripción nula");
            throw new IllegalArgumentException("La descripcion no puede ser null");
        }

        // Validación de unicidad
        if(repo.existsByNombre(request.getNombre())) {
            System.out.println("[ERROR] Nombre duplicado: " + request.getNombre());
            throw new ConflictException("El nombre de categoria ya esta registrado");
        }

        // Construcción de la entidad
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setActiva(true);

        System.out.println("[CREATE] Categoria construida correctamente");

        // Persistencia en base de datos
        Categoria saved = repo.save(categoria);

        System.out.println("[CREATE] Categoria guardada con ID: " + saved.getId());

        // Conversión a DTO
        return toResponse(saved);
    }

    /**
     * Obtiene una categoría por ID
     * Validaciones:
     * - id no nulo
     * - id existente
     */
    @Override
    public CategoriaResponse getById(Long id) {
        System.out.println("[GET] Buscando categoria por ID: " + id);
        // Validación del ID
        if (id == null) {
            System.out.println("[ERROR] ID nulo");
            throw new IllegalArgumentException("El id no puede ser null");
        }
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Categoria no encontrada con ID: " + id);
                    return new NotFoundException("Categoria no encontrada");
                });
        System.out.println("[GET] Categoria encontrada: " + categoria.getNombre());
        return toResponse(categoria);
    }
    /**
     * Lista todas las categorías
     */
    @Override
    public List<CategoriaResponse> list() {
        System.out.println("[LIST] Listando todas las categorias");
        List<CategoriaResponse> lista = repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        System.out.println("[LIST] Total categorias encontradas: " + lista.size());
        return lista;
    }
    /**
     * Actualiza una categoría existente
     * Validaciones:
     * - id no nulo
     * - request no nulo
     * - nombre válido
     * - unicidad del nombre
     */
    @Override
    public CategoriaResponse update(Long id, CategoriaRequestData request) {
        System.out.println("[UPDATE] Actualizando categoria ID: " + id);
        // Validaciones básicas
        if (id == null) {
            System.out.println("[ERROR] ID nulo en update");
            throw new IllegalArgumentException("El id no puede ser null");
        }

        if (request == null) {
            System.out.println("[ERROR] Request nulo en update");
            throw new IllegalArgumentException("CategoriaRequestData no puede ser null");
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] Nombre inválido en update");
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (request.getDescripcion() == null) {
            System.out.println("[ERROR] Descripción nula en update");
            throw new IllegalArgumentException("La descripcion no puede ser null");
        }

        // Recuperación de entidad
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Categoria no encontrada para update");
                    return new NotFoundException("Categoria no encontrada");
                });
        // Validación de unicidad del nombre
        if(!categoria.getNombre().equals(request.getNombre())
                && repo.existsByNombre(request.getNombre())) {
            System.out.println("[ERROR] Nombre duplicado en update: " + request.getNombre());
            throw new ConflictException("El nombre de categoria ya esta registrado");
        }

        // Actualización de datos
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        System.out.println("[UPDATE] Datos actualizados correctamente");
        Categoria updated = repo.save(categoria);
        System.out.println("[UPDATE] Categoria actualizada en BD");
        return toResponse(updated);
    }
    /**
     * Desactiva una categoría
     */
    @Override
    public CategoriaResponse deactivate(Long id) {
        System.out.println("[DEACTIVATE] Desactivando categoria ID: " + id);
        if (id == null) {
            System.out.println("[ERROR] ID nulo en deactivate");
            throw new IllegalArgumentException("El id no puede ser null");
        }

        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Categoria no encontrada para desactivar");
                    return new NotFoundException("Categoria no encontrada");
                });

        categoria.setActiva(false);
        System.out.println("[DEACTIVATE] Categoria marcada como inactiva");
        Categoria updated = repo.save(categoria);
        return toResponse(updated);
    }
    /**
     * Eliminación física de la categoría
     */
    @Override
    public void delete(Long id) {
        System.out.println("[DELETE] Eliminando categoria ID: " + id);
        if (id == null) {
            System.out.println("[ERROR] ID nulo en delete");
            throw new IllegalArgumentException("El id no puede ser null");
        }
        if (!repo.existsById(id)) {
            System.out.println("[ERROR] Categoria no encontrada para eliminar");
            throw new NotFoundException("Categoria no encontrada");
        }
        repo.deleteById(id);
        System.out.println("[DELETE] Categoria eliminada correctamente");
    }
    /**
     * Mapper Entity → DTO
     */
    private CategoriaResponse toResponse(Categoria categoria){
        System.out.println("[MAPPER] Convirtiendo Categoria a CategoriaResponse");
        CategoriaResponse r = new CategoriaResponse();
        r.setId(categoria.getId());
        r.setNombre(categoria.getNombre());
        r.setDescripcion(categoria.getDescripcion());
        r.setActiva(categoria.getActiva());
        return r;
    }
}