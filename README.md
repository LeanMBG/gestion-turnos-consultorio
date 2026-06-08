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

- src
  - excepciones
  - modelo
  - principal
  - servicio
  - vista

- sql
  - scripts SQL de base de datos

- .gitignore
- README.md

## Paquetes Java

- modelo: contiene las clases principales del dominio del sistema.
- servicio: contiene la lógica principal del sistema.
- vista: contiene la interfaz gráfica desarrollada con Swing.
- excepciones: contiene las excepciones personalizadas.
- principal: contiene la clase Main, encargada de iniciar la aplicación.

## Ejecución del prototipo

Para ejecutar el proyecto:

1. Abrir el proyecto en IntelliJ IDEA.
2. Verificar que el JDK configurado sea Java 21.
3. Ejecutar la clase principal.Main.

## Base de datos

La carpeta sql contiene los scripts trabajados para la base de datos MySQL, incluyendo creación de tablas, inserción de datos de prueba y consultas SQL.

En esta etapa, el prototipo Java utiliza listas ArrayList para almacenar los datos durante la ejecución. La conexión completa con MySQL mediante JDBC queda prevista como mejora futura.

## Organización del código

El código se encuentra organizado en paquetes para separar responsabilidades:

- Las clases del dominio se encuentran en modelo.
- La lógica principal se encuentra en servicio.
- La interfaz gráfica se encuentra en vista.
- Las excepciones personalizadas se encuentran en excepciones.
- El inicio de la aplicación se encuentra en principal.

## Mejoras futuras

Algunas mejoras previstas para próximos incrementos son:

- Conexión completa con MySQL mediante JDBC.
- Persistencia permanente de datos.
- Login de usuario administrativo.
- Edición de pacientes, médicos y turnos.
- Filtros por médico, fecha, especialidad o estado del turno.
- Mejoras visuales en la interfaz gráfica.
- Selector visual de fechas.

## Autor

Leandro M. Barbera
