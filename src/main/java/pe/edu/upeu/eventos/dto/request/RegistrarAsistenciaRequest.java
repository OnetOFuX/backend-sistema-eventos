package pe.edu.upeu.eventos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.AsistenciaEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarAsistenciaRequest {

    @NotNull(message = "El ID de la fecha del evento es requerido")
    private Long fechaEventoId;

    private String codigoQr;

    private String codigoEstudiante;

    @NotNull(message = "El tipo de registro es requerido")
    private AsistenciaEntity.TipoRegistroEnum tipoRegistro;

    private String observaciones;
}
