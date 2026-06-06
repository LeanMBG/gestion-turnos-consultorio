USE gestion_turnos;

CREATE TABLE pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni INT NOT NULL UNIQUE,
    telefono INT,
    email VARCHAR(100)
);

CREATE TABLE especialidades (
    id_especialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(150)
);

CREATE TABLE medicos (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    matricula VARCHAR(30) NOT NULL UNIQUE,
    telefono INT,
    id_especialidad INT NOT NULL,
    CONSTRAINT fk_medicos_especialidades
        FOREIGN KEY (id_especialidad)
        REFERENCES especialidades(id_especialidad)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE estados_turno (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL
);

CREATE TABLE turnos (
    id_turno INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    observacion VARCHAR(200),
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    id_estado INT NOT NULL,
    id_usuario INT NOT NULL,

    CONSTRAINT fk_turnos_pacientes
        FOREIGN KEY (id_paciente)
        REFERENCES pacientes(id_paciente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_turnos_medicos
        FOREIGN KEY (id_medico)
        REFERENCES medicos(id_medico)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_turnos_estados
        FOREIGN KEY (id_estado)
        REFERENCES estados_turno(id_estado)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_turnos_usuarios
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT uk_medico_fecha_hora
        UNIQUE (id_medico, fecha, hora)
);
