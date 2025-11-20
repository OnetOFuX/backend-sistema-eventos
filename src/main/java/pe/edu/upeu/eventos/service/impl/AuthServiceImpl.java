package pe.edu.upeu.eventos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.eventos.dto.UsuarioDTO;
import pe.edu.upeu.eventos.dto.request.LoginRequest;
import pe.edu.upeu.eventos.dto.request.RegistroUsuarioRequest;
import pe.edu.upeu.eventos.dto.response.AuthResponse;
import pe.edu.upeu.eventos.security.jwt.JwtUtils;
import pe.edu.upeu.eventos.service.AuthService;
import pe.edu.upeu.eventos.service.UsuarioService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailOrCodigo(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Obtener datos del usuario
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(authentication.getName());

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .usuario(usuario)
                .build();
    }

    @Override
    public AuthResponse registro(RegistroUsuarioRequest request) {
        UsuarioDTO usuario = usuarioService.registrarUsuario(request);

        // Autenticar automáticamente después del registro
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwt = jwtUtils.generateJwtToken(authentication);

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .usuario(usuario)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String token) {
        String email = jwtUtils.getEmailFromJwtToken(token);
        String newToken = jwtUtils.generateTokenFromEmail(email);
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);

        return AuthResponse.builder()
                .token(newToken)
                .type("Bearer")
                .usuario(usuario)
                .build();
    }
}
