-- Script de inicialización de Facultades y Carreras
-- Ejecutar este script después de crear las tablas

-- Insertar Facultades
INSERT INTO facultades (nombre, codigo, descripcion, activo, created_at, updated_at) VALUES
('Ciencias Empresariales', 'FCE', 'Facultad de Ciencias Empresariales', true, NOW(), NOW()),
('Ciencias Humanas y Educación', 'FCHE', 'Facultad de Ciencias Humanas y Educación', true, NOW(), NOW()),
('Ciencias de la Salud', 'FCS', 'Facultad de Ciencias de la Salud', true, NOW(), NOW()),
('Ingeniería y Arquitectura', 'FIA', 'Facultad de Ingeniería y Arquitectura', true, NOW(), NOW());

-- Insertar Carreras de Ciencias Empresariales
INSERT INTO carreras (nombre, codigo, descripcion, activo, facultad_id, created_at, updated_at) VALUES
('Administración Contabilidad', 'ADMIN-CONT', 'Carrera de Administración y Contabilidad', true,
    (SELECT id FROM facultades WHERE codigo = 'FCE'), NOW(), NOW()),
('Gestión Tributaria y Aduanera', 'GEST-TRIB', 'Carrera de Gestión Tributaria y Aduanera', true,
    (SELECT id FROM facultades WHERE codigo = 'FCE'), NOW(), NOW());

-- Insertar Carreras de Ciencias Humanas y Educación
INSERT INTO carreras (nombre, codigo, descripcion, activo, facultad_id, created_at, updated_at) VALUES
('Educación Inicial y Puericultura', 'EDU-INIC', 'Carrera de Educación Inicial y Puericultura', true,
    (SELECT id FROM facultades WHERE codigo = 'FCHE'), NOW(), NOW()),
('Educación Primaria y Pedagogía Terapéutica', 'EDU-PRIM', 'Carrera de Educación Primaria y Pedagogía Terapéutica', true,
    (SELECT id FROM facultades WHERE codigo = 'FCHE'), NOW(), NOW()),
('Educación, Especialidad Inglés y Español', 'EDU-LING', 'Carrera de Educación, Especialidad Inglés y Español', true,
    (SELECT id FROM facultades WHERE codigo = 'FCHE'), NOW(), NOW());

-- Insertar Carreras de Ciencias de la Salud
INSERT INTO carreras (nombre, codigo, descripcion, activo, facultad_id, created_at, updated_at) VALUES
('Enfermería', 'ENFERM', 'Carrera de Enfermería', true,
    (SELECT id FROM facultades WHERE codigo = 'FCS'), NOW(), NOW()),
('Nutrición Humana', 'NUTRIC', 'Carrera de Nutrición Humana', true,
    (SELECT id FROM facultades WHERE codigo = 'FCS'), NOW(), NOW()),
('Psicología', 'PSICOL', 'Carrera de Psicología', true,
    (SELECT id FROM facultades WHERE codigo = 'FCS'), NOW(), NOW());

-- Insertar Carreras de Ingeniería y Arquitectura
INSERT INTO carreras (nombre, codigo, descripcion, activo, facultad_id, created_at, updated_at) VALUES
('Ingeniería de Industria Alimentarias', 'ING-ALIM', 'Carrera de Ingeniería de Industria Alimentarias', true,
    (SELECT id FROM facultades WHERE codigo = 'FIA'), NOW(), NOW()),
('Ingeniería de Sistemas', 'ING-SIST', 'Carrera de Ingeniería de Sistemas', true,
    (SELECT id FROM facultades WHERE codigo = 'FIA'), NOW(), NOW()),
('Arquitectura', 'ARQUIT', 'Carrera de Arquitectura', true,
    (SELECT id FROM facultades WHERE codigo = 'FIA'), NOW(), NOW()),
('Ingeniería Ambiental', 'ING-AMB', 'Carrera de Ingeniería Ambiental', true,
    (SELECT id FROM facultades WHERE codigo = 'FIA'), NOW(), NOW()),
('Ingeniería Civil', 'ING-CIV', 'Carrera de Ingeniería Civil', true,
    (SELECT id FROM facultades WHERE codigo = 'FIA'), NOW(), NOW());

