package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.DetallePedido;
import espe.edu.tienda_ropa.dto.DetallePedidoRequestData;
import espe.edu.tienda_ropa.dto.DetallePedidoResponse;
import espe.edu.tienda_ropa.repository.DetallePedidoDomainRepository;
import espe.edu.tienda_ropa.service.DetallePedidoService;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {
    private final DetallePedidoDomainRepository repo;

    public DetallePedidoServiceImpl(DetallePedidoDomainRepository repo) {
        this.repo = repo;
    }

    @Override
    public DetallePedidoResponse create(DetallePedidoRequestData request) {
        DetallePedido detalle = new DetallePedido();
        detalle.setPedidoId(request.getPedidoId());
        detalle.setProductoId(request.getProductoId());
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(request.getPrecioUnitario());
        // Calcular subtotal
        BigDecimal subtotal = request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad()));
        detalle.setSubtotal(subtotal);

        DetallePedido saved = repo.save(detalle);
        return toResponse(saved);
    }

    @Override
    public DetallePedidoResponse getById(Long id) {
        DetallePedido detalle = repo.findById(id).orElseThrow(() -> new NotFoundException("Detalle de pedido no encontrado"));
        return toResponse(detalle);
    }

    @Override
    public List<DetallePedidoResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<DetallePedidoResponse> findByPedidoId(Long pedidoId) {
        return repo.findByPedidoId(pedidoId).stream().map(this::toResponse).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Detalle de pedido no encontrado");
        }
        repo.deleteById(id);
    }

    private DetallePedidoResponse toResponse(DetallePedido detalle){
        DetallePedidoResponse r = new DetallePedidoResponse();
        r.setId(detalle.getId());
        r.setPedidoId(detalle.getPedidoId());
        r.setProductoId(detalle.getProductoId());
        r.setCantidad(detalle.getCantidad());
        r.setPrecioUnitario(detalle.getPrecioUnitario());
        r.setSubtotal(detalle.getSubtotal());
        return r;
    }
}
