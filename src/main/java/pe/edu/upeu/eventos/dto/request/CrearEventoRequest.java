package pe.edu.upeu.eventos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.EventoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearEventoRequest {

    @NotBlank(message = "El título es requerido")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    private String descripcion;


    @NotNull(message = "El tipo de evento es requerido")
    private EventoEntity.TipoEventoEnum tipo;

    @NotBlank(message = "La ubicación es requerida")
    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    private String ubicacion;

    private Integer cupoMaximo;

    private String imagenUrl;

    @NotNull(message = "Debe especificar si el evento es de pago")
    private Boolean esPago;

    private BigDecimal precio;

    @NotNull
    private List<String> metodosPago;

    private Boolean requiereComprobante;

    @NotNull(message = "Debe proporcionar al menos una fecha para el evento")
    private List<FechaEventoRequest> fechas;

    private Long carreraId; // ID de la carrera objetivo (opcional)

    private Long facultadId; // ID de la facultad objetivo (opcional)

    private Boolean paraTodas; // TRUE = evento visible para todos (opcional, default false)

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FechaEventoRequest {
        @NotNull(message = "La fecha de inicio es requerida")
        private LocalDateTime fechaInicio;

        @NotNull(message = "La fecha de fin es requerida")
        private LocalDateTime fechaFin;

        private String observaciones;
    }
}
