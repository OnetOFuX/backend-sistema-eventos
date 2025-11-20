# 🚀 PROMPT PARA EQUIPO DE FRONTEND - Cambios Backend Sistema de Eventos

## 📢 COMUNICADO IMPORTANTE

El backend ha sido **completamente refactorizado** para usar tablas de base de datos en lugar de listas estáticas para gestionar Facultades y Carreras.

---

## ⚠️ CAMBIOS BREAKING - ACCIÓN REQUERIDA URGENTE

### ❌ LO QUE YA NO FUNCIONA:
```javascript
// REGISTRO DE USUARIO (OBSOLETO)
{
  "carrera": "Ingeniería de Sistemas"  // ❌ NO FUNCIONA
}

// CREAR EVENTO (OBSOLETO)
{
  "carrera": "TODAS",                  // ❌ NO FUNCIONA
  "facultad": "Ingeniería y Arquitectura"  // ❌ NO FUNCIONA
}
```

### ✅ NUEVO FORMATO REQUERIDO:
```javascript
// REGISTRO DE USUARIO (NUEVO)
{
  "carreraId": 10  // ✅ ID numérico
}

// CREAR EVENTO (NUEVO)
{
  "paraTodas": true  // ✅ Boolean para evento global
  // O
  "carreraId": 10    // ✅ ID numérico de carrera
  // O
  "facultadId": 4    // ✅ ID numérico de facultad
}
```

---

## 🔧 TAREAS PRIORITARIAS PARA EL FRONTEND

### 1️⃣ ACTUALIZAR FORMULARIO DE REGISTRO
**Archivo:** Componente de Registro/SignUp

**Cambios necesarios:**
1. Agregar llamada API para cargar facultades:
   ```javascript
   const facultades = await fetch('http://localhost:8080/catalog/facultades')
     .then(r => r.json());
   // Devuelve: [{ id: 1, nombre: "Ciencias Empresariales", ... }]
   ```

2. Cuando usuario selecciona facultad, cargar carreras:
   ```javascript
   const carreras = await fetch(`http://localhost:8080/catalog/facultades/${facultadId}/carreras`)
     .then(r => r.json());
   // Devuelve: [{ id: 10, nombre: "Ingeniería de Sistemas", ... }]
   ```

3. Cambiar el campo de texto `carrera` por 2 dropdowns:
   - Dropdown 1: Seleccionar Facultad
   - Dropdown 2: Seleccionar Carrera (se llena al elegir facultad)

4. En el JSON del POST, enviar `carreraId` (número) en lugar de `carrera` (string)

**Código ejemplo:**
```jsx
// Estado
const [facultades, setFacultades] = useState([]);
const [carreras, setCarreras] = useState([]);
const [selectedFacultad, setSelectedFacultad] = useState(null);
const [selectedCarrera, setSelectedCarrera] = useState(null);

// Cargar facultades al montar
useEffect(() => {
  fetch('/catalog/facultades')
    .then(r => r.json())
    .then(data => setFacultades(data.data));
}, []);

// Cargar carreras cuando cambia facultad
useEffect(() => {
  if (selectedFacultad) {
    fetch(`/catalog/facultades/${selectedFacultad}/carreras`)
      .then(r => r.json())
      .then(data => setCarreras(data.data));
  }
}, [selectedFacultad]);

