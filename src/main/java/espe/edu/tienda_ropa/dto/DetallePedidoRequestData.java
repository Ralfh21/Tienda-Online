package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DetallePedidoRequestData {

    @NotNull
    private Long pedidoId;

    @NotNull
    private Long productoId;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioUnitario;

    public @NotNull Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(@NotNull Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public @NotNull Long getProductoId() {
        return productoId;
    }

    public void setProductoId(@NotNull Long productoId) {
        this.productoId = productoId;
    }

    public @NotNull @Min(value = 1) Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NotNull @Min(value = 1) Integer cantidad) {
        this.cantidad = cantidad;
    }

    public @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
