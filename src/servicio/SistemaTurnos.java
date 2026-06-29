package servicio;

import excepciones.DatoInvalidoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.TurnoNoDisponibleException;
import modelo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Clase de servicio utilizada en el prototipo inicial del sistema
 *
 * En esta clase se administra la información en memoria mediante listas ArrayList.
 * Permite registrar pacientes, médicos, especialidades y turnos sin conectarse directamente a la base de datos.
 *
 * En el incremento actual del proyecto, la persistencia real se realiza mediante las clases DAO y la conexión con MySQL.
 */
public class SistemaTurnos {

    /*
     * Listas utilizadas para almacenar temporalmente los datos durante la ejecución.
     * Esta estructura fue útil para el primer prototipo, antes de incorporar MySQL.
     */
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Especialidad> especialidades;
    private ArrayList<Turno> turnos;
    private ArrayList<EstadoTurno> estadosTurno;
    private ArrayList<UsuarioAdministrativo> usuarios;

    /*
     * Contadores internos para generar identificadores dentro del prototipo en memoria.
     * Funcionan de manera similar a un AUTO_INCREMENT, pero solo mientras se ejecuta la aplicación.
     */
    private int contadorPacientes;
    private int contadorMedicos;
    private int contadorEspecialidades;
    private int contadorTurnos;
    private int contadorUsuarios;

    /**
     * Constructor del sistema.
     * Inicializa las listas, los contadores y carga datos básicos para poder trabajarlos
     */
    public SistemaTurnos() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.especialidades = new ArrayList<>();
        this.turnos = new ArrayList<>();
        this.estadosTurno = new ArrayList<>();
        this.usuarios = new ArrayList<>();

        this.contadorPacientes = 1;
        this.contadorMedicos = 1;
        this.contadorEspecialidades = 1;
        this.contadorTurnos = 1;
        this.contadorUsuarios = 1;

