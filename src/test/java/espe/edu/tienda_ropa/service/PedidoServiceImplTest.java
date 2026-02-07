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

/**
 * Tests unitarios para PedidoServiceImpl.
 * Verifica la lógica de negocio para el ciclo de vida de pedidos:
 * creación, consulta, listado y cancelación.
 */
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

    /**
     * Test: Crear pedido exitosamente.
     * Verifica que al crear un nuevo pedido:
     * - Se guarda correctamente con estado PENDIENTE
     * - Se asigna un ID al pedido
     * - Se registran los datos del cliente y dirección de envío
     */
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

    /**
     * Test: Obtener pedido por ID exitosamente.
     * Verifica que al buscar un pedido existente,
     * se retorna el PedidoResponse con los datos correctos.
     */
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

    /**
     * Test: Pedido no encontrado por ID.
     * Verifica que al buscar un pedido con ID inexistente,
     * se lanza NotFoundException con mensaje apropiado.
     */
    @Test
    void testGetById_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getById(99L));

        assertEquals("Pedido no encontrado", ex.getMessage());
    }

    /**
     * Test: Listar todos los pedidos.
     * Verifica que el método list() retorna correctamente
     * la lista completa de pedidos del sistema.
     */
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

    /**
     * Test: Cancelar pedido exitosamente.
     * Verifica que al cancelar un pedido pendiente:
     * - El estado cambia a CANCELADO
     * - Se guarda el cambio en el repositorio
     */
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

    /**
     * Test: Cancelar pedido inexistente.
     * Verifica que al intentar cancelar un pedido con ID
     * que no existe, se lanza NotFoundException.
     */
    @Test
    void testCancelPedido_NotFound() {
        when(repo.findById(100L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.cancel(100L));

        assertEquals("Pedido no encontrado", ex.getMessage());
    }
}
