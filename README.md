# Sistema de gestión de turnos para consultorio médico

Proyecto desarrollado para la asignatura **Seminario de Práctica Informática**.

## Descripción

Este proyecto consiste en un prototipo de aplicación de escritorio para la gestión de turnos en un consultorio médico.

El sistema permite administrar especialidades médicas, pacientes, médicos y turnos. Además, permite asignar turnos, consultar turnos registrados, buscar turnos por DNI del paciente, cancelar turnos activos, filtrar turnos por fecha y especialidad, y exportar una agenda médica diaria a un archivo TXT.

El desarrollo fue realizado en Java aplicando conceptos de Programación Orientada a Objetos, como encapsulamiento, herencia, polimorfismo y abstracción. También se incorporó persistencia de datos mediante MySQL, conexión JDBC y organización del código siguiendo el patrón MVC.

## Tecnologías utilizadas

* Java 21
* Swing
* MySQL
* JDBC
* Maven
* MySQL Connector/J
* IntelliJ IDEA
* GitHub

## Funcionalidades implementadas

* Registro de especialidades médicas.
* Registro de pacientes.
* Registro de médicos asociados a una especialidad.
* Asignación de turnos.
* Validación de disponibilidad médica.
* Validación para evitar fechas anteriores a la fecha actual.
* Validación de DNI duplicado en pacientes.
* Validación de DNI y matrícula duplicada en médicos.
* Consulta general de turnos.
* Consulta de turnos por fecha.
* Consulta de turnos activos por especialidad y fecha.
* Búsqueda de turnos por DNI del paciente.
* Cancelación de turnos activos.
* Exportación de agenda médica diaria a archivo TXT.
* Manejo de excepciones personalizadas.
* Persistencia de datos en MySQL mediante JDBC.

## Estructura del repositorio

```text
gestion-turnos-consultorio/
├── src/
│   ├── archivos/
│   ├── controlador/
│   ├── dao/
│   ├── excepciones/
│   ├── modelo/
│   ├── principal/
│   ├── servicio/
│   └── vista/
├── sql/
├── .gitignore
├── README.md
└── pom.xml
```

## Organización del código

El código se encuentra organizado en paquetes para separar responsabilidades:

* `modelo`: contiene las clases principales del dominio del sistema, como Paciente, Médico, Turno, Especialidad, EstadoTurno y UsuarioAdministrativo.
* `vista`: contiene la interfaz gráfica desarrollada con Swing.
* `controlador`: contiene la clase TurnoController, encargada de comunicar la vista con la lógica y el acceso a datos.
* `dao`: contiene las clases encargadas de acceder a la base de datos MySQL mediante JDBC.
* `archivos`: contiene la clase responsable de exportar turnos a un archivo TXT.
* `excepciones`: contiene las excepciones personalizadas utilizadas para controlar errores del sistema.
* `principal`: contiene la clase Main, encargada de iniciar la aplicación.
* `servicio`: contiene la clase SistemaTurnos, utilizada en el prototipo inicial con almacenamiento en memoria mediante ArrayList.

## Patrón de diseño utilizado

El proyecto aplica el patrón **MVC**:

* **Modelo:** clases del paquete `modelo` y clases DAO encargadas del acceso a datos.
* **Vista:** clase `VentanaPrincipal`, desarrollada con Swing.
* **Controlador:** clase `TurnoController`, que recibe las acciones de la vista y delega las operaciones necesarias.

Esta organización permite separar responsabilidades, mejorar la mantenibilidad del código y facilitar futuras modificaciones del sistema.

## Base de datos

El sistema utiliza una base de datos MySQL llamada `gestion_turnos`.

La carpeta `sql` contiene los scripts necesarios para crear la base de datos, crear las tablas, insertar datos de prueba y realizar consultas.

Tablas principales:

* `pacientes`
* `medicos`
* `especialidades`
* `turnos`
* `estados_turno`
* `usuarios`

## Persistencia de datos

La persistencia se realiza mediante JDBC.

La clase `ConexionBD` centraliza la conexión a MySQL. Las clases DAO utilizan `Connection`, `PreparedStatement` y `ResultSet` para ejecutar operaciones sobre la base de datos.

El sistema permite guardar, consultar, filtrar y actualizar turnos directamente en MySQL.

## Manejo de archivos

El sistema incorpora una funcionalidad de exportación a archivo TXT.

Esta opción permite generar una agenda médica diaria filtrada por especialidad y fecha. El archivo generado incluye información útil para el médico, como horario, paciente, DNI, teléfono y observación del turno.

## Ejecución del proyecto

Para ejecutar el proyecto:

1. Abrir el proyecto en IntelliJ IDEA.
2. Verificar que el JDK configurado sea Java 21.
3. Crear la base de datos ejecutando los scripts de la carpeta `sql`.
4. Verificar que MySQL esté iniciado.
5. Ejecutar la clase `principal.Main`.

## Dependencias

El proyecto utiliza Maven para administrar la dependencia del conector MySQL.

Dependencia principal:

* MySQL Connector/J

## Mejoras futuras

Algunas mejoras posibles para futuros incrementos son:

* Login completo de usuario administrativo.
* Edición de pacientes, médicos y turnos.
* Selector visual de fechas.
* Mejora visual de la interfaz gráfica.
* Generación de reportes en PDF.
* Gestión de usuarios con distintos perfiles.

## Autor

Leandro M. Barbera
