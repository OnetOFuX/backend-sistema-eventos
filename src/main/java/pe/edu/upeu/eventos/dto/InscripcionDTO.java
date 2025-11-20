package pe.edu.upeu.eventos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.InscripcionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InscripcionDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApellido;
    private String usuarioCodigo;
    private Long eventoId;
    private String eventoTitulo;
    private InscripcionEntity.EstadoInscripcionEnum estado;
    private String codigoQr;
    private InscripcionEntity.MetodoPagoEnum metodoPago;
    private BigDecimal montoPagado;
    private String comprobanteUrl;
    private LocalDateTime fechaPago;
    private Long verificadoPor;
    private String verificadoPorNombre;
    private LocalDateTime fechaVerificacion;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
