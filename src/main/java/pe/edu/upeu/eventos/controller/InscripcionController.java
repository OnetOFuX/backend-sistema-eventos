package pe.edu.upeu.eventos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.eventos.dto.InscripcionDTO;
import pe.edu.upeu.eventos.dto.request.InscripcionRequest;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.entity.InscripcionEntity;
import pe.edu.upeu.eventos.security.model.UserDetailsImpl;
import pe.edu.upeu.eventos.service.InscripcionService;

import java.util.List;

@RestController
@RequestMapping("/inscripciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<ApiResponse<InscripcionDTO>> crear(
            @Valid @RequestBody InscripcionRequest request,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        InscripcionDTO inscripcion = inscripcionService.crearInscripcion(request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Inscripción creada exitosamente", inscripcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InscripcionDTO>> obtenerPorId(@PathVariable Long id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        return ResponseEntity.ok(ApiResponse.success(inscripcion));
    }

    @GetMapping("/qr/{codigoQr}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<InscripcionDTO>> obtenerPorQr(@PathVariable String codigoQr) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorQr(codigoQr);
        return ResponseEntity.ok(ApiResponse.success(inscripcion));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(inscripciones));
    }

    @GetMapping("/mis-inscripciones")
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> obtenerMisInscripciones(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorUsuario(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(inscripciones));
    }

    @GetMapping("/evento/{eventoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> obtenerPorEvento(@PathVariable Long eventoId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEvento(eventoId);
        return ResponseEntity.ok(ApiResponse.success(inscripciones));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> obtenerPorEstado(
            @PathVariable InscripcionEntity.EstadoInscripcionEnum estado) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstado(estado);
        return ResponseEntity.ok(ApiResponse.success(inscripciones));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<InscripcionDTO>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam InscripcionEntity.EstadoInscripcionEnum estado) {
        InscripcionDTO inscripcion = inscripcionService.actualizarEstadoInscripcion(id, estado);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado", inscripcion));
    }

    @PatchMapping("/{id}/verificar-pago")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<InscripcionDTO>> verificarPago(
            @PathVariable Long id,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        InscripcionDTO inscripcion = inscripcionService.verificarPago(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Pago verificado", inscripcion));
    }

    @PatchMapping("/{id}/comprobante")
    public ResponseEntity<ApiResponse<InscripcionDTO>> subirComprobante(
            @PathVariable Long id,
            @RequestParam String comprobanteUrl) {
        InscripcionDTO inscripcion = inscripcionService.subirComprobante(id, comprobanteUrl);
        return ResponseEntity.ok(ApiResponse.success("Comprobante subido", inscripcion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelar(@PathVariable Long id) {
        inscripcionService.cancelarInscripcion(id);
        return ResponseEntity.ok(ApiResponse.success("Inscripción cancelada", null));
    }

    @GetMapping("/existe")
    public ResponseEntity<ApiResponse<Boolean>> existeInscripcion(
            @RequestParam Long usuarioId,
            @RequestParam Long eventoId) {
        Boolean existe = inscripcionService.existeInscripcion(usuarioId, eventoId);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }

    @PostMapping("/masiva/{eventoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> inscripcionMasiva(
            @PathVariable Long eventoId,
            @RequestParam("archivo") MultipartFile archivo) {
        List<InscripcionDTO> inscripciones = inscripcionService.inscripcionMasiva(eventoId, archivo);
        return ResponseEntity.ok(ApiResponse.success("Inscripciones masivas creadas", inscripciones));
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> generarQr(@PathVariable Long id) {
        byte[] qrCode = inscripcionService.generarQrInscripcion(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCode.length);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCode);
    }
}
