package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoRequestData {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precio;

    @NotNull
    private Long categoriaId;

    @NotBlank
    @Size(max = 20)
    private String talla;

    @NotBlank
    @Size(max = 30)
    private String color;

    @Min(value = 0)
    private Integer stock = 0;

    @Size(max = 500)
    private String imagenUrl;

    public @NotBlank @Size(min = 2, max = 100) String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank @Size(min = 2, max = 100) String nombre) {
        this.nombre = nombre;
    }

    public @Size(max = 500) String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@Size(max = 500) String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal precio) {
        this.precio = precio;
    }

    public @NotNull Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(@NotNull Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public @NotBlank @Size(max = 20) String getTalla() {
        return talla;
    }

    public void setTalla(@NotBlank @Size(max = 20) String talla) {
        this.talla = talla;
    }

    public @NotBlank @Size(max = 30) String getColor() {
        return color;
    }

    public void setColor(@NotBlank @Size(max = 30) String color) {
        this.color = color;
    }

    public @Min(value = 0) Integer getStock() {
        return stock;
    }

    public void setStock(@Min(value = 0) Integer stock) {
        this.stock = stock;
    }

    public @Size(max = 500) String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(@Size(max = 500) String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
