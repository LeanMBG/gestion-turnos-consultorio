use gestion_turnos;

select * from turnos where id_turno = 1;

UPDATE turnos
SET id_estado = 2, observacion = 'Cancelado por solicitud del paciente'
WHERE id_turno = 1;

/*verificacion*/
select * from turnos where id_turno = 1;

/*
prueba de eliminación de registros
*/

/*primero inserto prueba*/
INSERT INTO pacientes (nombre, apellido, dni, telefono, email) 
VALUES ('Paciente', 'Prueba', 39999111, 155007777, 'prueba@gmail.com');

/*verifico inserción y datos obtenido*/
SELECT * FROM pacientes;

/*luego elimino registro*/
DELETE FROM pacientes WHERE dni = 39999111;

/*verifico eliminación de los registros.*/
SELECT * FROM pacientes;
