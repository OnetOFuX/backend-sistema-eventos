# Ejemplos de JSON actualizados

## 1. REGISTRO DE USUARIO

### Endpoint: POST /auth/registro

```json
{
  "codigo": "2024001",
  "email": "juan.perez@upeu.edu.pe",
  "password": "secreto123456",
  "nombre": "Juan",
  "apellido": "Pérez García",
  "telefono": "987654321",
  "carreraId": 10,
  "ciclo": "VIII"
}
```

**Nota:** Para obtener el `carreraId` correcto:
1. Llamar a `GET /catalog/facultades` para ver las facultades
2. Llamar a `GET /catalog/facultades/{facultadId}/carreras` para ver las carreras
3. Usar el `id` de la carrera deseada

### Ejemplo con rol específico (para crear admin):
```json
{
  "codigo": "ADMIN001",
  "email": "admin@upeu.edu.pe",
  "password": "admin123456",
  "nombre": "María",
  "apellido": "González",
  "telefono": "999888777",
  "carreraId": 10,
  "ciclo": "X",
  "rol": "ADMINISTRADOR"
}
```

---

## 2. CREACIÓN DE EVENTOS

### Endpoint: POST /eventos
### Headers: Authorization: Bearer {token}

### Ejemplo 1: Evento para UNA CARRERA específica
```json
{
  "titulo": "Taller de Desarrollo Web con Spring Boot",
  "descripcion": "Aprende a crear APIs REST con Spring Boot y JPA",
  "tipo": "TALLER",
  "ubicacion": "Laboratorio de Cómputo 3",
  "cupoMaximo": 30,
  "imagenUrl": "https://example.com/imagen.jpg",
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "requiereComprobante": false,
  "fechas": [
    {
      "fechaInicio": "2026-01-15T14:00:00",
      "fechaFin": "2026-01-15T17:00:00",
      "observaciones": "Traer laptop"
    }
  ],
  "carreraId": 10,
  "paraTodas": false
}
```

### Ejemplo 2: Evento para UNA FACULTAD completa
```json
{
  "titulo": "Feria de Salud y Bienestar",
  "descripcion": "Evento para todas las carreras de Ciencias de la Salud",
  "tipo": "SOCIAL",
  "ubicacion": "Patio Central",
  "cupoMaximo": 200,
  "imagenUrl": "https://example.com/feria-salud.jpg",
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "requiereComprobante": false,
  "fechas": [
    {
      "fechaInicio": "2026-02-20T09:00:00",
      "fechaFin": "2026-02-20T16:00:00",
      "observaciones": "Actividades durante todo el día"
    }
  ],
  "facultadId": 3,
  "paraTodas": false
}
```

### Ejemplo 3: Evento PARA TODOS (sin restricción)
```json
{
  "titulo": "Conferencia: El Futuro de la Educación Superior",
  "descripcion": "Evento abierto a toda la comunidad universitaria",
  "tipo": "CONFERENCIA",
  "ubicacion": "Auditorio Principal",
  "cupoMaximo": 500,
  "imagenUrl": "https://example.com/conferencia.jpg",
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "requiereComprobante": false,
  "fechas": [
    {
      "fechaInicio": "2026-03-10T18:00:00",
      "fechaFin": "2026-03-10T20:00:00",
      "observaciones": "Entrada libre"
    }
  ],
  "paraTodas": true
}
```

### Ejemplo 4: Evento de PAGO con múltiples fechas
```json
{
  "titulo": "Curso de Certificación en Nutrición Deportiva",
  "descripcion": "Curso intensivo de 3 sesiones con certificación",
  "tipo": "SEMINARIO",
  "ubicacion": "Aula 205",
  "cupoMaximo": 25,
  "imagenUrl": "https://example.com/nutricion.jpg",
  "esPago": true,
  "precio": 150.00,
  "metodosPago": ["EFECTIVO", "YAPE", "PLIN", "TRANSFERENCIA"],
  "requiereComprobante": true,
  "fechas": [
    {
      "fechaInicio": "2026-04-05T15:00:00",
      "fechaFin": "2026-04-05T18:00:00",
      "observaciones": "Sesión 1: Fundamentos"
    },
    {
      "fechaInicio": "2026-04-12T15:00:00",
      "fechaFin": "2026-04-12T18:00:00",
      "observaciones": "Sesión 2: Aplicaciones Prácticas"
    },
    {
      "fechaInicio": "2026-04-19T15:00:00",
      "fechaFin": "2026-04-19T18:00:00",
      "observaciones": "Sesión 3: Certificación"
    }
  ],
  "carreraId": 7,
  "paraTodas": false
}
```

