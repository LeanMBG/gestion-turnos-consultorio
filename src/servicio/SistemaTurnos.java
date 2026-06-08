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
 * Clase de servicio contiene la lógica principal del sistema de turnos
 * Administra pacientes, médicos, especialidades, turnos, estados y usuarios administrativos
 */
public class SistemaTurnos {

    /*
     * Estructuras de datos usadas para almacenar temporalmente la información.
     * En una etapa posterior, estos datos podrían persistirse en MySQL...
     */
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Especialidad> especialidades;
    private ArrayList<Turno> turnos;
    private ArrayList<EstadoTurno> estadosTurno;
    private ArrayList<UsuarioAdministrativo> usuarios;

    /*
     * Contadores usados para generar identificadores internos.
     * Simulan comportamiento de claves autoincrementales de una BD
     */
    private int contadorPacientes;
    private int contadorMedicos;
    private int contadorEspecialidades;
    private int contadorTurnos;
    private int contadorUsuarios;

    /**
     * Constructor del sistema
     * Inicializa las listas, los contadores y carga datos básicos necesarios, como los estados iniciales de los turnos y un usuario administrativo.
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
     * Carga los estados iniciales que puede tener un turno
     */
    private void cargarEstadosIniciales() {
        estadosTurno.add(new EstadoTurno(1, "Reservado"));
        estadosTurno.add(new EstadoTurno(2, "Cancelado"));
        estadosTurno.add(new EstadoTurno(3, "Atendido"));
    }

    /**
     * Carga un usuario administrativo inicial para operar el sistema.
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
     * Registra un nuevo paciente y valida datos obligatorios
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
     * Registra una nueva especialidad médica
     */
    public Especialidad registrarEspecialidad(String nombre, String descripcion)
            throws DatoInvalidoException {

        validarTexto(nombre, "El nombre de la especialidad es obligatorio.");

        Especialidad especialidad = new Especialidad(contadorEspecialidades++, nombre, descripcion);
        especialidades.add(especialidad);

        return especialidad;
    }

    /**
     * Registra un nuevo médico asociado a una especialidad existente
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
     * Asigna un turno a un paciente con un médico en una fecha y hora determinadas
     * Antes de registrar el turno, valida datos obligatorios, fecha, disponibilidad
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

        // no se permiten turnos con fechas anteriores al día actual.
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se pueden asignar turnos con fechas anteriores a la fecha actual.");
        }

        if (hora == null) {
            throw new DatoInvalidoException("Debe seleccionar una hora.");
        }

        if (usuario == null) {
            throw new DatoInvalidoException("Debe existir un usuario administrativo.");
        }

        // Verifica que el médico no tenga otro turno activo en la misma fecha y hora.
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
     * Verifica si un médico está disponible en una fecha y hora determinada
     * Si existe un turno no cancelado para ese médico en ese horario, devuelve un false y no permite asignarlo.
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
     * Cancela un turno existente a partir del ID. No elimina el turno, cambia el estado a "Cancelado"
     */
    public void cancelarTurno(int idTurno) throws RegistroNoEncontradoException {

        Turno turno = buscarTurnoPorId(idTurno);

        if (turno == null) {
            throw new RegistroNoEncontradoException("No se encontró un turno con el ID indicado.");
        }

        turno.cancelar();
    }

    /**
     * Busca un turno por su identificador
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
     * Busca todos los turnos asociados a un paciente según su DNI.
     * Recorre la lista de turnos y devuelve aquellos que coinciden con el DNI
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
     * Ordena los turnos por fecha y hora. Aplica algoritmo de ordenamiento usando Comparator.
     */
    public void ordenarTurnosPorFechaYHora() {
        turnos.sort(
                Comparator.comparing(Turno::getFecha)
                        .thenComparing(Turno::getHora)
        );
    }

    /**
     * Busca un estado de turno por la descripción
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
     * Validación de texto obligatorio no sea nulo ni vacío.
     */
    private void validarTexto(String texto, String mensajeError) throws DatoInvalidoException {

        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensajeError);
        }
    }

    /*
     * Métodos getter.
     * Estos me permiten consultar las listas desde la interfaz sin exponer directamente la modificación interna de la lógica del sistema.
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
     * Devuelve el primer usuario administrativo disponible
     * En este prototipo se utiliza un usuario inicial cargado automáticamente...
     */
    public UsuarioAdministrativo getUsuarioAdministrador() {
        if (usuarios.isEmpty()) {
            return null;
        }
        return usuarios.get(0);
    }
}