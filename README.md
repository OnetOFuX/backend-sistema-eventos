# 🧪 Guía de Pruebas con Postman

## 📌 Configuración Inicial

### Base URL
```
http://localhost:8080/api
```

### Variables de Entorno (opcional)
Crea estas variables en Postman:
- `baseUrl`: `http://localhost:8080/api`
- `token`: (se llenará automáticamente después del login)

---

## 🔐 1. AUTENTICACIÓN

### 1.1 Login (POST /auth/login)
```json
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "emailOrCodigo": "admin@upeu.edu.pe",
  "password": "Admin123!"
}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "usuario": {
      "id": 1,
      "codigo": "ADMIN001",
      "email": "admin@upeu.edu.pe",
      "nombre": "Administrador",
      "apellido": "Sistema",
      "rol": "ADMINISTRADOR"
    }
  }
}
```

**⚠️ IMPORTANTE:** Copia el `token` y úsalo en el header `Authorization: Bearer {token}` para las siguientes peticiones.

### 1.2 Registro (POST /auth/registro)
```json
POST {{baseUrl}}/auth/registro
Content-Type: application/json

{
  "codigo": "2020123456",
  "email": "estudiante@upeu.edu.pe",
  "password": "Estudiante123!",
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "987654321",
  "carrera": "Ingeniería de Sistemas",
  "ciclo": 5,
  "rol": "PARTICIPANTE"
}
```

---

## 👥 2. USUARIOS

### 2.1 Obtener Todos los Usuarios (GET /usuarios)
```
GET {{baseUrl}}/usuarios
Authorization: Bearer {token}
```

### 2.2 Obtener Usuario por ID (GET /usuarios/{id})
```
GET {{baseUrl}}/usuarios/1
Authorization: Bearer {token}
```

### 2.3 Obtener Usuario por Código (GET /usuarios/codigo/{codigo})
```
GET {{baseUrl}}/usuarios/codigo/2020123456
Authorization: Bearer {token}
```

### 2.4 Actualizar Usuario (PUT /usuarios/{id})
```json
PUT {{baseUrl}}/usuarios/2
Authorization: Bearer {token}
Content-Type: application/json

{
  "telefono": "999888777",
  "carrera": "Ingeniería de Software",
  "ciclo": 6
}
```

### 2.5 Desactivar Usuario (PATCH /usuarios/{id}/desactivar)
```
PATCH {{baseUrl}}/usuarios/2/desactivar
Authorization: Bearer {token}
```

---

## 🎉 3. EVENTOS

### 3.1 Crear Evento (POST /eventos)
```json
POST {{baseUrl}}/eventos
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Seminario de Inteligencia Artificial",
  "descripcion": "Seminario sobre las últimas tendencias en IA",
  "tipo": "ACADEMICO",
  "ubicacion": "Auditorio Principal - UPEU Juliaca",
  "cupoMaximo": 100,
  "imagenUrl": "https://example.com/imagen.jpg",
  "esPago": true,
  "precio": 50.00,
  "metodosPago": ["EFECTIVO", "YAPE", "PLIN"],
  "requiereComprobante": true,
  "fechas": [
    {
      "fechaInicio": "2024-12-20T09:00:00",
      "fechaFin": "2024-12-20T13:00:00",
      "observaciones": "Primera sesión"
    },
    {
      "fechaInicio": "2024-12-21T09:00:00",
      "fechaFin": "2024-12-21T13:00:00",
      "observaciones": "Segunda sesión"
    }
  ]
}
```

### 3.2 Obtener Eventos Activos (GET /eventos/activos)
```
GET {{baseUrl}}/eventos/activos
```

### 3.3 Obtener Eventos Públicos (GET /eventos/publicos)
```
GET {{baseUrl}}/eventos/publicos
```
*(No requiere autenticación)*

### 3.4 Obtener Eventos por Tipo (GET /eventos/tipo/{tipo})
```
GET {{baseUrl}}/eventos/tipo/ACADEMICO
```

**Tipos disponibles:** `ACADEMICO`, `CULTURAL`, `ADMINISTRATIVO`, `DEPORTIVO`

### 3.5 Obtener Eventos Próximos (GET /eventos/proximos)
```
GET {{baseUrl}}/eventos/proximos
```

### 3.6 Obtener Eventos de Pago (GET /eventos/pago)
```
GET {{baseUrl}}/eventos/pago
```

### 3.7 Obtener Eventos Gratuitos (GET /eventos/gratuitos)
```
GET {{baseUrl}}/eventos/gratuitos
```

### 3.8 Actualizar Evento (PUT /eventos/{id})
```json
PUT {{baseUrl}}/eventos/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Seminario de IA - Actualizado",
  "precio": 40.00
}
```

### 3.9 Obtener Cupos Disponibles (GET /eventos/{id}/cupos-disponibles)
```
GET {{baseUrl}}/eventos/1/cupos-disponibles
```

---

## 📝 4. INSCRIPCIONES

### 4.1 Crear Inscripción (POST /inscripciones)
```json
POST {{baseUrl}}/inscripciones
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventoId": 1,
  "metodoPago": "YAPE",
  "montoPagado": 50.00,
  "comprobanteUrl": "https://example.com/comprobante.jpg",
  "observaciones": "Pago realizado"
}
```

**Métodos de pago disponibles:** `GRATUITO`, `EFECTIVO`, `YAPE`, `PLIN`, `TRANSFERENCIA`, `TARJETA`

### 4.2 Obtener Mis Inscripciones (GET /inscripciones/mis-inscripciones)
```
GET {{baseUrl}}/inscripciones/mis-inscripciones
Authorization: Bearer {token}
```

