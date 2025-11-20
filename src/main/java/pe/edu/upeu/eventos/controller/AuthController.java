package pe.edu.upeu.eventos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.eventos.dto.request.LoginRequest;
import pe.edu.upeu.eventos.dto.request.RegistroUsuarioRequest;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.dto.response.AuthResponse;
import pe.edu.upeu.eventos.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<AuthResponse>> registro(@Valid @RequestBody RegistroUsuarioRequest request) {
        AuthResponse response = authService.registro(request);
        return ResponseEntity.ok(ApiResponse.success("Registro exitoso", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remover "Bearer "
        AuthResponse response = authService.refreshToken(jwt);
        return ResponseEntity.ok(ApiResponse.success("Token actualizado", response));
    }
}
