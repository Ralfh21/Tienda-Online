package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;

import java.util.List;

public interface PedidoService {

    //Crear un pedido a partir del DTO validado
    PedidoResponse create(PedidoRequestData request);

    //Busqueda por ID
    PedidoResponse getById(Long id);

    //Listar todos los pedidos
    List<PedidoResponse> list();

    //Cambiar estado del pedido (cancelar)
    PedidoResponse cancel(Long id);
}
