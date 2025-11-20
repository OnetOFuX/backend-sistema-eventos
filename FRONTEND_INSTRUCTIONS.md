# 📋 GUÍA COMPLETA PARA FRONTEND - Sistema de Eventos con Carreras y Facultades

## 🔄 CAMBIOS IMPORTANTES EN EL BACKEND

El sistema ahora usa **tablas relacionales** para gestionar Facultades y Carreras en lugar de listas estáticas.

### ✅ Nuevas Entidades y Relaciones
- **`facultades`** tabla con: id, nombre, codigo, descripcion, activo
- **`carreras`** tabla con: id, nombre, codigo, descripcion, activo, facultad_id
- **`usuarios`** ahora tiene `carrera_id` (relación a carreras)
- **`eventos`** ahora tiene `carrera_id`, `facultad_id` y `para_todas` (boolean)

---

## 🎯 ENDPOINTS ACTUALIZADOS

### 1. **Obtener Facultades**
```
GET /catalog/facultades
```

**Respuesta:**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "nombre": "Ciencias Empresariales",
      "codigo": "FCE",
      "descripcion": "Facultad de Ciencias Empresariales",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Ciencias Humanas y Educación",
      "codigo": "FCHE",
      "descripcion": "...",
      "activo": true
    }
    // ... más facultades
  ]
}
```

### 2. **Obtener Todas las Carreras**
```
GET /catalog/carreras
```

**Respuesta:**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "nombre": "Administración Contabilidad",
      "codigo": "ADMIN-CONT",
      "descripcion": "...",
      "activo": true,
      "facultadId": 1,
      "facultadNombre": "Ciencias Empresariales"
    },
    {
      "id": 2,
      "nombre": "Ingeniería de Sistemas",
      "codigo": "ING-SIST",
      "descripcion": "...",
      "activo": true,
      "facultadId": 4,
      "facultadNombre": "Ingeniería y Arquitectura"
    }
    // ... más carreras
  ]
}
```

### 3. **Obtener Carreras por Facultad**
```
GET /catalog/facultades/{facultadId}/carreras
```

**Ejemplo:**
```
GET /catalog/facultades/4/carreras
```

**Respuesta:** Lista de carreras que pertenecen a esa facultad.

---

## 📝 REGISTRO DE USUARIO (ACTUALIZADO)

### Endpoint:
```
POST /auth/registro
```

### Nuevo formato del JSON:
```json
{
  "codigo": "2024001",
  "email": "juan@example.com",
  "password": "secreto123",
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "987654321",
  "carreraId": 10,  // ⚠️ CAMBIÓ: Ahora envías el ID de la carrera
  "ciclo": "VIII"
}
```

**Importante:**
- ❌ **YA NO envíes** `"carrera": "Ingeniería de Sistemas"`
- ✅ **Ahora envía** `"carreraId": 10` (el ID numérico de la carrera)

### Flujo recomendado en el Frontend:

1. **Cargar facultades** al montar el componente de registro:
   ```javascript
   const facultades = await fetch('/catalog/facultades').then(r => r.json());
   ```

2. **Cuando el usuario selecciona una facultad**, cargar las carreras de esa facultad:
   ```javascript
   const carreras = await fetch(`/catalog/facultades/${facultadId}/carreras`).then(r => r.json());
   ```

3. **Al enviar el formulario**, incluir el `carreraId` seleccionado.

---

## 🎉 CREACIÓN DE EVENTOS (ACTUALIZADO)

### Endpoint:
```
POST /eventos
Headers: Authorization: Bearer {token}
```

### Nuevo formato del JSON:

#### Opción 1: Evento para UNA CARRERA específica
```json
{
  "titulo": "Taller de Java",
  "descripcion": "...",
  "tipo": "TALLER",
  "ubicacion": "Aula 101",
  "cupoMaximo": 50,
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "fechas": [
    {
      "fechaInicio": "2026-01-10T09:00:00",
      "fechaFin": "2026-01-10T12:00:00"
    }
  ],
  "carreraId": 10,  // ⚠️ CAMBIÓ: Ahora es ID numérico
  "paraTodas": false
}
```

