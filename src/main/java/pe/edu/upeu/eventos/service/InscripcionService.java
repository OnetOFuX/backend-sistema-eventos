package pe.edu.upeu.eventos.service;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.eventos.dto.InscripcionDTO;
import pe.edu.upeu.eventos.dto.request.InscripcionRequest;
import pe.edu.upeu.eventos.entity.InscripcionEntity;

import java.util.List;

public interface InscripcionService {

    InscripcionDTO crearInscripcion(InscripcionRequest request, Long usuarioId);

    InscripcionDTO obtenerInscripcionPorId(Long id);

    InscripcionDTO obtenerInscripcionPorQr(String codigoQr);

    List<InscripcionDTO> obtenerInscripcionesPorUsuario(Long usuarioId);

    List<InscripcionDTO> obtenerInscripcionesPorEvento(Long eventoId);

    List<InscripcionDTO> obtenerInscripcionesPorEstado(InscripcionEntity.EstadoInscripcionEnum estado);

    InscripcionDTO actualizarEstadoInscripcion(Long id, InscripcionEntity.EstadoInscripcionEnum estado);

    InscripcionDTO verificarPago(Long id, Long verificadorId);

    InscripcionDTO subirComprobante(Long id, String comprobanteUrl);

    void cancelarInscripcion(Long id);

    Boolean existeInscripcion(Long usuarioId, Long eventoId);

    List<InscripcionDTO> inscripcionMasiva(Long eventoId, MultipartFile archivo);

    byte[] generarQrInscripcion(Long inscripcionId);
}
