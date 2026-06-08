# Sistema de gestión de turnos para consultorio médico

Proyecto desarrollado para la asignatura Seminario de Práctica Informática.

## Descripción

Prototipo de aplicación de escritorio para la gestión de turnos en un consultorio médico.

El sistema permite registrar especialidades, pacientes y médicos, asignar turnos, consultar turnos registrados, buscar turnos por DNI del paciente y cancelar turnos activos.

## Tecnologías utilizadas

- Java 21
- Swing
- IntelliJ IDEA
- MySQL
- SQL

## Funcionalidades implementadas

- Registro de especialidades médicas.
- Registro de pacientes.
- Registro de médicos asociados a especialidades.
- Asignación de turnos.
- Validación de disponibilidad médica.
- Validación de fechas anteriores a la actual.
- Consulta de turnos en tabla.
- Búsqueda de turnos por DNI del paciente.
- Cancelación de turnos activos.
- Manejo de excepciones personalizadas.

## Estructura del proyecto

```text
src/
├── excepciones/
├── modelo/
├── principal/
├── servicio/
└── vista/

sql/
└── scripts de base de datos MySQL

docs/
└── documentación del trabajo práctico
