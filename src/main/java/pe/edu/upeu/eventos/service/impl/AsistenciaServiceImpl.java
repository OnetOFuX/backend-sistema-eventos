package pe.edu.upeu.eventos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.eventos.dto.AsistenciaDTO;
import pe.edu.upeu.eventos.dto.request.RegistrarAsistenciaRequest;
import pe.edu.upeu.eventos.entity.AsistenciaEntity;
import pe.edu.upeu.eventos.entity.FechaEventoEntity;
import pe.edu.upeu.eventos.entity.InscripcionEntity;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.mapper.AsistenciaMapper;
import pe.edu.upeu.eventos.repository.AsistenciaRepository;
import pe.edu.upeu.eventos.repository.FechaEventoRepository;
import pe.edu.upeu.eventos.repository.InscripcionRepository;
import pe.edu.upeu.eventos.repository.UsuarioRepository;
import pe.edu.upeu.eventos.service.AsistenciaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FechaEventoRepository fechaEventoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private AsistenciaMapper asistenciaMapper;

    @Override
    public AsistenciaDTO registrarAsistencia(RegistrarAsistenciaRequest request, Long registradorId) {
        if (request.getTipoRegistro() == AsistenciaEntity.TipoRegistroEnum.QR) {
            return registrarAsistenciaPorQr(request.getCodigoQr(), request.getFechaEventoId(), registradorId);
        } else {
            return registrarAsistenciaManual(request.getCodigoEstudiante(), request.getFechaEventoId(), registradorId);
        }
    }

    @Override
    public AsistenciaDTO registrarAsistenciaPorQr(String codigoQr, Long fechaEventoId, Long registradorId) {
        InscripcionEntity inscripcion = inscripcionRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Código QR no válido"));

        FechaEventoEntity fechaEvento = fechaEventoRepository.findById(fechaEventoId)
                .orElseThrow(() -> new RuntimeException("Fecha de evento no encontrada"));

        // Verificar que la inscripción corresponda al evento
        if (!inscripcion.getEvento().getId().equals(fechaEvento.getEvento().getId())) {
            throw new RuntimeException("El código QR no corresponde a este evento");
        }

        // Verificar que la inscripción esté confirmada o pagada
        if (inscripcion.getEstado() != InscripcionEntity.EstadoInscripcionEnum.CONFIRMADA &&
            inscripcion.getEstado() != InscripcionEntity.EstadoInscripcionEnum.PAGADA) {
            throw new RuntimeException("La inscripción no está confirmada");
        }

        // Verificar si ya existe asistencia
        if (asistenciaRepository.existsByUsuarioIdAndFechaEventoId(
                inscripcion.getUsuario().getId(), fechaEventoId)) {
            throw new RuntimeException("La asistencia ya fue registrada");
        }

        UsuarioEntity registrador = usuarioRepository.findById(registradorId)
                .orElseThrow(() -> new RuntimeException("Registrador no encontrado"));

        AsistenciaEntity asistencia = AsistenciaEntity.builder()
                .usuario(inscripcion.getUsuario())
                .fechaEvento(fechaEvento)
                .fechaRegistro(LocalDateTime.now())
                .tipoRegistro(AsistenciaEntity.TipoRegistroEnum.QR)
                .registradoPor(registrador)
                .build();

        AsistenciaEntity asistenciaGuardada = asistenciaRepository.save(asistencia);
        return asistenciaMapper.asistenciaEntityToAsistenciaDTO(asistenciaGuardada);
    }

    @Override
    public AsistenciaDTO registrarAsistenciaManual(String codigoEstudiante, Long fechaEventoId, Long registradorId) {
        UsuarioEntity usuario = usuarioRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        FechaEventoEntity fechaEvento = fechaEventoRepository.findById(fechaEventoId)
                .orElseThrow(() -> new RuntimeException("Fecha de evento no encontrada"));

        // Verificar si ya existe asistencia
        if (asistenciaRepository.existsByUsuarioIdAndFechaEventoId(usuario.getId(), fechaEventoId)) {
            throw new RuntimeException("La asistencia ya fue registrada");
        }

        UsuarioEntity registrador = usuarioRepository.findById(registradorId)
                .orElseThrow(() -> new RuntimeException("Registrador no encontrado"));

        AsistenciaEntity asistencia = AsistenciaEntity.builder()
                .usuario(usuario)
                .fechaEvento(fechaEvento)
                .fechaRegistro(LocalDateTime.now())
                .tipoRegistro(AsistenciaEntity.TipoRegistroEnum.MANUAL)
                .registradoPor(registrador)
                .build();

        AsistenciaEntity asistenciaGuardada = asistenciaRepository.save(asistencia);
        return asistenciaMapper.asistenciaEntityToAsistenciaDTO(asistenciaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public AsistenciaDTO obtenerAsistenciaPorId(Long id) {
        AsistenciaEntity asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
        return asistenciaMapper.asistenciaEntityToAsistenciaDTO(asistencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaDTO> obtenerAsistenciasPorUsuario(Long usuarioId) {
        List<AsistenciaEntity> asistencias = asistenciaRepository.findByUsuarioId(usuarioId);
        return asistenciaMapper.asistenciaEntitiesToAsistenciaDTOs(asistencias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaDTO> obtenerAsistenciasPorFechaEvento(Long fechaEventoId) {
        List<AsistenciaEntity> asistencias = asistenciaRepository.findByFechaEventoId(fechaEventoId);
        return asistenciaMapper.asistenciaEntitiesToAsistenciaDTOs(asistencias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaDTO> obtenerAsistenciasPorEvento(Long eventoId) {
        List<AsistenciaEntity> asistencias = asistenciaRepository.findByEventoId(eventoId);
        return asistenciaMapper.asistenciaEntitiesToAsistenciaDTOs(asistencias);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existeAsistencia(Long usuarioId, Long fechaEventoId) {
        return asistenciaRepository.existsByUsuarioIdAndFechaEventoId(usuarioId, fechaEventoId);
    }

    @Override
    public void eliminarAsistencia(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new RuntimeException("Asistencia no encontrada");
        }
        asistenciaRepository.deleteById(id);
    }
}
