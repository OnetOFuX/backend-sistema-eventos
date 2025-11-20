package pe.edu.upeu.eventos.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.eventos.dto.InscripcionDTO;
import pe.edu.upeu.eventos.dto.request.InscripcionRequest;
import pe.edu.upeu.eventos.entity.EventoEntity;
import pe.edu.upeu.eventos.entity.InscripcionEntity;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.mapper.InscripcionMapper;
import pe.edu.upeu.eventos.repository.EventoRepository;
import pe.edu.upeu.eventos.repository.InscripcionRepository;
import pe.edu.upeu.eventos.repository.UsuarioRepository;
import pe.edu.upeu.eventos.service.EventoService;
import pe.edu.upeu.eventos.service.InscripcionService;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private InscripcionMapper inscripcionMapper;

    @Autowired
    private EventoService eventoService;

    @Override
    public InscripcionDTO crearInscripcion(InscripcionRequest request, Long usuarioId) {
        // Obtener usuario
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener evento
        EventoEntity evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Verificar si ya existe inscripción
        if (inscripcionRepository.existsByUsuarioIdAndEventoId(usuarioId, request.getEventoId())) {
            throw new RuntimeException("El usuario ya está inscrito en este evento");
        }

        // Verificar disponibilidad de cupos
        if (!eventoService.tieneCapacidadDisponible(request.getEventoId())) {
            throw new RuntimeException("No hay cupos disponibles para este evento");
        }

        // Determinar estado inicial
        InscripcionEntity.EstadoInscripcionEnum estado;
        if (!evento.getEsPago()) {
            estado = InscripcionEntity.EstadoInscripcionEnum.CONFIRMADA;
        } else {
            estado = InscripcionEntity.EstadoInscripcionEnum.PENDIENTE;
        }

        // Generar código QR único
        String codigoQr = UUID.randomUUID().toString();

        // Crear entity de inscripción
        InscripcionEntity inscripcion = InscripcionEntity.builder()
                .usuario(usuario)
                .evento(evento)
                .estado(estado)
                .codigoQr(codigoQr)
                .metodoPago(request.getMetodoPago())
                .montoPagado(request.getMontoPagado())
                .comprobanteUrl(request.getComprobanteUrl())
                .observaciones(request.getObservaciones())
                .build();

        // Si el evento es gratuito, marcar método de pago y fecha
        if (!evento.getEsPago()) {
            inscripcion.setMetodoPago(InscripcionEntity.MetodoPagoEnum.GRATUITO);
            inscripcion.setFechaPago(LocalDateTime.now());
        }

        // Guardar en base de datos
        InscripcionEntity inscripcionGuardada = inscripcionRepository.save(inscripcion);

        // Mapear a DTO
        InscripcionDTO dto = inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcionGuardada);

        // Llenar campos que podrían ser null
        dto.setUsuarioId(usuario.getId());
        dto.setUsuarioNombre(usuario.getNombre());
        dto.setUsuarioApellido(usuario.getApellido());
        dto.setUsuarioCodigo(usuario.getCodigo());

        dto.setEventoId(evento.getId());
        dto.setEventoTitulo(evento.getTitulo());

        if (!evento.getEsPago() && dto.getFechaPago() == null) {
            dto.setFechaPago(LocalDateTime.now());
        }

        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        return inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionDTO obtenerInscripcionPorQr(String codigoQr) {
        InscripcionEntity inscripcion = inscripcionRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con código QR"));
        return inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPorUsuario(Long usuarioId) {
        List<InscripcionEntity> inscripciones = inscripcionRepository.findByUsuarioId(usuarioId);
        return inscripcionMapper.inscripcionEntitiesToInscripcionDTOs(inscripciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPorEvento(Long eventoId) {
        List<InscripcionEntity> inscripciones = inscripcionRepository.findByEventoId(eventoId);
        return inscripcionMapper.inscripcionEntitiesToInscripcionDTOs(inscripciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPorEstado(InscripcionEntity.EstadoInscripcionEnum estado) {
        List<InscripcionEntity> inscripciones = inscripcionRepository.findByEstado(estado);
        return inscripcionMapper.inscripcionEntitiesToInscripcionDTOs(inscripciones);
    }

    @Override
    public InscripcionDTO actualizarEstadoInscripcion(Long id, InscripcionEntity.EstadoInscripcionEnum estado) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setEstado(estado);
        InscripcionEntity inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcionActualizada);
    }

    @Override
    public InscripcionDTO verificarPago(Long id, Long verificadorId) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setEstado(InscripcionEntity.EstadoInscripcionEnum.PAGADA);
        inscripcion.setVerificadoPor(verificadorId);
        inscripcion.setFechaVerificacion(LocalDateTime.now());

        InscripcionEntity inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcionActualizada);
    }

    @Override
    public InscripcionDTO subirComprobante(Long id, String comprobanteUrl) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setComprobanteUrl(comprobanteUrl);
        inscripcion.setFechaPago(LocalDateTime.now());

        InscripcionEntity inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.inscripcionEntityToInscripcionDTO(inscripcionActualizada);
    }

    @Override
    public void cancelarInscripcion(Long id) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setEstado(InscripcionEntity.EstadoInscripcionEnum.CANCELADA);
        inscripcionRepository.save(inscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existeInscripcion(Long usuarioId, Long eventoId) {
        return inscripcionRepository.existsByUsuarioIdAndEventoId(usuarioId, eventoId);
    }

    @Override
    public List<InscripcionDTO> inscripcionMasiva(Long eventoId, MultipartFile archivo) {
        // TODO: Implementar lectura de Excel y creación masiva de inscripciones
        // Esta es una implementación básica que deberás expandir
        List<InscripcionDTO> inscripciones = new ArrayList<>();

        // Aquí deberías:
        // 1. Leer el archivo Excel con Apache POI
        // 2. Por cada fila, crear una inscripción
        // 3. Validar los datos
        // 4. Guardar las inscripciones

        return inscripciones;
    }

    @Override
    public byte[] generarQrInscripcion(Long inscripcionId) {
        InscripcionEntity inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    inscripcion.getCodigoQr(),
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar código QR: " + e.getMessage());
        }
    }
}
