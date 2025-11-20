package pe.edu.upeu.eventos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.eventos.dto.EventoDTO;
import pe.edu.upeu.eventos.dto.request.CrearEventoRequest;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.entity.EventoEntity;
import pe.edu.upeu.eventos.security.model.UserDetailsImpl;
import pe.edu.upeu.eventos.service.EventoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<EventoDTO>> crear(
            @Valid @RequestBody CrearEventoRequest request,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        EventoDTO evento = eventoService.crearEvento(request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Evento creado exitosamente", evento));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerTodos() {
        List<EventoDTO> eventos = eventoService.obtenerTodosLosEventos();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoDTO>> obtenerPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.obtenerEventoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(evento));
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerActivos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosActivos();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/publicos")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerPublicos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosActivos();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerPorTipo(@PathVariable EventoEntity.TipoEventoEnum tipo) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorTipo(tipo);
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/creador/{creadorId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerPorCreador(@PathVariable Long creadorId) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorCreador(creadorId);
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/proximos")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerProximos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosProximos();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/rango")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorRangoFechas(inicio, fin);
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/pago")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerDePago() {
        List<EventoDTO> eventos = eventoService.obtenerEventosDePago();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @GetMapping("/gratuitos")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> obtenerGratuitos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosGratuitos();
        return ResponseEntity.ok(ApiResponse.success(eventos));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<EventoDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoActualizado = eventoService.actualizarEvento(id, eventoDTO);
        return ResponseEntity.ok(ApiResponse.success("Evento actualizado", eventoActualizado));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable Long id) {
        eventoService.desactivarEvento(id);
        return ResponseEntity.ok(ApiResponse.success("Evento desactivado", null));
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COORDINADOR')")
    public ResponseEntity<ApiResponse<Void>> activar(@PathVariable Long id) {
        eventoService.activarEvento(id);
        return ResponseEntity.ok(ApiResponse.success("Evento activado", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.ok(ApiResponse.success("Evento eliminado", null));
    }

    @GetMapping("/{id}/cupos-disponibles")
    public ResponseEntity<ApiResponse<Integer>> obtenerCuposDisponibles(@PathVariable Long id) {
        Integer cupos = eventoService.obtenerCuposDisponibles(id);
        return ResponseEntity.ok(ApiResponse.success(cupos));
    }
}
