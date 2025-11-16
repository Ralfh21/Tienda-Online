package espe.edu.tienda_ropa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

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

    // 🔥 NUEVO CAMPO
    @NotNull
    private List<DetallePedidoRequestData> items;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public List<DetallePedidoRequestData> getItems() { return items; }
    public void setItems(List<DetallePedidoRequestData> items) { this.items = items; }
}
