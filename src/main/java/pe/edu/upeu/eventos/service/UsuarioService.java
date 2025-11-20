package pe.edu.upeu.eventos.service;

import pe.edu.upeu.eventos.dto.UsuarioDTO;
import pe.edu.upeu.eventos.dto.request.RegistroUsuarioRequest;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO registrarUsuario(RegistroUsuarioRequest request);

    UsuarioDTO obtenerUsuarioPorId(Long id);

    UsuarioDTO obtenerUsuarioPorEmail(String email);

    UsuarioDTO obtenerUsuarioPorCodigo(String codigo);

    List<UsuarioDTO> obtenerTodosLosUsuarios();

    List<UsuarioDTO> obtenerUsuariosPorRol(UsuarioEntity.RolEnum rol);

    List<UsuarioDTO> obtenerUsuariosActivos();

    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);

    void desactivarUsuario(Long id);

    void activarUsuario(Long id);

    void eliminarUsuario(Long id);

    Boolean existeEmail(String email);

    Boolean existeCodigo(String codigo);

    UsuarioDTO obtenerDatosEstudiantePorCodigo(String codigo);
}
