package pe.edu.upeu.eventos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroUsuarioRequest {

    @NotBlank(message = "El código de estudiante es requerido")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codigo;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;

    @Size(max = 100, message = "La carrera no puede exceder 100 caracteres")
    private String carrera;

    @Size(max = 50, message = "El ciclo no puede exceder 50 caracteres")
    private String ciclo;

    private UsuarioEntity.RolEnum rol;
}