        cargarEstadosIniciales();
        cargarUsuarioInicial();
    }

    /**
     * Carga los estados básicos que puede tener un turno
     */
    private void cargarEstadosIniciales() {
        estadosTurno.add(new EstadoTurno(1, "Reservado"));
        estadosTurno.add(new EstadoTurno(2, "Cancelado"));
        estadosTurno.add(new EstadoTurno(3, "Atendido"));
    }

    /**
     * Carga un usuario administrativo inicial para operar el prototipo
     */
    private void cargarUsuarioInicial() {
        usuarios.add(new UsuarioAdministrativo(
                contadorUsuarios++,
                "admin",
                "1234",
                "Leandro",
                "Barbera"
        ));
    }

    /**
     * Registro de paciente nuevo en la lista de pacientes
     * Antes de guardarlo, valida que los datos obligatorios estén completos
     */
    public Paciente registrarPaciente(String nombre, String apellido, int dni, int telefono, String email)
            throws DatoInvalidoException {

        validarTexto(nombre, "El nombre del paciente es obligatorio.");
        validarTexto(apellido, "El apellido del paciente es obligatorio.");

        if (dni <= 0) {
            throw new DatoInvalidoException("El DNI debe ser mayor a cero.");
        }

        Paciente paciente = new Paciente(contadorPacientes++, nombre, apellido, dni, telefono, email);
        pacientes.add(paciente);

        return paciente;
    }

    /**
     * Registro de nueva especialidad médica en la lista de especialidades
     */
    public Especialidad registrarEspecialidad(String nombre, String descripcion)
            throws DatoInvalidoException {

        validarTexto(nombre, "El nombre de la especialidad es obligatorio.");

        Especialidad especialidad = new Especialidad(contadorEspecialidades++, nombre, descripcion);
        especialidades.add(especialidad);

        return especialidad;
    }

    /**
     * Registro de un médico nuevo y lo asocia a una especialidad existente
     */
    public Medico registrarMedico(String nombre, String apellido, int dni, int telefono,
                                  String matricula, Especialidad especialidad)
            throws DatoInvalidoException {

        validarTexto(nombre, "El nombre del médico es obligatorio.");
        validarTexto(apellido, "El apellido del médico es obligatorio.");
        validarTexto(matricula, "La matrícula del médico es obligatoria.");

        if (especialidad == null) {
            throw new DatoInvalidoException("Debe seleccionar una especialidad.");
        }

        Medico medico = new Medico(
                contadorMedicos++,
                nombre,
                apellido,
                dni,
                telefono,
                matricula,
                especialidad
        );

        medicos.add(medico);

        return medico;
    }

    /**
     * Asigno un turno a un paciente con un médico en una fecha y hora determinadas
     * Antes de registrar el turno, valida los datos recibidos y verifica la disponibilidad
     */
    public Turno asignarTurno(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora,
                              String observacion, UsuarioAdministrativo usuario)
            throws DatoInvalidoException, TurnoNoDisponibleException {

        if (paciente == null) {
            throw new DatoInvalidoException("Debe seleccionar un paciente.");
        }

        if (medico == null) {
            throw new DatoInvalidoException("Debe seleccionar un médico.");
        }

        if (fecha == null) {
            throw new DatoInvalidoException("Debe seleccionar una fecha.");
        }

        // No se permiten turnos con fechas anteriores al día actual.
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se pueden asignar turnos con fechas anteriores a la fecha actual.");
        }

        if (hora == null) {
            throw new DatoInvalidoException("Debe seleccionar una hora.");
        }

        if (usuario == null) {
            throw new DatoInvalidoException("Debe existir un usuario administrativo.");
        }

        // Se verifica que el médico no tenga otro turno activo en el mismo día y horario. Eivta superposicion.
        if (!verificarDisponibilidad(medico, fecha, hora)) {
            throw new TurnoNoDisponibleException("El médico no tiene disponibilidad en la fecha y hora seleccionadas.");
        }

        EstadoTurno estadoReservado = obtenerEstadoPorNombre("Reservado");

        Turno turno = new Turno(
                contadorTurnos++,
                fecha,
                hora,
                observacion,
                paciente,
                medico,
                estadoReservado,
                usuario
        );

        turnos.add(turno);

        return turno;
    }

    /**
     * Verifico si un médico está disponible para una fecha y hora determinadas
     * Si existe un turno no cancelado para ese médico, devuelve false y no será almacenado un nuevo turno
     */
    public boolean verificarDisponibilidad(Medico medico, LocalDate fecha, LocalTime hora) {

        for (Turno turno : turnos) {
            boolean mismoMedico = turno.getMedico().getId() == medico.getId();
            boolean mismaFecha = turno.getFecha().equals(fecha);
            boolean mismaHora = turno.getHora().equals(hora);
            boolean noCancelado = !turno.getEstadoTurno().getEstado().equalsIgnoreCase("Cancelado");

            if (mismoMedico && mismaFecha && mismaHora && noCancelado) {
                return false;
            }
        }

        return true;
    }

    /**
     * Cancela un turno existente.
     * El turno no se elimina, solo cambia su estado a "Cancelado"
     */
    public void cancelarTurno(int idTurno) throws RegistroNoEncontradoException {

        Turno turno = buscarTurnoPorId(idTurno);

        if (turno == null) {
            throw new RegistroNoEncontradoException("No se encontró un turno con el ID indicado.");
        }

        turno.cancelar();
    }

    /**
     * Busco un turno por su identificador
     */
    public Turno buscarTurnoPorId(int idTurno) {

        for (Turno turno : turnos) {
            if (turno.getIdTurno() == idTurno) {
                return turno;
            }
        }

        return null;
    }

    /**
     * Busco todos los turnos asociados a un paciente según su DNI
     * Recorre la lista de turnos, y devuelve todas las coincidencias encontradas
     */
    public ArrayList<Turno> buscarTurnosPorDniPaciente(int dni) {

        ArrayList<Turno> resultado = new ArrayList<>();

        for (Turno turno : turnos) {
            if (turno.getPaciente().getDni() == dni) {
                resultado.add(turno);
            }
        }

        return resultado;
    }

    /**
     * Ordena los turnos por fecha y hora. Se utiliza Comparator para organizar la lista de turnos
     */
    public void ordenarTurnosPorFechaYHora() {
        turnos.sort(
                Comparator.comparing(Turno::getFecha)
                        .thenComparing(Turno::getHora)
        );
    }

    /**
     * Busca un estado de turno por su descripción
     */
    private EstadoTurno obtenerEstadoPorNombre(String estadoBuscado) {

        for (EstadoTurno estado : estadosTurno) {
            if (estado.getEstado().equalsIgnoreCase(estadoBuscado)) {
                return estado;
            }
        }

        return null;
    }

    /**
     * Valido que un texto obligatorio no esté vacío
     */
    private void validarTexto(String texto, String mensajeError) throws DatoInvalidoException {

        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensajeError);
        }
    }

    /*
     * Métodos getter
     * Permiten consultar las listas desde otras partes del sistema
     */

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    public ArrayList<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public ArrayList<Turno> getTurnos() {
        return turnos;
    }

    public ArrayList<EstadoTurno> getEstadosTurno() {
        return estadosTurno;
    }

    public ArrayList<UsuarioAdministrativo> getUsuarios() {
        return usuarios;
    }

    /**
     * Devuelve el primer usuario administrativo disponible.
     * En este prototipo se utiliza un usuario inicial cargado automáticamente
     */
    public UsuarioAdministrativo getUsuarioAdministrador() {
        if (usuarios.isEmpty()) {
            return null;
        }
        return usuarios.get(0);
    }
}