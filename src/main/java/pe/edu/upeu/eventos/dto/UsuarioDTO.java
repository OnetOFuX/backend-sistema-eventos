package pe.edu.upeu.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String codigo;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String carrera;
    private String ciclo;
    private UsuarioEntity.RolEnum rol;
    private Boolean activo;
    private String fotoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