// Al enviar formulario
const registroData = {
  codigo: formData.codigo,
  email: formData.email,
  password: formData.password,
  nombre: formData.nombre,
  apellido: formData.apellido,
  telefono: formData.telefono,
  carreraId: selectedCarrera,  // ⚠️ ID numérico, no string
  ciclo: formData.ciclo
};
```

---

### 2️⃣ ACTUALIZAR FORMULARIO DE CREAR EVENTO
**Archivo:** Componente CreateEvent/NuevoEvento

**Cambios necesarios:**
1. Agregar checkbox "Este evento es para TODOS"
2. Si checkbox desmarcado, mostrar opciones:
   - Radio button: "Para una carrera" o "Para una facultad"
3. Mostrar dropdowns según selección:
   - Si "carrera": mostrar dropdown Facultad + Carrera
   - Si "facultad": mostrar solo dropdown Facultad
4. En el JSON del POST:
   - Si checkbox marcado: `paraTodas: true`
   - Si carrera seleccionada: `carreraId: X`
   - Si facultad seleccionada: `facultadId: X`

**Código ejemplo:**
```jsx
const [paraTodas, setParaTodas] = useState(false);
const [targetType, setTargetType] = useState('carrera'); // 'carrera' o 'facultad'

// En el render
<Checkbox 
  label="Este evento es para TODOS"
  checked={paraTodas}
  onChange={setParaTodas}
/>

{!paraTodas && (
  <>
    <RadioGroup value={targetType} onChange={setTargetType}>
      <Radio value="carrera">Una carrera específica</Radio>
      <Radio value="facultad">Una facultad completa</Radio>
    </RadioGroup>
    
    {targetType === 'carrera' && (
      <>
        <Select label="Facultad" ... />
        <Select label="Carrera" ... />
      </>
    )}
    
    {targetType === 'facultad' && (
      <Select label="Facultad" ... />
    )}
  </>
)}

// Al enviar
const eventoData = {
  ...camposNormales,
  paraTodas: paraTodas,
  carreraId: targetType === 'carrera' ? selectedCarrera : null,
  facultadId: targetType === 'facultad' ? selectedFacultad : null
};
```

---

### 3️⃣ ACTUALIZAR VISUALIZACIÓN DE EVENTOS
**Archivo:** Componente EventCard/TarjetaEvento

**Cambios en el DTO recibido:**
El backend ahora devuelve:
```javascript
{
  id: 1,
  titulo: "Taller de Java",
  // ... otros campos ...
  carreraId: 10,                              // ⚠️ NUEVO
  carreraNombre: "Ingeniería de Sistemas",    // ⚠️ NUEVO
  facultadId: 4,                              // ⚠️ NUEVO
  facultadNombre: "Ingeniería y Arquitectura",// ⚠️ NUEVO
  paraTodas: false                            // ⚠️ NUEVO
}
```

**Mostrar badges de visibilidad:**
```jsx
<EventCard>
  <h3>{evento.titulo}</h3>
  <p>{evento.descripcion}</p>
  
  {/* Badge según tipo de evento */}
  {evento.paraTodas && (
    <Badge color="blue">📢 Para todos</Badge>
  )}
  
  {evento.carreraNombre && !evento.paraTodas && (
    <Badge color="green">🎓 {evento.carreraNombre}</Badge>
  )}
  
  {evento.facultadNombre && !evento.carreraNombre && !evento.paraTodas && (
    <Badge color="purple">🏛️ {evento.facultadNombre}</Badge>
  )}
