package pe.edu.upeu.eventos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // <-- esto evita los null en la respuesta
public class FechaEventoDTO {

    private Long id;
    private Long eventoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String observaciones;
    private Integer totalAsistencias;
    private LocalDateTime createdAt;
}
