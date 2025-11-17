package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Producto;
import espe.edu.tienda_ropa.dto.DetallePedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoRequestData;
import espe.edu.tienda_ropa.dto.PedidoResponse;
import espe.edu.tienda_ropa.repository.PedidoDomainRepository;
import espe.edu.tienda_ropa.repository.ProductoDomainRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PedidoIntegrationTest {

    @Autowired
    private ProductoDomainRepository productoRepo;

    @Autowired
    private PedidoDomainRepository pedidoRepo;

    @Autowired
    private ClienteDomainRepository clienteRepo;

    @Autowired
    private PedidoService pedidoService;

    @Test
    public void crearYConfirmarPedido_debeCambiarEstadoYReducirStock() {
        // Crear producto de prueba
        Producto p = new Producto();
        p.setNombre("Prueba");
        p.setDescripcion("Producto de prueba");
        p.setPrecio(new BigDecimal("10.00"));
        p.setTalla("M");
        p.setColor("Negro");
        p.setStock(10);
        Producto saved = productoRepo.save(p);

        // Crear cliente de prueba
        Cliente c = new Cliente();
        c.setNombre("Test");
        c.setApellido("User");
        c.setCorreo("test@example.com");
        c.setTelefono("0999999999");
        // agrega cualquier otro campo @NotNull
        Cliente savedCliente = clienteRepo.save(c);

        // Preparar pedido con detalle
        DetallePedidoRequestData detalle = new DetallePedidoRequestData();
        detalle.setProductoId(saved.getId());
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(saved.getPrecio());

        PedidoRequestData pedidoReq = new PedidoRequestData();
        pedidoReq.setClienteId(savedCliente.getId());
        pedidoReq.setTotal(new BigDecimal("22.00"));
        pedidoReq.setDireccionEnvio("Calle Test 123");
        pedidoReq.setObservaciones("");
        pedidoReq.setItems(List.of(detalle));

        // Crear pedido
        PedidoResponse creado = pedidoService.create(pedidoReq);
        assertNotNull(creado.getId(), "El pedido creado debe tener ID");

        // Confirmar pedido
        PedidoResponse confirmado = pedidoService.confirm(creado.getId());
        assertNotNull(confirmado, "Respuesta de confirmación no debe ser nula");
        assertEquals(espe.edu.tienda_ropa.domain.Pedido.EstadoPedido.CONFIRMADO, confirmado.getEstado(), "Estado debe ser CONFIRMADO");

        // Verificar en BD
        var opt = pedidoRepo.findById(creado.getId());
        assertTrue(opt.isPresent());
        assertEquals(espe.edu.tienda_ropa.domain.Pedido.EstadoPedido.CONFIRMADO, opt.get().getEstado());

        // Verificar stock reducido
        Producto prodAfter = productoRepo.findById(saved.getId()).orElseThrow();
        assertEquals(8, prodAfter.getStock());
    }
}
