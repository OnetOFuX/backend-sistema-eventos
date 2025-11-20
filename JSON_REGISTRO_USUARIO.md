# 📄 JSON PARA CREAR NUEVO USUARIO (ACTUALIZADO)

## ⚠️ CAMBIO IMPORTANTE

**ANTES (ya no funciona):**
```json
{
  "carrera": "Ingeniería de Sistemas"
}
```

**AHORA (formato correcto):**
```json
{
  "carreraId": 10
}
```

---

## 🎯 FORMATO ACTUALIZADO

### Endpoint: `POST /auth/registro`

### JSON Completo:
```json
{
  "codigo": "2024001",
  "email": "usuario@upeu.edu.pe",
  "password": "contraseña123",
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "987654321",
  "carreraId": 10,
  "ciclo": "VIII"
}
```

---

## 📋 PASOS PARA OBTENER EL carreraId CORRECTO

### Opción 1: Listar todas las carreras
```
GET http://localhost:8080/catalog/carreras
```

**Respuesta:**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "nombre": "Administración Contabilidad",
      "facultadId": 1,
      "facultadNombre": "Ciencias Empresariales"
    },
    {
      "id": 10,
      "nombre": "Ingeniería de Sistemas",
      "facultadId": 4,
      "facultadNombre": "Ingeniería y Arquitectura"
    }
    // ... más carreras
  ]
}
```

### Opción 2: Listar carreras por facultad
```
GET http://localhost:8080/catalog/facultades
```
Primero obtén el ID de la facultad, luego:

```
GET http://localhost:8080/catalog/facultades/4/carreras
```
(Ejemplo para Ingeniería y Arquitectura)

---

## 📊 REFERENCIA RÁPIDA DE IDs

### IDs Aproximados de Carreras:

| ID | Carrera | Facultad |
|----|---------|----------|
| 1  | Administración Contabilidad | Ciencias Empresariales |
| 2  | Gestión Tributaria y Aduanera | Ciencias Empresariales |
| 3  | Educación Inicial y Puericultura | Ciencias Humanas y Educación |
| 4  | Educación Primaria y Pedagogía Terapéutica | Ciencias Humanas y Educación |
| 5  | Educación, Especialidad Inglés y Español | Ciencias Humanas y Educación |
| 6  | Enfermería | Ciencias de la Salud |
| 7  | Nutrición Humana | Ciencias de la Salud |
| 8  | Psicología | Ciencias de la Salud |
| 9  | Ingeniería de Industria Alimentarias | Ingeniería y Arquitectura |
| 10 | Ingeniería de Sistemas | Ingeniería y Arquitectura |
| 11 | Arquitectura | Ingeniería y Arquitectura |
| 12 | Ingeniería Ambiental | Ingeniería y Arquitectura |
| 13 | Ingeniería Civil | Ingeniería y Arquitectura |

**⚠️ Nota:** Estos IDs son aproximados. Usa los endpoints para obtener los IDs reales de tu BD.

---

## 💡 EJEMPLOS COMPLETOS

### Ejemplo 1: Estudiante de Ingeniería de Sistemas
```json
{
  "codigo": "2024001",
  "email": "carlos.martinez@upeu.edu.pe",
  "password": "miPassword123",
  "nombre": "Carlos",
  "apellido": "Martínez López",
  "telefono": "987654321",
  "carreraId": 10,
  "ciclo": "VIII"
}
```

### Ejemplo 2: Estudiante de Enfermería
```json
{
  "codigo": "2024002",
  "email": "maria.garcia@upeu.edu.pe",
  "password": "maria456789",
  "nombre": "María",
  "apellido": "García Torres",
  "telefono": "999888777",
  "carreraId": 6,
  "ciclo": "V"
}
```

### Ejemplo 3: Estudiante de Psicología
```json
{
  "codigo": "2024003",
  "email": "ana.rodriguez@upeu.edu.pe",
  "password": "ana321654",
  "nombre": "Ana",
  "apellido": "Rodríguez Pérez",
  "telefono": "988776655",
  "carreraId": 8,
  "ciclo": "VI"
}
```

### Ejemplo 4: Administrador (con rol específico)
```json
{
  "codigo": "ADMIN001",
  "email": "admin@upeu.edu.pe",
  "password": "admin123456",
  "nombre": "Administrador",
  "apellido": "Sistema",
  "telefono": "999999999",
  "carreraId": 10,
  "ciclo": "X",
  "rol": "ADMINISTRADOR"
}
```

### Ejemplo 5: Coordinador
```json
{
  "codigo": "COORD001",
  "email": "coordinador@upeu.edu.pe",
  "password": "coord123456",
  "nombre": "Luis",
  "apellido": "Fernández",
  "telefono": "988888888",
  "carreraId": 10,
  "ciclo": "X",
  "rol": "COORDINADOR"
}
```

---

## ✅ VALIDACIONES DEL BACKEND

El backend validará automáticamente:
- ✅ Que el `carreraId` exista en la base de datos
- ✅ Que la carrera esté activa (`activo = true`)
- ✅ Que el email no esté duplicado
- ✅ Que el código no esté duplicado
- ✅ Que la contraseña tenga al menos 6 caracteres

---

## ❌ ERRORES COMUNES

### Error 1: "Carrera no encontrada"
```json
{
  "status": "error",
  "message": "Carrera no encontrada con ID: 999"
}
```
**Solución:** Verifica que el `carreraId` sea correcto usando `GET /catalog/carreras`

### Error 2: "La carrera seleccionada no está activa"
```json
{
  "status": "error",
  "message": "La carrera seleccionada no está activa"
}
```
**Solución:** Esa carrera fue desactivada. Selecciona otra carrera activa.

### Error 3: "El email ya está registrado"
```json
{
  "status": "error",
  "message": "El email ya está registrado"
}
```
**Solución:** Usa un email diferente.

---

## 🔍 CÓMO PROBAR EN POSTMAN

### Paso 1: Obtener lista de carreras
```
GET http://localhost:8080/catalog/carreras
```

### Paso 2: Copiar el ID de la carrera deseada
Por ejemplo, si ves:
```json
{
  "id": 10,
  "nombre": "Ingeniería de Sistemas",
  ...
}
```
Copia el `id: 10`

### Paso 3: Registrar usuario
```
POST http://localhost:8080/auth/registro
Content-Type: application/json

{
  "codigo": "2024001",
  "email": "test@upeu.edu.pe",
  "password": "test123",
  "nombre": "Test",
  "apellido": "User",
  "telefono": "999999999",
  "carreraId": 10,
  "ciclo": "VIII"
}
```

### Paso 4: Verificar respuesta exitosa
```json
{
  "status": "success",
  "message": "Registro exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "usuario": {
      "id": 1,
      "codigo": "2024001",
      "email": "test@upeu.edu.pe",
      "nombre": "Test",
      "apellido": "User",
      "carreraId": 10,
      "carreraNombre": "Ingeniería de Sistemas",
      "rol": "PARTICIPANTE"
    }
  }
}
```

---

## 🎉 ¡Listo!

Ahora ya puedes registrar usuarios usando el nuevo formato con `carreraId`.

**Recuerda:** El frontend debe actualizarse para:
1. Cargar la lista de carreras
2. Mostrar un dropdown al usuario
3. Enviar el `carreraId` seleccionado (no el nombre)

