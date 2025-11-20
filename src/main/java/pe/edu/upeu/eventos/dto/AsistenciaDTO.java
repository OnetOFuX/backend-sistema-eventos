package pe.edu.upeu.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.AsistenciaEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApellido;
    private String usuarioCodigo;
    private Long fechaEventoId;
    private LocalDateTime fechaRegistro;
    private AsistenciaEntity.TipoRegistroEnum tipoRegistro;
    private Long registradoPorId;
    private String registradoPorNombre;
    private String observaciones;
    private LocalDateTime createdAt;
}
