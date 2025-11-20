package pe.edu.upeu.eventos.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String codigo;
    private String email;
    private String password;
    private UsuarioEntity.RolEnum rol;
    private Boolean activo;
    private String carrera;

    public static UserDetailsImpl build(UsuarioEntity usuario) {
        return UserDetailsImpl.builder()
                .id(usuario.getId())
                .codigo(usuario.getCodigo())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .carrera(usuario.getCarrera())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + rol.name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
