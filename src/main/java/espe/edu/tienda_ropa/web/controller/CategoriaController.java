package espe.edu.tienda_ropa.web.controller;

import espe.edu.tienda_ropa.dto.CategoriaRequestData;
import espe.edu.tienda_ropa.dto.CategoriaResponse;
import espe.edu.tienda_ropa.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> create(@Valid @RequestBody CategoriaRequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAll() {
        return ResponseEntity.ok(categoriaService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestData request) {
        return ResponseEntity.ok(categoriaService.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CategoriaResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
