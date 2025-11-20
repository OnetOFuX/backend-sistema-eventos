package pe.edu.upeu.eventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.eventos.dto.CarreraDTO;
import pe.edu.upeu.eventos.dto.FacultadDTO;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.entity.CarreraEntity;
import pe.edu.upeu.eventos.entity.FacultadEntity;
import pe.edu.upeu.eventos.repository.CarreraRepository;
import pe.edu.upeu.eventos.repository.FacultadRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private FacultadRepository facultadRepository;

    @GetMapping("/carreras")
    public ResponseEntity<ApiResponse<List<CarreraDTO>>> obtenerCarreras() {
        List<CarreraDTO> carreras = carreraRepository.findByActivoTrue().stream()
                .map(this::convertirCarreraADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(carreras));
    }

    @GetMapping("/facultades")
    public ResponseEntity<ApiResponse<List<FacultadDTO>>> obtenerFacultades() {
        List<FacultadDTO> facultades = facultadRepository.findByActivoTrue().stream()
                .map(this::convertirFacultadADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(facultades));
    }

    @GetMapping("/facultades/{facultadId}/carreras")
    public ResponseEntity<ApiResponse<List<CarreraDTO>>> obtenerCarrerasPorFacultad(@PathVariable Long facultadId) {
        List<CarreraDTO> carreras = carreraRepository.findByFacultadIdAndActivoTrue(facultadId).stream()
                .map(this::convertirCarreraADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(carreras));
    }

    private FacultadDTO convertirFacultadADTO(FacultadEntity entity) {
        return FacultadDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .codigo(entity.getCodigo())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CarreraDTO convertirCarreraADTO(CarreraEntity entity) {
        return CarreraDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .codigo(entity.getCodigo())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .facultadId(entity.getFacultad().getId())
                .facultadNombre(entity.getFacultad().getNombre())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
