package pe.edu.upeu.eventos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.eventos.dto.EventoDTO;
import pe.edu.upeu.eventos.dto.request.CrearEventoRequest;
import pe.edu.upeu.eventos.entity.CarreraEntity;
import pe.edu.upeu.eventos.entity.EventoEntity;
import pe.edu.upeu.eventos.entity.FacultadEntity;
import pe.edu.upeu.eventos.entity.FechaEventoEntity;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.mapper.EventoMapper;
import pe.edu.upeu.eventos.repository.CarreraRepository;
import pe.edu.upeu.eventos.repository.EventoRepository;
import pe.edu.upeu.eventos.repository.FacultadRepository;
import pe.edu.upeu.eventos.repository.UsuarioRepository;
import pe.edu.upeu.eventos.service.EventoService;
import pe.edu.upeu.eventos.security.model.UserDetailsImpl;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private EventoMapper eventoMapper;

    @Override
    public EventoDTO crearEvento(CrearEventoRequest request, Long creadorId) {
        UsuarioEntity creador = usuarioRepository.findById(creadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CarreraEntity carrera = null;
        FacultadEntity facultad = null;
        Boolean paraTodas = request.getParaTodas() != null ? request.getParaTodas() : false;

        // Validar y buscar carrera si se proporcionó
        if (request.getCarreraId() != null) {
            carrera = carreraRepository.findById(request.getCarreraId())
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + request.getCarreraId()));
            if (!carrera.getActivo()) {
                throw new RuntimeException("La carrera seleccionada no está activa");
            }
        }

        // Validar y buscar facultad si se proporcionó
        if (request.getFacultadId() != null) {
            facultad = facultadRepository.findById(request.getFacultadId())
                    .orElseThrow(() -> new RuntimeException("Facultad no encontrada con ID: " + request.getFacultadId()));
            if (!facultad.getActivo()) {
                throw new RuntimeException("La facultad seleccionada no está activa");
            }
        }

        // Verificar consistencia: si se proporcionan ambas, la carrera debe pertenecer a la facultad
        if (carrera != null && facultad != null) {
            if (!carrera.getFacultad().getId().equals(facultad.getId())) {
                throw new RuntimeException("La carrera seleccionada no pertenece a la facultad indicada");
            }
        }

        EventoEntity evento = EventoEntity.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .ubicacion(request.getUbicacion())
                .cupoMaximo(request.getCupoMaximo())
                .imagenUrl(request.getImagenUrl())
                .activo(true)
                .esPago(request.getEsPago())
                .precio(request.getPrecio())
                .metodosPago(request.getMetodosPago())
                .requiereComprobante(request.getRequiereComprobante())
                .creador(creador)
                .inscripciones(new HashSet<>())
                .carrera(carrera)
                .facultad(facultad)
                .paraTodas(paraTodas)
                .build();

        Set<FechaEventoEntity> fechas = new HashSet<>();
        for (CrearEventoRequest.FechaEventoRequest fechaRequest : request.getFechas()) {
            FechaEventoEntity fecha = FechaEventoEntity.builder()
                    .evento(evento)
                    .fechaInicio(fechaRequest.getFechaInicio())
                    .fechaFin(fechaRequest.getFechaFin())
                    .observaciones(fechaRequest.getObservaciones())
                    .asistencias(new HashSet<>())
                    .build();
            fechas.add(fecha);
        }
        evento.setFechas(fechas);

        EventoEntity eventoGuardado = eventoRepository.save(evento);

        EventoDTO dto = eventoMapper.eventoEntityToEventoDTO(eventoGuardado);

        // Llenar campos adicionales para que no haya null
        dto.setCreadorId(creador.getId());
        dto.setCreadorNombre(creador.getNombre());
        if(dto.getTotalInscritos() == null) dto.setTotalInscritos(0);
        if(dto.getCuposDisponibles() == null) dto.setCuposDisponibles(dto.getCupoMaximo());
        if(dto.getFechas() != null) {
            dto.getFechas().forEach(f -> {
                if(f.getEventoId() == null) f.setEventoId(dto.getId());
                if(f.getTotalAsistencias() == null) f.setTotalAsistencias(0);
            });
        }

        return dto;
    }



    @Override
    @Transactional(readOnly = true)
    public EventoDTO obtenerEventoPorId(Long id) {
        EventoEntity evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        return eventoMapper.eventoEntityToEventoDTO(evento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerTodosLosEventos() {
        List<EventoEntity> eventos = eventoRepository.findAll();
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosActivos() {
        List<EventoEntity> eventos = eventoRepository.findByActivoTrue();
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorTipo(EventoEntity.TipoEventoEnum tipo) {
        List<EventoEntity> eventos = eventoRepository.findByTipoAndActivoTrue(tipo);
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorCreador(Long creadorId) {
        List<EventoEntity> eventos = eventoRepository.findByCreadorId(creadorId);
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosProximos() {
        List<EventoEntity> eventos = eventoRepository.findEventosProximos(LocalDateTime.now());
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        List<EventoEntity> eventos = eventoRepository.findEventosByRangoFechas(inicio, fin);
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosDePago() {
        List<EventoEntity> eventos = eventoRepository.findByEsPagoAndActivoTrue(true);
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosGratuitos() {
        List<EventoEntity> eventos = eventoRepository.findByEsPagoAndActivoTrue(false);
        return eventoMapper.eventoEntitiesToEventoDTOs(eventos);
    }

    @Override
    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO) {
        EventoEntity evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        if (eventoDTO.getTitulo() != null) {
            evento.setTitulo(eventoDTO.getTitulo());
        }
        if (eventoDTO.getDescripcion() != null) {
            evento.setDescripcion(eventoDTO.getDescripcion());
        }
        if (eventoDTO.getTipo() != null) {
            evento.setTipo(eventoDTO.getTipo());
        }
        if (eventoDTO.getUbicacion() != null) {
            evento.setUbicacion(eventoDTO.getUbicacion());
        }
        if (eventoDTO.getCupoMaximo() != null) {
            evento.setCupoMaximo(eventoDTO.getCupoMaximo());
        }
        if (eventoDTO.getImagenUrl() != null) {
            evento.setImagenUrl(eventoDTO.getImagenUrl());
        }
        if (eventoDTO.getPrecio() != null) {
            evento.setPrecio(eventoDTO.getPrecio());
        }
        if (eventoDTO.getMetodosPago() != null) {
            evento.setMetodosPago(eventoDTO.getMetodosPago());
        }
        if (eventoDTO.getCarreraId() != null) {
            CarreraEntity carrera = carreraRepository.findById(eventoDTO.getCarreraId())
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + eventoDTO.getCarreraId()));
            if (!carrera.getActivo()) {
                throw new RuntimeException("La carrera seleccionada no está activa");
            }
            evento.setCarrera(carrera);
        }
        if (eventoDTO.getFacultadId() != null) {
            FacultadEntity facultad = facultadRepository.findById(eventoDTO.getFacultadId())
                    .orElseThrow(() -> new RuntimeException("Facultad no encontrada con ID: " + eventoDTO.getFacultadId()));
            if (!facultad.getActivo()) {
                throw new RuntimeException("La facultad seleccionada no está activa");
            }
            evento.setFacultad(facultad);
        }
        if (eventoDTO.getParaTodas() != null) {
            evento.setParaTodas(eventoDTO.getParaTodas());
        }

        EventoEntity eventoActualizado = eventoRepository.save(evento);
        return eventoMapper.eventoEntityToEventoDTO(eventoActualizado);
    }

    @Override
    public void desactivarEvento(Long id) {
        EventoEntity evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        evento.setActivo(false);
        eventoRepository.save(evento);
    }

    @Override
    public void activarEvento(Long id) {
        EventoEntity evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        evento.setActivo(true);
        eventoRepository.save(evento);
    }

    @Override
    public void eliminarEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        eventoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerCuposDisponibles(Long eventoId) {
        EventoEntity evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (evento.getCupoMaximo() == null) {
            return null; // Sin límite
        }

        Integer inscritos = eventoRepository.countInscripcionesByEventoId(eventoId);
        return evento.getCupoMaximo() - inscritos;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean tieneCapacidadDisponible(Long eventoId) {
        Integer cuposDisponibles = obtenerCuposDisponibles(eventoId);
        return cuposDisponibles == null || cuposDisponibles > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPublicos(Authentication authentication) {
        List<EventoEntity> activos = eventoRepository.findByActivoTrue();

        // Si no hay autenticación, mostrar solo eventos para TODAS
        if (authentication == null || !authentication.isAuthenticated()) {
            List<EventoEntity> filtrados = activos.stream()
                    .filter(e -> e.getParaTodas() != null && e.getParaTodas())
                    .collect(Collectors.toList());
            return eventoMapper.eventoEntitiesToEventoDTOs(filtrados);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            // Si no es nuestro UserDetails, mostrar solo para TODAS
            List<EventoEntity> filtrados = activos.stream()
                    .filter(e -> e.getParaTodas() != null && e.getParaTodas())
                    .collect(Collectors.toList());
            return eventoMapper.eventoEntitiesToEventoDTOs(filtrados);
        }

        UserDetailsImpl user = (UserDetailsImpl) principal;
        // Si es admin, retornar todos los activos
        if (user.getRol() == UsuarioEntity.RolEnum.ADMINISTRADOR) {
            return eventoMapper.eventoEntitiesToEventoDTOs(activos);
        }

        Long carreraUsuarioId = user.getCarreraId();
        Long facultadUsuarioId = user.getFacultadId();

        List<EventoEntity> filtrados = activos.stream()
                .filter(e -> {
                    // visible si evento es para todas
                    if (e.getParaTodas() != null && e.getParaTodas()) return true;

                    // visible si coincide la carrera
                    if (e.getCarrera() != null && carreraUsuarioId != null && e.getCarrera().getId().equals(carreraUsuarioId)) return true;

                    // visible si coincide la facultad
                    if (e.getFacultad() != null && facultadUsuarioId != null && e.getFacultad().getId().equals(facultadUsuarioId)) return true;

                    return false;
                })
                .collect(Collectors.toList());

        return eventoMapper.eventoEntitiesToEventoDTOs(filtrados);
    }
}
