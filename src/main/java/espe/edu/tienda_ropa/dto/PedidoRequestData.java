package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PedidoRequestData {

    @NotNull
    private Long clienteId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal total;

    @Size(max = 500)
    private String observaciones;

    @Size(max = 200)
    private String direccionEnvio;

    public @NotNull Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(@NotNull Long clienteId) {
        this.clienteId = clienteId;
    }

    public @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal getTotal() {
        return total;
    }

    public void setTotal(@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal total) {
        this.total = total;
    }

    public @Size(max = 500) String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(@Size(max = 500) String observaciones) {
        this.observaciones = observaciones;
    }

    public @Size(max = 200) String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(@Size(max = 200) String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