#### Opción 2: Evento para UNA FACULTAD completa
```json
{
  "titulo": "Seminario de Salud",
  "descripcion": "...",
  "tipo": "SEMINARIO",
  "ubicacion": "Auditorio",
  "cupoMaximo": 200,
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "fechas": [
    {
      "fechaInicio": "2026-02-15T10:00:00",
      "fechaFin": "2026-02-15T13:00:00"
    }
  ],
  "facultadId": 3,  // ⚠️ CAMBIÓ: ID de la facultad
  "paraTodas": false
}
```

#### Opción 3: Evento PARA TODOS (sin restricción)
```json
{
  "titulo": "Conferencia General",
  "descripcion": "...",
  "tipo": "CONFERENCIA",
  "ubicacion": "Coliseo",
  "cupoMaximo": 500,
  "esPago": false,
  "metodosPago": ["EFECTIVO"],
  "fechas": [
    {
      "fechaInicio": "2026-03-20T08:00:00",
      "fechaFin": "2026-03-20T18:00:00"
    }
  ],
  "paraTodas": true  // ⚠️ TRUE = visible para TODOS
}
```

**Reglas de validación:**
- Si envías `carreraId` y `facultadId`, la carrera debe pertenecer a esa facultad (el backend lo valida).
- Si `paraTodas: true`, no es necesario enviar `carreraId` ni `facultadId`.
- Si `paraTodas: false`, debes enviar al menos `carreraId` o `facultadId`.

---

## 👁️ VISUALIZACIÓN DE EVENTOS (FILTRADO AUTOMÁTICO)

### Endpoint:
```
GET /eventos/publicos
Headers: Authorization: Bearer {token} (opcional)
```

**Comportamiento del backend:**

1. **Sin autenticación (sin token):**
   - Solo devuelve eventos con `paraTodas: true`

2. **Usuario PARTICIPANTE autenticado:**
   - Devuelve eventos donde:
     - `paraTodas: true`, O
     - `carreraId` coincide con la carrera del usuario, O
     - `facultadId` coincide con la facultad del usuario

3. **Usuario ADMINISTRADOR:**
   - Devuelve TODOS los eventos activos (sin filtrar)

### Respuesta (EventoDTO actualizado):
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "titulo": "Taller de Java",
      "descripcion": "...",
      "tipo": "TALLER",
      "ubicacion": "Aula 101",
      "cupoMaximo": 50,
      "activo": true,
      "esPago": false,
      "precio": null,
      "metodosPago": ["EFECTIVO"],
      "requiereComprobante": false,
      "creadorId": 1,
      "creadorNombre": "Admin",
      "fechas": [...],
      "totalInscritos": 10,
      "cuposDisponibles": 40,
      "carreraId": 10,          // ⚠️ NUEVO: ID numérico
      "carreraNombre": "Ingeniería de Sistemas",  // ⚠️ NUEVO
      "facultadId": null,        // ⚠️ NUEVO
      "facultadNombre": null,    // ⚠️ NUEVO
      "paraTodas": false         // ⚠️ NUEVO
    }
  ]
}
```

---

## 🎨 RECOMENDACIONES PARA LA UI DEL FRONTEND

### Formulario de Registro de Usuario:

```jsx
<Form>
  {/* Campos normales: codigo, email, password, nombre, apellido, telefono, ciclo */}
  
  <Select 
    label="Facultad" 
    onChange={handleFacultadChange}
    options={facultades.map(f => ({ value: f.id, label: f.nombre }))}
  />
  
  <Select 
    label="Carrera" 
    disabled={!selectedFacultad}
    options={carreras.map(c => ({ value: c.id, label: c.nombre }))}
    value={formData.carreraId}
    onChange={(value) => setFormData({...formData, carreraId: value})}
  />
</Form>
```

### Formulario de Creación de Evento:

```jsx
<Form>
  {/* Campos normales: titulo, descripcion, tipo, ubicacion, etc. */}
  
  <Checkbox 
    label="Este evento es para TODOS (todas las facultades y carreras)"
    checked={paraTodas}
    onChange={(checked) => setParaTodas(checked)}
  />
  
  {!paraTodas && (
    <>
      <Radio.Group label="Dirigido a:">
        <Radio value="carrera">Una carrera específica</Radio>
        <Radio value="facultad">Una facultad completa</Radio>
      </Radio.Group>
      
      {tipoSeleccion === 'carrera' && (
        <>
          <Select label="Facultad" options={facultades} onChange={...} />
          <Select label="Carrera" options={carreras} onChange={...} />
        </>
      )}
      
      {tipoSeleccion === 'facultad' && (
        <Select label="Facultad" options={facultades} onChange={...} />
      )}
    </>
  )}