### 4.3 Obtener Inscripciones por Evento (GET /inscripciones/evento/{eventoId})
```
GET {{baseUrl}}/inscripciones/evento/1
Authorization: Bearer {token}
```

### 4.4 Verificar Pago (PATCH /inscripciones/{id}/verificar-pago)
```
PATCH {{baseUrl}}/inscripciones/1/verificar-pago
Authorization: Bearer {token}
```
*(Solo ADMINISTRADOR o COORDINADOR)*

### 4.5 Subir Comprobante (PATCH /inscripciones/{id}/comprobante)
```
PATCH {{baseUrl}}/inscripciones/1/comprobante?comprobanteUrl=https://example.com/comprobante.jpg
Authorization: Bearer {token}
```

### 4.6 Generar QR de Inscripción (GET /inscripciones/{id}/qr)
```
GET {{baseUrl}}/inscripciones/1/qr
Authorization: Bearer {token}
```
*(Devuelve una imagen PNG)*

### 4.7 Cancelar Inscripción (DELETE /inscripciones/{id})
```
DELETE {{baseUrl}}/inscripciones/1
Authorization: Bearer {token}
```

---

## ✅ 5. ASISTENCIAS

### 5.1 Registrar Asistencia por QR (POST /asistencias/qr)
```
POST {{baseUrl}}/asistencias/qr?codigoQr=abc123&fechaEventoId=1
Authorization: Bearer {token}
```
*(Solo ADMINISTRADOR o COORDINADOR)*

### 5.2 Registrar Asistencia Manual (POST /asistencias/manual)
```
POST {{baseUrl}}/asistencias/manual?codigoEstudiante=2020123456&fechaEventoId=1
Authorization: Bearer {token}
```
*(Solo ADMINISTRADOR o COORDINADOR)*

### 5.3 Obtener Mis Asistencias (GET /asistencias/mis-asistencias)
```
GET {{baseUrl}}/asistencias/mis-asistencias
Authorization: Bearer {token}
```

### 5.4 Obtener Asistencias por Fecha de Evento (GET /asistencias/fecha-evento/{fechaEventoId})
```
GET {{baseUrl}}/asistencias/fecha-evento/1
Authorization: Bearer {token}
```

### 5.5 Obtener Asistencias por Evento (GET /asistencias/evento/{eventoId})
```
GET {{baseUrl}}/asistencias/evento/1
Authorization: Bearer {token}
```

---

## 📊 6. FLUJO COMPLETO DE PRUEBA

### Paso 1: Login como Admin
```json
POST /auth/login
{
  "emailOrCodigo": "admin@upeu.edu.pe",
  "password": "Admin123!"
}
```

### Paso 2: Registrar un Estudiante
```json
POST /auth/registro
{
  "codigo": "2020111111",
  "email": "estudiante1@upeu.edu.pe",
  "password": "Est123!",
  "nombre": "María",
  "apellido": "García",
  "telefono": "987654321",
  "carrera": "Administración",
  "ciclo": 3
}
```

### Paso 3: Crear un Evento (como Admin)
```json
POST /eventos
{
  "titulo": "Taller de Emprendimiento",
  "descripcion": "Taller práctico de emprendimiento",
  "tipo": "ACADEMICO",
  "ubicacion": "Sala 101",
  "cupoMaximo": 50,
  "esPago": false,
  "fechas": [
    {
      "fechaInicio": "2024-12-25T14:00:00",
      "fechaFin": "2024-12-25T18:00:00"
    }
  ]
}
```

### Paso 4: Inscribirse al Evento (como Estudiante)
```json
POST /inscripciones
{
  "eventoId": 1
}
```

### Paso 5: Registrar Asistencia (como Admin/Coordinador)
```
POST /asistencias/manual?codigoEstudiante=2020111111&fechaEventoId=1
```

---

## 🔧 Configuración de Headers en Postman

Para todas las peticiones autenticadas, agrega el header:
```
Authorization: Bearer {tu_token_aqui}
```

Puedes automatizar esto en Postman:
1. Ve a la colección
2. Edit → Authorization
3. Type: Bearer Token
4. Token: `{{token}}`
5. Guarda

Luego, en el script "Tests" del endpoint de login, agrega:
```javascript
var jsonData = pm.response.json();
pm.environment.set("token", jsonData.data.token);
```

---

## 📝 Notas Importantes

1. **Base de Datos:** Asegúrate de tener MySQL corriendo en `localhost:3306`
2. **Credenciales Admin por defecto:**
   - Email: `admin@upeu.edu.pe`
   - Password: `Admin123!`
3. **Roles disponibles:** `ADMINISTRADOR`, `COORDINADOR`, `PARTICIPANTE`
4. **Estados de Inscripción:** `PENDIENTE`, `CONFIRMADA`, `PAGADA`, `CANCELADA`

---

## ⚠️ Solución de Problemas

### Error 401 Unauthorized
- Verifica que el token sea válido
- Asegúrate de incluir "Bearer " antes del token

### Error 403 Forbidden
- Verifica que tu rol tenga permisos para ese endpoint
- Los endpoints de administración requieren rol ADMINISTRADOR

### Error 500 Internal Server Error
- Revisa los logs de la consola del backend
- Verifica la conexión con MySQL
- Asegúrate de que las tablas estén creadas

---

## 🎯 Colección de Postman

Puedes importar esta colección en Postman para facilitar las pruebas.

**Pasos:**
1. Abre Postman
2. Click en "Import"
3. Copia y pega los ejemplos anteriores
4. Configura las variables de entorno
5. ¡Comienza a probar!
