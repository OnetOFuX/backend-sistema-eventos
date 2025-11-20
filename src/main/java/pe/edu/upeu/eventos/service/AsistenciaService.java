package pe.edu.upeu.eventos.service;

import pe.edu.upeu.eventos.dto.AsistenciaDTO;
import pe.edu.upeu.eventos.dto.request.RegistrarAsistenciaRequest;

import java.util.List;

public interface AsistenciaService {

    AsistenciaDTO registrarAsistencia(RegistrarAsistenciaRequest request, Long registradorId);

    AsistenciaDTO registrarAsistenciaPorQr(String codigoQr, Long fechaEventoId, Long registradorId);

    AsistenciaDTO registrarAsistenciaManual(String codigoEstudiante, Long fechaEventoId, Long registradorId);

    AsistenciaDTO obtenerAsistenciaPorId(Long id);

    List<AsistenciaDTO> obtenerAsistenciasPorUsuario(Long usuarioId);

    List<AsistenciaDTO> obtenerAsistenciasPorFechaEvento(Long fechaEventoId);

    List<AsistenciaDTO> obtenerAsistenciasPorEvento(Long eventoId);

    Boolean existeAsistencia(Long usuarioId, Long fechaEventoId);

    void eliminarAsistencia(Long id);
}
