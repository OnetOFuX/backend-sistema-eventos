package pe.edu.upeu.eventos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.eventos.dto.response.ApiResponse;
import pe.edu.upeu.eventos.utils.Carreras;
import pe.edu.upeu.eventos.utils.Facultades;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @GetMapping("/carreras")
    public ResponseEntity<ApiResponse<List<String>>> obtenerCarreras() {
        return ResponseEntity.ok(ApiResponse.success(Carreras.LIST));
    }

    @GetMapping("/facultades")
    public ResponseEntity<ApiResponse<List<String>>> obtenerFacultades() {
        return ResponseEntity.ok(ApiResponse.success(Facultades.FACULTADES));
    }

    @GetMapping("/facultades/{facultad}/carreras")
    public ResponseEntity<ApiResponse<List<String>>> obtenerCarrerasPorFacultad(@PathVariable String facultad) {
        return ResponseEntity.ok(ApiResponse.success(Facultades.careersForFaculty(facultad)));
    }
}
