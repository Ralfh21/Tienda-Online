package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;

public class CategoriaRequestData {

    @NotBlank
    @Size(min = 2, max = 50)
    private String nombre;

    @Size(max = 200)
    private String descripcion;

    public @NotBlank @Size(min = 2, max = 50) String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank @Size(min = 2, max = 50) String nombre) {
        this.nombre = nombre;
    }

    public @Size(max = 200) String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@Size(max = 200) String descripcion) {
        this.descripcion = descripcion;
    }
}
