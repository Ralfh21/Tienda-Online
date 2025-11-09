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

    @Override
    public CategoriaResponse create(CategoriaRequestData request) {
        if(repo.existsByNombre(request.getNombre())) {
            throw new ConflictException("El nombre de categoria ya esta registrado");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setActiva(true);

        Categoria saved = repo.save(categoria);
        return toResponse(saved);
    }

    @Override
    public CategoriaResponse getById(Long id) {
        Categoria categoria = repo.findById(id).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));
        return toResponse(categoria);
    }

    @Override
    public List<CategoriaResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CategoriaResponse update(Long id, CategoriaRequestData request) {
        Categoria categoria = repo.findById(id).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));

        // Verificar si el nombre ya existe en otra categoria
        if(!categoria.getNombre().equals(request.getNombre()) && repo.existsByNombre(request.getNombre())) {
            throw new ConflictException("El nombre de categoria ya esta registrado");
        }

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        return toResponse(repo.save(categoria));
    }

    @Override
    public CategoriaResponse deactivate(Long id) {
        Categoria categoria = repo.findById(id).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));
        categoria.setActiva(false);
        return toResponse(repo.save(categoria));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Categoria no encontrada");
        }
        repo.deleteById(id);
    }

    private CategoriaResponse toResponse(Categoria categoria){
        CategoriaResponse r = new CategoriaResponse();
        r.setId(categoria.getId());
        r.setNombre(categoria.getNombre());
        r.setDescripcion(categoria.getDescripcion());
        r.setActiva(categoria.getActiva());
        return r;
    }
}
