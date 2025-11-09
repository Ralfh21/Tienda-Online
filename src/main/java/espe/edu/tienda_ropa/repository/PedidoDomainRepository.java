package espe.edu.tienda_ropa.repository;

import espe.edu.tienda_ropa.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDomainRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar pedidos por estado
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    // Responder si existe pedido para un cliente
    boolean existsByClienteId(Long clienteId);

    // Buscar pedidos por rango de fechas
    List<Pedido> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar pedidos ordenados por fecha descendente
    List<Pedido> findAllByOrderByFechaPedidoDesc();
}
