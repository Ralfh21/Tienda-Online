package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Pedido;
import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
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
    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(PedidoDomainRepository.class);
        service = new PedidoServiceImpl(repo);
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

        assertNotNull(response, "El response no debería ser nulo");
        assertEquals(1L, response.getId(), "El ID del pedido debería ser 1");
        assertEquals(Pedido.EstadoPedido.PENDIENTE, response.getEstado(), "El estado del pedido debería ser PENDIENTE");
        assertEquals(req.getDireccionEnvio(), response.getDireccionEnvio(), "La dirección de envío debe coincidir");
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

        assertNotNull(response, "El pedido debería ser encontrado");
        assertEquals(5L, response.getId(), "El ID debe coincidir con el pedido buscado");
        verify(repo).findById(5L);
    }

    @Test
    void testGetById_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertEquals("Pedido no encontrado", ex.getMessage(), "El mensaje de excepción debe coincidir");
    }

    @Test
    void testListPedidos() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        Pedido p2 = new Pedido();
        p2.setId(2L);

        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<PedidoResponse> pedidos = service.list();

        assertEquals(2, pedidos.size(), "Debe listar todos los pedidos existentes");
        assertTrue(pedidos.stream().anyMatch(p -> p.getId() == 1L), "Debe contener el pedido con ID 1");
        assertTrue(pedidos.stream().anyMatch(p -> p.getId() == 2L), "Debe contener el pedido con ID 2");
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

        assertEquals(Pedido.EstadoPedido.CANCELADO, response.getEstado(), "El pedido debe quedar CANCELADO");
        verify(repo).findById(3L);
        verify(repo).save(pedido);
    }

    @Test
    void testCancelPedido_NotFound() {
        when(repo.findById(100L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.cancel(100L));
        assertEquals("Pedido no encontrado", ex.getMessage(), "El mensaje de excepción debe coincidir");
    }
}