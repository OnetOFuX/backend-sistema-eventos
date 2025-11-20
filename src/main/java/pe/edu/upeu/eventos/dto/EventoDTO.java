package pe.edu.upeu.eventos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // <-- esto evita los null en la respuesta
public class EventoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private EventoEntity.TipoEventoEnum tipo;
    private String ubicacion;
    private Integer cupoMaximo;
    private String imagenUrl;
    private Boolean activo;
    private Boolean esPago;
    private BigDecimal precio;
    private List<String> metodosPago;
    private Boolean requiereComprobante;
    private Long creadorId;
    private String creadorNombre;
    private List<FechaEventoDTO> fechas;
    private Integer totalInscritos;
    private Integer cuposDisponibles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String carrera; // carrera objetivo del evento o 'TODAS'
    private String facultad; // facultad objetivo del evento o 'TODAS'
}
