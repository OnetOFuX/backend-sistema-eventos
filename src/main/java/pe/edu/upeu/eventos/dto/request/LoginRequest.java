package pe.edu.upeu.eventos.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El email o código es requerido")
    private String emailOrCodigo;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
