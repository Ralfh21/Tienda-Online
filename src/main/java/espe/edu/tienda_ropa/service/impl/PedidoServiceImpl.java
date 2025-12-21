package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.DetallePedido;
import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.dto.DetallePedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.repository.DetallePedidoDomainRepository;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import espe.edu.tienda_ropa.service.DetallePedidoService;
import espe.edu.tienda_ropa.service.PedidoService;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import espe.edu.tienda_ropa.reactive.ReactiveOrderService;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDomainRepository repo;
    private final DetallePedidoService detalleService;
    private final DetallePedidoDomainRepository detalleRepo;
    private final ProductoDomainRepository productoRepo;
    private final ReactiveOrderService reactiveOrderService;



    public PedidoServiceImpl(PedidoDomainRepository repo,
                             DetallePedidoService detalleService,
                             DetallePedidoDomainRepository detalleRepo,
                             ProductoDomainRepository productoRepo,
                             ReactiveOrderService reactiveOrderService) {

        this.repo = repo;
        this.detalleService = detalleService;
        this.detalleRepo = detalleRepo;
        this.productoRepo = productoRepo;
        this.reactiveOrderService = reactiveOrderService;
    }



    @Override
    public PedidoResponse create(PedidoRequestData request) {

        // Crear pedido principal
        Pedido pedido = new Pedido();
        pedido.setClienteId(request.getClienteId());
        pedido.setTotal(request.getTotal());
        pedido.setObservaciones(request.getObservaciones());
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setFechaPedido(LocalDateTime.now());

        if (request.getEstado() != null && !request.getEstado().isEmpty()) {
            try {
                pedido.setEstado(Pedido.EstadoPedido.valueOf(request.getEstado().toUpperCase()));
            } catch (IllegalArgumentException e) {
                pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
            }
        } else {
            pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        }

        // Guardar pedido
        Pedido saved = repo.save(pedido);

        // Guardar detalles del pedido
        if (request.getItems() != null) {
            for (DetallePedidoRequestData item : request.getItems()) {
                item.setPedidoId(saved.getId());
                detalleService.create(item);
            }
        }

        return toResponse(saved);
    }
    @Override
    @Transactional
    public PedidoResponse confirm(Long id) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));

        if (pedido.getEstado() == Pedido.EstadoPedido.CONFIRMADO) {
            throw new ConflictException("El pedido ya ha sido confirmado.");
        }

        List<DetallePedido> detalles = detalleRepo.findByPedidoId(id);

        for (DetallePedido detalle : detalles) {
            Producto producto = productoRepo.findById(detalle.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + detalle.getProductoId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new ConflictException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepo.save(producto);
        }

        pedido.setEstado(Pedido.EstadoPedido.CONFIRMADO);
        return toResponse(repo.save(pedido));
    }


    @Override
    public PedidoResponse getById(Long id) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
        return toResponse(pedido);
    }

    @Override
    public List<PedidoResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PedidoResponse cancel(Long id) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));

        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        return toResponse(repo.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse completePago(Long id) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));

        if (pedido.getEstado() == Pedido.EstadoPedido.COMPLETADO) {
            throw new ConflictException("El pedido ya ha sido completado.");
        }

        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            throw new ConflictException("Solo se pueden completar pedidos en estado PENDIENTE.");
        }

        // Verificar stock y reducirlo
        List<DetallePedido> detalles = detalleRepo.findByPedidoId(id);

        for (DetallePedido detalle : detalles) {
            Producto producto = productoRepo.findById(detalle.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + detalle.getProductoId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new ConflictException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepo.save(producto);
        }

        pedido.setEstado(Pedido.EstadoPedido.COMPLETADO);
        Pedido pedidoGuardado = repo.save(pedido);

        // 🔥 DISPARAR FLUJO REACTIVO REAL
        reactiveOrderService.procesarPedidoReal(pedidoGuardado);

        return toResponse(pedidoGuardado);

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
