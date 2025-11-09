package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
import espe.edu.tienda_ropa.service.PedidoService;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoDomainRepository repo;

    public PedidoServiceImpl(PedidoDomainRepository repo) {
        this.repo = repo;
    }

    @Override
    public PedidoResponse create(PedidoRequestData request) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(request.getClienteId());
        pedido.setTotal(request.getTotal());
        pedido.setObservaciones(request.getObservaciones());
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        Pedido saved = repo.save(pedido);
        return toResponse(saved);
    }

    @Override
    public PedidoResponse getById(Long id) {
        Pedido pedido = repo.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
        return toResponse(pedido);
    }

    @Override
    public List<PedidoResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PedidoResponse cancel(Long id) {
        Pedido pedido = repo.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        return toResponse(repo.save(pedido));
    }

    private PedidoResponse toResponse(Pedido pedido){
        PedidoResponse r = new PedidoResponse();
        r.setId(pedido.getId());
        r.setClienteId(pedido.getClienteId());
        r.setFechaPedido(pedido.getFechaPedido());
        r.setEstado(pedido.getEstado());
        r.setTotal(pedido.getTotal());
        r.setObservaciones(pedido.getObservaciones());
        r.setDireccionEnvio(pedido.getDireccionEnvio());
        return r;
    }
}
