package pe.edu.upeu.eventos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.upeu.eventos.entity.CarreraEntity;
import pe.edu.upeu.eventos.entity.FacultadEntity;
import pe.edu.upeu.eventos.repository.CarreraRepository;
import pe.edu.upeu.eventos.repository.FacultadRepository;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class FacultadesCarrerasInitializer implements CommandLineRunner {

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Override
    public void run(String... args) throws Exception {
        if (facultadRepository.count() == 0) {
            inicializarFacultadesYCarreras();
        }
    }

    private void inicializarFacultadesYCarreras() {
        // Crear Facultades
        FacultadEntity fce = crearFacultad("Ciencias Empresariales", "FCE", "Facultad de Ciencias Empresariales");
        FacultadEntity fche = crearFacultad("Ciencias Humanas y Educación", "FCHE", "Facultad de Ciencias Humanas y Educación");
        FacultadEntity fcs = crearFacultad("Ciencias de la Salud", "FCS", "Facultad de Ciencias de la Salud");
        FacultadEntity fia = crearFacultad("Ingeniería y Arquitectura", "FIA", "Facultad de Ingeniería y Arquitectura");

        // Crear Carreras de Ciencias Empresariales
        crearCarrera("Administración Contabilidad", "ADMIN-CONT", "Carrera de Administración y Contabilidad", fce);
        crearCarrera("Gestión Tributaria y Aduanera", "GEST-TRIB", "Carrera de Gestión Tributaria y Aduanera", fce);

        // Crear Carreras de Ciencias Humanas y Educación
        crearCarrera("Educación Inicial y Puericultura", "EDU-INIC", "Carrera de Educación Inicial y Puericultura", fche);
        crearCarrera("Educación Primaria y Pedagogía Terapéutica", "EDU-PRIM", "Carrera de Educación Primaria y Pedagogía Terapéutica", fche);
        crearCarrera("Educación, Especialidad Inglés y Español", "EDU-LING", "Carrera de Educación, Especialidad Inglés y Español", fche);

        // Crear Carreras de Ciencias de la Salud
        crearCarrera("Enfermería", "ENFERM", "Carrera de Enfermería", fcs);
        crearCarrera("Nutrición Humana", "NUTRIC", "Carrera de Nutrición Humana", fcs);
        crearCarrera("Psicología", "PSICOL", "Carrera de Psicología", fcs);

        // Crear Carreras de Ingeniería y Arquitectura
        crearCarrera("Ingeniería de Industria Alimentarias", "ING-ALIM", "Carrera de Ingeniería de Industria Alimentarias", fia);
        crearCarrera("Ingeniería de Sistemas", "ING-SIST", "Carrera de Ingeniería de Sistemas", fia);
        crearCarrera("Arquitectura", "ARQUIT", "Carrera de Arquitectura", fia);
        crearCarrera("Ingeniería Ambiental", "ING-AMB", "Carrera de Ingeniería Ambiental", fia);
        crearCarrera("Ingeniería Civil", "ING-CIV", "Carrera de Ingeniería Civil", fia);

        System.out.println("✓ Facultades y Carreras inicializadas correctamente");
    }

    private FacultadEntity crearFacultad(String nombre, String codigo, String descripcion) {
        FacultadEntity facultad = FacultadEntity.builder()
                .nombre(nombre)
                .codigo(codigo)
                .descripcion(descripcion)
                .activo(true)
                .build();
        return facultadRepository.save(facultad);
    }

    private CarreraEntity crearCarrera(String nombre, String codigo, String descripcion, FacultadEntity facultad) {
        CarreraEntity carrera = CarreraEntity.builder()
                .nombre(nombre)
                .codigo(codigo)
                .descripcion(descripcion)
                .activo(true)
                .facultad(facultad)
                .build();
        return carreraRepository.save(carrera);
    }
}

