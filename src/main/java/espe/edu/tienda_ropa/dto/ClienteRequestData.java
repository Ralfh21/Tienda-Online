package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ClienteRequestData {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank
    @Size(min = 2, max = 100)
    private String apellido;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @Size(max = 15)
    private String telefono;

    @Size(max = 200)
    private String direccion;

    @Size(max = 50)
    private String ciudad;

    @Size(max = 10)
    private String codigoPostal;

    public @NotBlank @Size(min = 2, max = 100) String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank @Size(min = 2, max = 100) String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank @Size(min = 2, max = 100) String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank @Size(min = 2, max = 100) String apellido) {
        this.apellido = apellido;
    }

    public @NotBlank @Email @Size(max = 120) String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email @Size(max = 120) String email) {
        this.email = email;
    }

    public @Size(max = 15) String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Size(max = 15) String telefono) {
        this.telefono = telefono;
    }

    public @Size(max = 200) String getDireccion() {
        return direccion;
    }

    public void setDireccion(@Size(max = 200) String direccion) {
        this.direccion = direccion;
    }

    public @Size(max = 50) String getCiudad() {
        return ciudad;
    }

    public void setCiudad(@Size(max = 50) String ciudad) {
        this.ciudad = ciudad;
    }

    public @Size(max = 10) String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(@Size(max = 10) String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
