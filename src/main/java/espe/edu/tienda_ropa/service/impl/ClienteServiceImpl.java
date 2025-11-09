package espe.edu.tienda_ropa.service.impl;

import espe.edu.tienda_ropa.domain.Cliente;
import espe.edu.tienda_ropa.dto.ClienteRequestData;
import espe.edu.tienda_ropa.dto.ClienteResponse;
import espe.edu.tienda_ropa.repository.ClienteDomainRepository;
import espe.edu.tienda_ropa.service.ClienteService;
import espe.edu.tienda_ropa.web.advice.ConflictException;
import espe.edu.tienda_ropa.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteDomainRepository repo;

    public ClienteServiceImpl(ClienteDomainRepository repo) {
        this.repo = repo;
    }

    @Override
    public ClienteResponse create(ClienteRequestData request) {
        if(repo.existsByEmail(request.getEmail())) {
            throw new ConflictException("El email ya esta registrado");
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setCodigoPostal(request.getCodigoPostal());
        // Establecer fechaRegistro explícitamente
        cliente.setFechaRegistro(java.time.LocalDateTime.now());
        cliente.setActivo(true);

        Cliente saved = repo.save(cliente);
        return toResponse(saved);
    }

    @Override
    public ClienteResponse getById(Long id) {
        Cliente cliente = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return toResponse(cliente);
    }

    @Override
    public List<ClienteResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ClienteResponse deactivate(Long id) {
        Cliente cliente = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        cliente.setActivo(false);
        return toResponse(repo.save(cliente));
    }

    private ClienteResponse toResponse(Cliente cliente){
        ClienteResponse r = new ClienteResponse();
        r.setId(cliente.getId());
        r.setNombre(cliente.getNombre());
        r.setApellido(cliente.getApellido());
        r.setEmail(cliente.getEmail());
        r.setTelefono(cliente.getTelefono());
        r.setDireccion(cliente.getDireccion());
        r.setCiudad(cliente.getCiudad());
        r.setCodigoPostal(cliente.getCodigoPostal());
        r.setFechaRegistro(cliente.getFechaRegistro());
        r.setActivo(cliente.getActivo());
        return r;
    }
}
