package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.dto.CategoriaRequestData;
import espe.edu.tienda_ropa.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    //Crear una categoria a partir del DTO validado
    CategoriaResponse create(CategoriaRequestData request);

    //Busqueda por ID
    CategoriaResponse getById(Long id);

    //Listar todas las categorias
    List<CategoriaResponse> list();

    //Actualizar categoria
    CategoriaResponse update(Long id, CategoriaRequestData request);

    //Cambiar estado de la categoria
    CategoriaResponse deactivate(Long id);

    //Eliminar categoria definitivamente
    void delete(Long id);
}
