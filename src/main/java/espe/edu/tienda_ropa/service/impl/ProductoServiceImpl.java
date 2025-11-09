package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.domain.Categoria;
import espe.edu.tienda_ropa.dto.ProductoRequestData;
import espe.edu.tienda_ropa.dto.ProductoResponse;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import espe.edu.tienda_ropa.repository.CategoriaDomainRepository;
import espe.edu.tienda_ropa.service.ProductoService;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {
    private final ProductoDomainRepository repo;
    private final CategoriaDomainRepository categoriaRepo;

    public ProductoServiceImpl(ProductoDomainRepository repo, CategoriaDomainRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    @Override
    public ProductoResponse create(ProductoRequestData request) {
        if(repo.existsByNombre(request.getNombre())) {
            throw new ConflictException("El nombre del producto ya esta registrado");
        }

        // Buscar la categoria por ID
        Categoria categoria = categoriaRepo.findById(request.getCategoriaId())
            .orElseThrow(() -> new NotFoundException("Categoria no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(categoria);
        producto.setTalla(request.getTalla());
        producto.setColor(request.getColor());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());

        Producto saved = repo.save(producto);
        return toResponse(saved);
    }

    @Override
    public ProductoResponse getById(Long id) {
        Producto producto = repo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        return toResponse(producto);
    }

    @Override
    public List<ProductoResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ProductoResponse update(Long id, ProductoRequestData request) {
        Producto producto = repo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // Verificar si el nombre ya existe en otro producto
        if(!producto.getNombre().equals(request.getNombre()) && repo.existsByNombre(request.getNombre())) {
            throw new ConflictException("El nombre del producto ya esta registrado");
        }

        // Buscar la categoria por ID
        Categoria categoria = categoriaRepo.findById(request.getCategoriaId())
            .orElseThrow(() -> new NotFoundException("Categoria no encontrada"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(categoria);
        producto.setTalla(request.getTalla());
        producto.setColor(request.getColor());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());

        return toResponse(repo.save(producto));
    }

    @Override
    public ProductoResponse deactivate(Long id) {
        Producto producto = repo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        // Desactivar producto poniendo stock en 0
        producto.setStock(0);
        return toResponse(repo.save(producto));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Producto no encontrado");
        }
        repo.deleteById(id);
    }

    private ProductoResponse toResponse(Producto producto){
        ProductoResponse r = new ProductoResponse();
        r.setId(producto.getId());
        r.setNombre(producto.getNombre());
        r.setDescripcion(producto.getDescripcion());
        r.setPrecio(producto.getPrecio());
        r.setCategoriaId(producto.getCategoria().getId());
        r.setCategoriaNombre(producto.getCategoria().getNombre());
        r.setTalla(producto.getTalla());
        r.setColor(producto.getColor());
        r.setStock(producto.getStock());
        r.setImagenUrl(producto.getImagenUrl());
        return r;
    }
}
