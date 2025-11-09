package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.dto.ClienteRequestData;
import espe.edu.tienda_ropa.dto.ClienteResponse;

import java.util.List;

public interface ClienteService {

    //Crear un cliente a partir del DTO validado
    ClienteResponse create(ClienteRequestData request);

    //Busqueda por ID
    ClienteResponse getById(Long id);

    //Listar todos los clientes
    List<ClienteResponse> list();

    //Cambiar estado del cliente
    ClienteResponse deactivate(Long id);
}
