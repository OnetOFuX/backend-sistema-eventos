package pe.edu.upeu.eventos.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Carreras {

    private Carreras() {}

    public static final List<String> LIST = Collections.unmodifiableList(Arrays.asList(
            "Administración Contabilidad",
            "Gestión Tributaria y Aduanera",
            "Educación Inicial y Puericultura",
            "Enfermería",
            "Educación Primaria y Pedagogía Terapéutica",
            "Educación, Especialidad Inglés y Español",
            "Nutrición Humana",
            "Psicología",
            "Ingeniería de Industria Alimentarias",
            "Ingeniería de Sistemas",
            "Arquitectura",
            "Ingeniería Ambiental",
            "Ingeniería Civil",
            "TODAS"
    ));

    public static boolean isValid(String carrera) {
        if (carrera == null) return false;
        return LIST.stream().anyMatch(s -> s.equalsIgnoreCase(carrera.trim()));
    }
}

