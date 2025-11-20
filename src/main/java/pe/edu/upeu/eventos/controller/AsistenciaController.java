package pe.edu.upeu.eventos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.eventos.dto.AsistenciaDTO;
import pe.edu.upeu.eventos.dto.request.RegistrarAsistenciaRequest;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.security.model.UserDetailsImpl;
import pe.edu.upeu.eventos.service.AsistenciaService;

import java.util.List;

@RestController
@RequestMapping("/asistencias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<AsistenciaDTO>> registrar(
            @Valid @RequestBody RegistrarAsistenciaRequest request,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AsistenciaDTO asistencia = asistenciaService.registrarAsistencia(request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Asistencia registrada exitosamente", asistencia));
    }

    @PostMapping("/qr")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<AsistenciaDTO>> registrarPorQr(
            @RequestParam String codigoQr,
            @RequestParam Long fechaEventoId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AsistenciaDTO asistencia = asistenciaService.registrarAsistenciaPorQr(
                codigoQr, fechaEventoId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Asistencia registrada por QR", asistencia));
    }

    @PostMapping("/manual")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<AsistenciaDTO>> registrarManual(
            @RequestParam String codigoEstudiante,
            @RequestParam Long fechaEventoId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AsistenciaDTO asistencia = asistenciaService.registrarAsistenciaManual(
                codigoEstudiante, fechaEventoId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Asistencia registrada manualmente", asistencia));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<AsistenciaDTO>> obtenerPorId(@PathVariable Long id) {
        AsistenciaDTO asistencia = asistenciaService.obtenerAsistenciaPorId(id);
        return ResponseEntity.ok(ApiResponse.success(asistencia));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<AsistenciaDTO>>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<AsistenciaDTO> asistencias = asistenciaService.obtenerAsistenciasPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(asistencias));
    }

    @GetMapping("/mis-asistencias")
    public ResponseEntity<ApiResponse<List<AsistenciaDTO>>> obtenerMisAsistencias(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<AsistenciaDTO> asistencias = asistenciaService.obtenerAsistenciasPorUsuario(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(asistencias));
    }

    @GetMapping("/fecha-evento/{fechaEventoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<AsistenciaDTO>>> obtenerPorFechaEvento(@PathVariable Long fechaEventoId) {
        List<AsistenciaDTO> asistencias = asistenciaService.obtenerAsistenciasPorFechaEvento(fechaEventoId);
        return ResponseEntity.ok(ApiResponse.success(asistencias));
    }

    @GetMapping("/evento/{eventoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<AsistenciaDTO>>> obtenerPorEvento(@PathVariable Long eventoId) {
        List<AsistenciaDTO> asistencias = asistenciaService.obtenerAsistenciasPorEvento(eventoId);
        return ResponseEntity.ok(ApiResponse.success(asistencias));
    }

    @GetMapping("/existe")
    public ResponseEntity<ApiResponse<Boolean>> existeAsistencia(
            @RequestParam Long usuarioId,
            @RequestParam Long fechaEventoId) {
        Boolean existe = asistenciaService.existeAsistencia(usuarioId, fechaEventoId);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        asistenciaService.eliminarAsistencia(id);
        return ResponseEntity.ok(ApiResponse.success("Asistencia eliminada", null));
    }
}
