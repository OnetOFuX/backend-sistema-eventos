package pe.edu.upeu.eventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.eventos.dto.UsuarioDTO;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorCodigo(@PathVariable String codigo) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorCodigo(codigo);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerPorRol(@PathVariable UsuarioEntity.RolEnum rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerActivos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado", usuarioActualizado));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario desactivado", null));
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> activar(@PathVariable Long id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario activado", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado", null));
    }

    @GetMapping("/existe/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> existeEmail(@PathVariable String email) {
        Boolean existe = usuarioService.existeEmail(email);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }

    @GetMapping("/existe/codigo/{codigo}")
    public ResponseEntity<ApiResponse<Boolean>> existeCodigo(@PathVariable String codigo) {
        Boolean existe = usuarioService.existeCodigo(codigo);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }
}
