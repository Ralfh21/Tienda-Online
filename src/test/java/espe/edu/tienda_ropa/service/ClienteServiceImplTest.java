package espe.edu.tienda_ropa.service;

import espe.edu.tienda_ropa.domain.Cliente;
import espe.edu.tienda_ropa.dto.ClienteRequestData;
import espe.edu.tienda_ropa.dto.ClienteResponse;
import espe.edu.tienda_ropa.repository.ClienteDomainRepository;
import espe.edu.tienda_ropa.service.impl.ClienteServiceImpl;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    private ClienteDomainRepository repo;
    private ClienteServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(ClienteDomainRepository.class);
        service = new ClienteServiceImpl(repo);
    }

    @Test
    @DisplayName("Crear cliente exitosamente")
    void testCreateCliente_Success() {
        ClienteRequestData req = new ClienteRequestData();
        req.setNombre("Juan");
        req.setApellido("Perez");
        req.setEmail("juan@example.com");
        req.setTelefono("0999999999");
        req.setDireccion("Av. Siempre Viva 123");
        req.setCiudad("Quito");
        req.setCodigoPostal("170150");

        Cliente saved = new Cliente();
        saved.setId(1L);
        saved.setNombre(req.getNombre());
        saved.setApellido(req.getApellido());
        saved.setEmail(req.getEmail());
        saved.setTelefono(req.getTelefono());
        saved.setDireccion(req.getDireccion());
        saved.setCiudad(req.getCiudad());
        saved.setCodigoPostal(req.getCodigoPostal());
        saved.setFechaRegistro(LocalDateTime.now());
        saved.setActivo(true);

        when(repo.existsByEmail("juan@example.com")).thenReturn(false);
        when(repo.save(any(Cliente.class))).thenReturn(saved);

        ClienteResponse response = service.create(req);

        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals("Juan", response.getNombre(), "El nombre del cliente debe ser 'Juan'");
        assertTrue(response.getActivo(), "El cliente debe estar activo al crearlo");
        assertEquals("juan@example.com", response.getEmail(), "El email debe coincidir");

        verify(repo).existsByEmail("juan@example.com");
        verify(repo).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Crear cliente con email duplicado lanza ConflictException")
    void testCreateCliente_Conflict() {
        ClienteRequestData req = new ClienteRequestData();
        req.setEmail("juan@example.com");

        when(repo.existsByEmail("juan@example.com")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class,
                () -> service.create(req),
                "Se esperaba ConflictException al crear cliente con email duplicado");

        assertEquals("El email ya esta registrado", ex.getMessage(), "El mensaje de excepción debe coincidir");
    }

    @Test
    @DisplayName("Obtener cliente por ID exitosamente")
    void testGetById_Success() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setEmail("juan@example.com");
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponse response = service.getById(1L);

        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(1L, response.getId(), "El ID debe ser 1");
        assertEquals("Juan", response.getNombre(), "El nombre debe ser Juan");
    }

    @Test
    @DisplayName("Obtener cliente por ID inexistente lanza NotFoundException")
    void testGetById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.getById(1L),
                "Se esperaba NotFoundException para cliente no encontrado");

        assertEquals("Cliente no encontrado", ex.getMessage(), "El mensaje de excepción debe coincidir");
    }

    @Test
    @DisplayName("Listar clientes devuelve lista correctamente")
    void testListClientes() {
        Cliente c1 = new Cliente();
        c1.setId(1L);
        c1.setNombre("Juan");
        c1.setEmail("juan@example.com");
        c1.setActivo(true);

        Cliente c2 = new Cliente();
        c2.setId(2L);
        c2.setNombre("Maria");
        c2.setEmail("maria@example.com");
        c2.setActivo(true);

        when(repo.findAll()).thenReturn(List.of(c1, c2));

        List<ClienteResponse> list = service.list();

        assertEquals(2, list.size(), "La lista debería contener 2 clientes");
        assertEquals("Juan", list.get(0).getNombre(), "Primer cliente debe ser Juan");
        assertEquals("Maria", list.get(1).getNombre(), "Segundo cliente debe ser Maria");
    }

    @Test
    @DisplayName("Desactivar cliente exitosamente")
    void testDeactivateCliente_Success() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setActivo(true);

        when(repo.findById(1L)).thenReturn(Optional.of(cliente));
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        ClienteResponse response = service.deactivate(1L);

        assertFalse(response.getActivo(), "El cliente debe estar inactivo después de desactivar");
        verify(repo).findById(1L);
        verify(repo).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Desactivar cliente inexistente lanza NotFoundException")
    void testDeactivateCliente_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.deactivate(1L),
                "Se esperaba NotFoundException para desactivar cliente inexistente");

        assertEquals("Cliente no encontrado", ex.getMessage(), "El mensaje de excepción debe coincidir");
    }
}