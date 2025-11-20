package pe.edu.upeu.eventos.service;

import pe.edu.upeu.eventos.dto.EventoDTO;
import pe.edu.upeu.eventos.dto.request.CrearEventoRequest;
import pe.edu.upeu.eventos.entity.EventoEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {

    EventoDTO crearEvento(CrearEventoRequest request, Long creadorId);

    EventoDTO obtenerEventoPorId(Long id);

    List<EventoDTO> obtenerTodosLosEventos();

    List<EventoDTO> obtenerEventosActivos();

    List<EventoDTO> obtenerEventosPorTipo(EventoEntity.TipoEventoEnum tipo);

    List<EventoDTO> obtenerEventosPorCreador(Long creadorId);

    List<EventoDTO> obtenerEventosProximos();

    List<EventoDTO> obtenerEventosPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    List<EventoDTO> obtenerEventosDePago();

    List<EventoDTO> obtenerEventosGratuitos();

    EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO);

    void desactivarEvento(Long id);

    void activarEvento(Long id);

    void eliminarEvento(Long id);

    Integer obtenerCuposDisponibles(Long eventoId);

    Boolean tieneCapacidadDisponible(Long eventoId);

    List<EventoDTO> obtenerEventosPublicos(Authentication authentication);
}
