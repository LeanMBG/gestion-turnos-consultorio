# Sistema de gestión de turnos para consultorio médico

Proyecto desarrollado para la asignatura Seminario de Práctica Informática.

## Descripción

Este proyecto consiste en un prototipo de aplicación de escritorio para la gestión de turnos en un consultorio médico.

El sistema permite administrar especialidades médicas, pacientes, médicos y turnos. Además, permite asignar turnos, consultar los turnos registrados, buscar turnos por DNI del paciente y cancelar turnos activos.

El desarrollo fue realizado en Java aplicando conceptos de Programación Orientada a Objetos, como encapsulamiento, herencia, polimorfismo y abstracción.

## Tecnologías utilizadas

- Java 21
- Swing
- IntelliJ IDEA
- MySQL
- SQL

## Funcionalidades implementadas

- Registro de especialidades médicas.
- Registro de pacientes.
- Registro de médicos asociados a una especialidad.
- Asignación de turnos.
- Validación de disponibilidad médica.
- Validación para evitar fechas anteriores a la fecha actual.
- Consulta de turnos registrados en tabla.
- Búsqueda de turnos por DNI del paciente.
- Cancelación de turnos activos.
- Manejo de excepciones personalizadas.

## Estructura del repositorio

```text
gestion-turnos-consultorio/
├── src/
│   ├── excepciones/
│   ├── modelo/
│   ├── principal/
│   ├── servicio/
│   └── vista/
│
├── sql/
│   └── scripts SQL de base de datos
│
├── .gitignore
└── README.md
