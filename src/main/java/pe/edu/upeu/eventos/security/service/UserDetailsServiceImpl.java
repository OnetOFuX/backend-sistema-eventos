package pe.edu.upeu.eventos.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.repository.UsuarioRepository;
import pe.edu.upeu.eventos.security.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByEmailOrCodigo(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email o código: " + username
                ));

        return UserDetailsImpl.build(usuario);
    }
}
