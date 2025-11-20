package pe.edu.upeu.eventos.service;

import pe.edu.upeu.eventos.dto.request.LoginRequest;
import pe.edu.upeu.eventos.dto.request.RegistroUsuarioRequest;
import pe.edu.upeu.eventos.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse registro(RegistroUsuarioRequest request);

    AuthResponse refreshToken(String token);
}
