package espe.edu.tienda_ropa.repository;

import espe.edu.tienda_ropa.domain.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoDomainRepository extends JpaRepository<DetallePedido, Long> {

    // Buscar detalles por pedido
    List<DetallePedido> findByPedidoId(Long pedidoId);

    // Buscar detalles por producto
    List<DetallePedido> findByProductoId(Long productoId);

    // Responder si existe detalle para un pedido
    boolean existsByPedidoId(Long pedidoId);

    // Responder si existe detalle para un producto
    boolean existsByProductoId(Long productoId);

    // Buscar detalles por pedido y producto específico
    DetallePedido findByPedidoIdAndProductoId(Long pedidoId, Long productoId);
}