---

## 3. MAPEO DE IDs (Referencia)

### IDs de Facultades (aproximados según el orden de creación):
- 1: Ciencias Empresariales
- 2: Ciencias Humanas y Educación
- 3: Ciencias de la Salud
- 4: Ingeniería y Arquitectura

### IDs de Carreras (aproximados):
1. Administración Contabilidad (Facultad 1)
2. Gestión Tributaria y Aduanera (Facultad 1)
3. Educación Inicial y Puericultura (Facultad 2)
4. Educación Primaria y Pedagogía Terapéutica (Facultad 2)
5. Educación, Especialidad Inglés y Español (Facultad 2)
6. Enfermería (Facultad 3)
7. Nutrición Humana (Facultad 3)
8. Psicología (Facultad 3)
9. Ingeniería de Industria Alimentarias (Facultad 4)
10. Ingeniería de Sistemas (Facultad 4)
11. Arquitectura (Facultad 4)
12. Ingeniería Ambiental (Facultad 4)
13. Ingeniería Civil (Facultad 4)

**Importante:** Estos IDs son aproximados. Usa los endpoints para obtener los IDs reales:
- `GET /catalog/facultades`
- `GET /catalog/carreras`

---

## 4. FLUJO COMPLETO EN POSTMAN

### Paso 1: Obtener lista de facultades
```
GET http://localhost:8080/catalog/facultades
```

### Paso 2: Obtener carreras de una facultad específica
```
GET http://localhost:8080/catalog/facultades/4/carreras
```
(Ejemplo: Facultad 4 = Ingeniería y Arquitectura)

### Paso 3: Registrar usuario con una carrera
```
POST http://localhost:8080/auth/registro
Content-Type: application/json

{
  "codigo": "2024001",
  "email": "estudiante@upeu.edu.pe",
  "password": "password123",
  "nombre": "Carlos",
  "apellido": "Martínez",
  "telefono": "987654321",
  "carreraId": 10,
  "ciclo": "VII"
}
```

### Paso 4: Login
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "emailOrCodigo": "estudiante@upeu.edu.pe",
  "password": "password123"
}
```

Copiar el `token` de la respuesta.

### Paso 5: Crear evento (como admin)
```
POST http://localhost:8080/eventos
Authorization: Bearer {token_del_admin}
Content-Type: application/json

{
  "titulo": "Evento de Prueba",
  "descripcion": "Descripción del evento",
  "tipo": "TALLER",
  "ubicacion": "Aula 101",
  "cupoMaximo": 50,
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "fechas": [
    {
      "fechaInicio": "2026-05-10T10:00:00",
      "fechaFin": "2026-05-10T12:00:00"
    }
  ],
  "carreraId": 10,
  "paraTodas": false
}
```

### Paso 6: Ver eventos públicos (como estudiante)
```
GET http://localhost:8080/eventos/publicos
Authorization: Bearer {token_del_estudiante}
```

El estudiante solo verá eventos que:
- Sean `paraTodas: true`, o
- Sean de su carrera (carreraId: 10), o
- Sean de su facultad (facultadId: 4)

---

## 5. NOTAS IMPORTANTES

1. **carreraId es requerido** al registrar un usuario (si no es admin sin carrera).
2. **Al crear eventos**, debes especificar:
   - `paraTodas: true` para eventos generales, O
   - `carreraId` para eventos de una carrera, O
   - `facultadId` para eventos de una facultad
3. **El backend valida** que los IDs existan y estén activos.
4. **El filtrado es automático**: cada usuario solo ve eventos relevantes para su carrera/facultad.

