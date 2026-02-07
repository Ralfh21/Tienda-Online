package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.repository.DetallePedidoDomainRepository;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import espe.edu.tienda_ropa.reactive.ReactiveOrderService;
import espe.edu.tienda_ropa.service.impl.PedidoServiceImpl;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    private PedidoDomainRepository repo;
    private DetallePedidoService detalleService;
    private DetallePedidoDomainRepository detalleRepo;
    private ProductoDomainRepository productoRepo;
    private ReactiveOrderService reactiveOrderService;
    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(PedidoDomainRepository.class);
        detalleService = mock(DetallePedidoService.class);
        detalleRepo = mock(DetallePedidoDomainRepository.class);
        productoRepo = mock(ProductoDomainRepository.class);
        reactiveOrderService = mock(ReactiveOrderService.class);
        service = new PedidoServiceImpl(repo, detalleService, detalleRepo, productoRepo, reactiveOrderService);
    }

    @Test
    void testCreatePedido_Success() {

        PedidoRequestData req = new PedidoRequestData();
        req.setClienteId(10L);
        req.setTotal(new BigDecimal("150.75"));
        req.setObservaciones("Entregar antes del mediodía");
        req.setDireccionEnvio("Av. Siempre Viva 123");

        Pedido savedPedido = new Pedido();
        savedPedido.setId(1L);
        savedPedido.setClienteId(req.getClienteId());
        savedPedido.setTotal(req.getTotal());
        savedPedido.setObservaciones(req.getObservaciones());
        savedPedido.setDireccionEnvio(req.getDireccionEnvio());
        savedPedido.setFechaPedido(LocalDateTime.now());
        savedPedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        when(repo.save(ArgumentMatchers.any(Pedido.class))).thenReturn(savedPedido);

        PedidoResponse response = service.create(req);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(Pedido.EstadoPedido.PENDIENTE, response.getEstado());
        verify(repo).save(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    void testGetById_Success() {
        Pedido pedido = new Pedido();
        pedido.setId(5L);
        pedido.setClienteId(10L);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        when(repo.findById(5L)).thenReturn(Optional.of(pedido));

        PedidoResponse response = service.getById(5L);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        verify(repo).findById(5L);
    }

    @Test
    void testGetById_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getById(99L));

        assertEquals("Pedido no encontrado", ex.getMessage());
    }

    @Test
    void testListPedidos() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        Pedido p2 = new Pedido();
        p2.setId(2L);

        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<PedidoResponse> pedidos = service.list();

        assertEquals(2, pedidos.size());
        verify(repo).findAll();
    }

    @Test
    void testCancelPedido_Success() {
        Pedido pedido = new Pedido();
        pedido.setId(3L);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        when(repo.findById(3L)).thenReturn(Optional.of(pedido));
        when(repo.save(pedido)).thenReturn(pedido);

        PedidoResponse response = service.cancel(3L);

        assertEquals(Pedido.EstadoPedido.CANCELADO, response.getEstado());
        verify(repo).findById(3L);
        verify(repo).save(pedido);
    }

    @Test
    void testCancelPedido_NotFound() {
        when(repo.findById(100L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.cancel(100L));

        assertEquals("Pedido no encontrado", ex.getMessage());
    }
}
