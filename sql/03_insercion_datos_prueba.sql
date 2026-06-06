USE gestion_turnos;

/*
Inserción de datos basicos y su verificación
*/

INSERT INTO estados_turno (estado) VALUES
('Reservado'),
('Cancelado'),
('Atendido');

SELECT * FROM estados_turno;

/*inserto especialdiades*/
INSERT INTO especialidades (nombre, descripcion) 
VALUES
('Clínica médica', 'Atención médica general'),
('Pediatría', 'Atención médica de niños'),
('Cardiología', 'Atención relacionada al sistema cardiovascular');
/*veirfico inserción*/
SELECT * FROM especialidades;

/*inserto pacientes*/
INSERT INTO pacientes (nombre, apellido, dni, telefono, email)
VALUES
('Juan', 'Visnur', 30111222, 34222333, 'juanitoOk@gmail.com'),
('Eliot', 'Gomez', 28999888, 1002222, 'eliot.gomez@gmail.com'),
('Esteban', 'Lopez', 33444555, 155003333, 'esteban_lopez@gmail.com');

/*veirfico inserción*/
SELECT * FROM pacientes;


/*inserto medicos*/
INSERT INTO medicos (nombre, apellido, matricula, telefono, id_especialidad) 
VALUES
('Ana', 'Martínez', 'MP-1001', 155004444, 1),
('Roberto', 'Fernández', 'MP-1002', 155005555, 2),
('Lucía', 'Sosa', 'MP-1003', 155006666, 3);


/*veirfico inserción*/
SELECT * FROM medicos;

/*veirfico con especialidades medicas*/
SELECT m.id_medico, m.nombre, m.apellido, m.matricula, e.nombre AS especialidad
FROM medicos m
INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad;


/*insercion de usuario admin*/
INSERT INTO usuarios (usuario, clave, nombre, apellido) 
VALUES
('admin', '1234', 'Leandro', 'Barbera');

/*verificacion de inserción*/
SELECT * FROM usuarios;

/*inserción de turnos médicos*/
INSERT INTO turnos (fecha, hora, observacion, id_paciente, id_medico, id_estado, id_usuario)
VALUES
('2026-05-20', '09:00:00', 'Primer consulta', 1, 1, 1, 1),
('2026-05-30', '10:00:00', 'Control pediatrico', 2, 2, 1, 1),
('2026-05-18', '11:30:00', 'Consulta cardiologica', 3, 3, 1, 1);

/*verificacion de inserción*/
SELECT * FROM turnos;
