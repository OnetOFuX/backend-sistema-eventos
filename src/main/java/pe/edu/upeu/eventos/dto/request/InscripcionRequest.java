package pe.edu.upeu.eventos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.InscripcionEntity;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionRequest {

    @NotNull(message = "El ID del evento es requerido")
    private Long eventoId;

    private InscripcionEntity.MetodoPagoEnum metodoPago;

    private BigDecimal montoPagado;

    private String comprobanteUrl;

    private String observaciones;
}
