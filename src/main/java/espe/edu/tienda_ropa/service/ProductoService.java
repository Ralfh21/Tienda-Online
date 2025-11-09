package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.dto.ProductoRequestData;
import espe.edu.tienda_ropa.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {

    //Crear un producto a partir del DTO validado
    ProductoResponse create(ProductoRequestData request);

    //Busqueda por ID
    ProductoResponse getById(Long id);

    //Listar todos los productos
    List<ProductoResponse> list();

    //Actualizar producto
    ProductoResponse update(Long id, ProductoRequestData request);

    //Cambiar estado del producto (desactivar poniendo stock en 0)
    ProductoResponse deactivate(Long id);

    //Eliminar producto definitivamente
    void delete(Long id);
}