</EventCard>
```

---

## 📡 NUEVOS ENDPOINTS DISPONIBLES

### GET /catalog/facultades
```javascript
fetch('http://localhost:8080/catalog/facultades')
// Respuesta: { status: "success", data: [{ id, nombre, codigo, ... }] }
```

### GET /catalog/carreras
```javascript
fetch('http://localhost:8080/catalog/carreras')
// Respuesta: { status: "success", data: [{ id, nombre, facultadId, facultadNombre, ... }] }
```

### GET /catalog/facultades/{id}/carreras
```javascript
fetch('http://localhost:8080/catalog/facultades/4/carreras')
// Respuesta: carreras de esa facultad específica
```

---

## 🎯 COMPORTAMIENTO DEL FILTRADO (AUTOMÁTICO)

El endpoint `GET /eventos/publicos` ahora filtra automáticamente:

| Usuario | Eventos que ve |
|---------|----------------|
| Sin login | Solo eventos con `paraTodas: true` |
| Estudiante | Eventos de su carrera + su facultad + `paraTodas: true` |
| Admin | TODOS los eventos |

**No necesitas filtrar en el frontend**, el backend ya lo hace.

---

## 📋 DATOS PRECARGADOS EN BD

Al iniciar el backend, ya están creados:

**4 Facultades:**
1. Ciencias Empresariales (id: 1)
2. Ciencias Humanas y Educación (id: 2)
3. Ciencias de la Salud (id: 3)
4. Ingeniería y Arquitectura (id: 4)

**13 Carreras:**
- FCE: Administración Contabilidad, Gestión Tributaria
- FCHE: Educación Inicial, Educación Primaria, Educación Inglés/Español
- FCS: Enfermería, Nutrición, Psicología
- FIA: Ing. Alimentos, Ing. Sistemas, Arquitectura, Ing. Ambiental, Ing. Civil

⚠️ **Importante:** Los IDs pueden variar. Usa los endpoints para obtener los datos reales.

---

## 🧪 CÓMO PROBAR

### 1. Registrar usuario:
```bash
POST http://localhost:8080/auth/registro
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

### 2. Crear evento (como admin):
```bash
POST http://localhost:8080/eventos
Authorization: Bearer {token}
{
  "titulo": "Test Event",
  "tipo": "TALLER",
  "ubicacion": "Aula 101",
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "fechas": [{
    "fechaInicio": "2026-01-10T10:00:00",
    "fechaFin": "2026-01-10T12:00:00"
  }],
  "carreraId": 10,
  "paraTodas": false
}
```

### 3. Ver eventos:
```bash
GET http://localhost:8080/eventos/publicos
Authorization: Bearer {token}
```

---

## 📚 DOCUMENTACIÓN COMPLETA

Para detalles técnicos completos, consultar:
- `FRONTEND_INSTRUCTIONS.md` - Guía detallada paso a paso
- `EJEMPLOS_JSON.md` - Ejemplos de JSON para todas las operaciones
- `JSON_REGISTRO_USUARIO.md` - Ejemplos específicos de registro

---

## ⏰ TIMELINE

- **Fecha de cambio:** 2025-11-20
- **Prioridad:** 🔴 CRÍTICA - Breaking changes
- **Deadline sugerido:** ASAP - El backend actual no acepta el formato anterior

---

## 💬 PREGUNTAS FRECUENTES

**Q: ¿Por qué este cambio?**  
A: Para tener integridad referencial, permitir administración de carreras sin cambiar código, y mejorar el rendimiento con relaciones de BD.

**Q: ¿El frontend debe validar las carreras?**  
A: No, el backend ya valida que los IDs existan y estén activos.

**Q: ¿Qué pasa si envío el formato antiguo?**  
A: Obtendrás un error 400 o 500 del backend.

**Q: ¿Dónde obtengo los IDs de las carreras?**  
A: Llamando a `GET /catalog/facultades` y `GET /catalog/carreras`.

---

## 🆘 SOPORTE

Si tienes dudas o encuentras problemas:
1. Revisa la documentación completa en `FRONTEND_INSTRUCTIONS.md`
2. Revisa ejemplos en `EJEMPLOS_JSON.md`
3. Contacta al equipo de backend con el error específico

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

```
□ Actualizar componente de registro (dropdowns de facultad/carrera)
□ Cambiar POST /auth/registro para enviar carreraId
□ Actualizar componente de crear evento (checkbox + radio + dropdowns)
□ Cambiar POST /eventos para enviar carreraId/facultadId/paraTodas
□ Actualizar componente de visualización de eventos (mostrar badges)
□ Probar flujo completo: registro → login → ver eventos → crear evento
□ Verificar que el filtrado funciona correctamente
□ Actualizar tipos TypeScript/interfaces (si aplica)
□ Actualizar tests (si aplica)
```

---

**¡Estos cambios son necesarios para que la aplicación funcione correctamente!**  
El equipo de backend está disponible para resolver dudas. 🚀

