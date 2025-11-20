package pe.edu.upeu.eventos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.eventos.dto.UsuarioDTO;
import pe.edu.upeu.eventos.dto.request.RegistroUsuarioRequest;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.mapper.UsuarioMapper;
import pe.edu.upeu.eventos.repository.UsuarioRepository;
import pe.edu.upeu.eventos.service.UsuarioService;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO registrarUsuario(RegistroUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (usuarioRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("El código de estudiante ya está registrado");
        }

        UsuarioEntity usuario = UsuarioEntity.builder()
                .codigo(request.getCodigo())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .carrera(request.getCarrera())
                .ciclo(request.getCiclo())
                .rol(request.getRol() != null ? request.getRol() : UsuarioEntity.RolEnum.PARTICIPANTE)
                .activo(true)
                .build();

        UsuarioEntity usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.usuarioEntityToUsuarioDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.usuarioEntityToUsuarioDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return usuarioMapper.usuarioEntityToUsuarioDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorCodigo(String codigo) {
        UsuarioEntity usuario = usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con código: " + codigo));
        return usuarioMapper.usuarioEntityToUsuarioDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        return usuarioMapper.usuarioEntitiesToUsuarioDTOs(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosPorRol(UsuarioEntity.RolEnum rol) {
        List<UsuarioEntity> usuarios = usuarioRepository.findByRol(rol);
        return usuarioMapper.usuarioEntitiesToUsuarioDTOs(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        List<UsuarioEntity> usuarios = usuarioRepository.findByActivoTrue();
        return usuarioMapper.usuarioEntitiesToUsuarioDTOs(usuarios);
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }
        if (usuarioDTO.getApellido() != null) {
            usuario.setApellido(usuarioDTO.getApellido());
        }
        if (usuarioDTO.getTelefono() != null) {
            usuario.setTelefono(usuarioDTO.getTelefono());
        }
        if (usuarioDTO.getCarrera() != null) {
            usuario.setCarrera(usuarioDTO.getCarrera());
        }
        if (usuarioDTO.getCiclo() != null) {
            usuario.setCiclo(usuarioDTO.getCiclo());
        }
        if (usuarioDTO.getFotoUrl() != null) {
            usuario.setFotoUrl(usuarioDTO.getFotoUrl());
        }

        UsuarioEntity usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.usuarioEntityToUsuarioDTO(usuarioActualizado);
    }

    @Override
    public void desactivarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existeCodigo(String codigo) {
        return usuarioRepository.existsByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerDatosEstudiantePorCodigo(String codigo) {
        // Aquí podrías integrar con un sistema externo de la universidad
        // Por ahora, solo buscamos en nuestra base de datos
        return obtenerUsuarioPorCodigo(codigo);
    }
}
