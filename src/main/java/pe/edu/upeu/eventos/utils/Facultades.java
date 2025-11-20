package pe.edu.upeu.eventos.utils;

import java.util.*;

public final class Facultades {

    private static final Map<String, List<String>> MAP;
    public static final List<String> FACULTADES;

    static {
        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put("Ciencias Empresariales", Arrays.asList(
                "Administración Contabilidad",
                "Gestión Tributaria y Aduanera"
        ));
        map.put("Ciencias Humanas y Educación", Arrays.asList(
                "Educación Inicial y Puericultura",
                "Educación Primaria y Pedagogía Terapéutica",
                "Educación, Especialidad Inglés y Español"
        ));
        map.put("Ciencias de la Salud", Arrays.asList(
                "Enfermería",
                "Nutrición Humana",
                "Psicología"
        ));
        map.put("Ingeniería y Arquitectura", Arrays.asList(
                "Ingeniería de Industria Alimentarias",
                "Ingeniería de Sistemas",
                "Arquitectura",
                "Ingeniería Ambiental",
                "Ingeniería Civil"
        ));
        MAP = Collections.unmodifiableMap(map);
        List<String> facs = new ArrayList<>(MAP.keySet());
        facs.add("TODAS");
        FACULTADES = Collections.unmodifiableList(facs);
    }

    private Facultades() {}

    public static List<String> careersForFaculty(String facultad) {
        if (facultad == null) return Collections.emptyList();
        if ("TODAS".equalsIgnoreCase(facultad)) return Collections.unmodifiableList(new ArrayList<>(Carreras.LIST));
        return MAP.getOrDefault(facultad, Collections.emptyList());
    }

    public static boolean isFaculty(String facultad) {
        if (facultad == null) return false;
        return FACULTADES.stream().anyMatch(f -> f.equalsIgnoreCase(facultad.trim()));
    }

    public static Optional<String> facultyForCareer(String carrera) {
        if (carrera == null) return Optional.empty();
        for (Map.Entry<String, List<String>> e : MAP.entrySet()) {
            for (String c : e.getValue()) {
                if (c.equalsIgnoreCase(carrera.trim())) return Optional.of(e.getKey());
            }
        }
        return Optional.empty();
    }
}