</Form>
```

### Tarjeta de Evento (mostrar visibilidad):

```jsx
<EventCard>
  <h3>{evento.titulo}</h3>
  <p>{evento.descripcion}</p>
  
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

## 🛠️ MIGRACIÓN DE CÓDIGO EXISTENTE

### Si ya tenías código que enviaba strings:

**ANTES (ya no funciona):**
```javascript
const registroData = {
  // ...
  carrera: "Ingeniería de Sistemas"  // ❌
}
```

**AHORA:**
```javascript
const registroData = {
  // ...
  carreraId: 10  // ✅ ID numérico
}
```

### Para eventos:

**ANTES:**
```javascript
const eventoData = {
  // ...
  carrera: "TODAS"  // ❌
}
```

**AHORA:**
```javascript
const eventoData = {
  // ...
  paraTodas: true  // ✅ Boolean
}
```

---

## 🔍 VALIDACIONES QUE HACE EL BACKEND

1. ✅ Valida que `carreraId` exista y esté activa
2. ✅ Valida que `facultadId` exista y esté activa
3. ✅ Valida consistencia entre carrera y facultad
4. ✅ Filtra eventos automáticamente según la carrera/facultad del usuario
5. ✅ Los admin ven todos los eventos sin filtro

---

## 📊 DATOS INICIALES

El backend ya incluye un **DataInitializer** que carga automáticamente al arrancar:
- 4 Facultades
- 13 Carreras (distribuidas en sus facultades)

No necesitas crear estos datos manualmente. ✅

---

## 🚀 RESUMEN DE ACCIONES PARA EL FRONTEND

### 1. **Actualizar formulario de registro:**
   - Cargar facultades con `GET /catalog/facultades`
   - Al seleccionar facultad, cargar carreras con `GET /catalog/facultades/{id}/carreras`
   - Enviar `carreraId` (número) en lugar de `carrera` (string)

### 2. **Actualizar formulario de eventos:**
   - Añadir checkbox "Para todos"
   - Si no es "para todos", mostrar opciones de carrera o facultad
   - Enviar `carreraId` o `facultadId` (números) + `paraTodas` (boolean)
   - Ya no enviar `carrera` ni `facultad` como strings

### 3. **Actualizar visualización de eventos:**
   - Usar `carreraId`, `carreraNombre`, `facultadId`, `facultadNombre`, `paraTodas`
   - Mostrar badges indicando la visibilidad del evento

### 4. **Probar el filtrado:**
   - Crear usuarios con diferentes carreras
   - Crear eventos para diferentes carreras/facultades
   - Verificar que cada usuario solo ve sus eventos correspondientes

---

## ❓ FAQ

**P: ¿Qué pasa si envío carrera como string?**  
R: El backend devolverá un error de tipo o validación. Debes enviar `carreraId` (número).

**P: ¿Puedo crear un evento sin carrera ni facultad?**  
R: Solo si `paraTodas: true`. Si es false, debes especificar al menos uno.

**P: ¿Los usuarios pueden cambiar su carrera?**  
R: Sí, mediante el endpoint de actualización de usuario (enviar `carreraId` nuevo).

**P: ¿Cómo sé qué eventos ve un usuario?**  
R: El endpoint `/eventos/publicos` ya filtra automáticamente según el token.

---

## ✅ CHECKLIST FINAL

- [ ] Actualizar llamada GET /catalog/facultades
- [ ] Actualizar llamada GET /catalog/carreras o /catalog/facultades/{id}/carreras
- [ ] Cambiar registro de usuario: `carrera` → `carreraId`
- [ ] Cambiar creación de evento: strings → IDs + boolean `paraTodas`
- [ ] Actualizar UI para mostrar `carreraNombre`, `facultadNombre`, `paraTodas`
- [ ] Probar flujo completo: registro → login → ver eventos filtrados
- [ ] Probar creación de eventos con diferentes opciones (carrera, facultad, todos)

---

**¡Listo! Con estos cambios el frontend estará sincronizado con el nuevo sistema de tablas relacionales.** 🎉

