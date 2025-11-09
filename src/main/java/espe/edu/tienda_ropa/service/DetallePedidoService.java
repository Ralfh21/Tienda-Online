package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.dto.DetallePedidoRequestData;
import espe.edu.tienda_ropa.dto.DetallePedidoResponse;

import java.util.List;

public interface DetallePedidoService {

    //Crear un detalle de pedido a partir del DTO validado
    DetallePedidoResponse create(DetallePedidoRequestData request);

    //Busqueda por ID
    DetallePedidoResponse getById(Long id);

    //Listar todos los detalles de pedido
    List<DetallePedidoResponse> list();

    //Listar detalles por pedido ID
    List<DetallePedidoResponse> findByPedidoId(Long pedidoId);

    //Eliminar detalle de pedido
    void deleteById(Long id);
}
