package controlador;

import dao.EspecialidadDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.TurnoDAO;
import excepciones.DatoInvalidoException;
import excepciones.TurnoNoDisponibleException;
import archivos.ExportadorTurnosTXT;

import modelo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 * Controlador principal del sistema.
 * Actúa como intermediario entre la interfaz gráfica Swing y las clases DAO.
 *
 * Esta clase permite aplicar el patrón MVC, separando la vista, el modelo
 * y la lógica de coordinación del sistema.
 */
public class TurnoController {

    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private EspecialidadDAO especialidadDAO;
    private TurnoDAO turnoDAO;

    /**
     * Constructor del controlador.
     * Inicializa los DAO utilizados para acceder a la base de datos.
     */
    public TurnoController() {
        this.pacienteDAO = new PacienteDAO();
        this.medicoDAO = new MedicoDAO();
        this.especialidadDAO = new EspecialidadDAO();
        this.turnoDAO = new TurnoDAO();
    }

    /**
     * Registra un paciente en la base de datos.
     */
    public void registrarPaciente(String nombre, String apellido, int dni, int telefono, String email)
            throws DatoInvalidoException, SQLException {

        validarTexto(nombre, "El nombre del paciente es obligatorio.");
        validarTexto(apellido, "El apellido del paciente es obligatorio.");

        if (dni <= 0) {
            throw new DatoInvalidoException("El DNI debe ser mayor a cero.");
        }

        if (pacienteDAO.existeDni(dni)) {
            throw new DatoInvalidoException("Ya existe un paciente registrado con ese DNI.");
        }

        Paciente paciente = new Paciente(0, nombre, apellido, dni, telefono, email);
        pacienteDAO.insertar(paciente);
    }

    /**
     * Registra una especialidad médica en la base de datos.
     */
    public void registrarEspecialidad(String nombre, String descripcion)
            throws DatoInvalidoException, SQLException {

        validarTexto(nombre, "El nombre de la especialidad es obligatorio.");

        Especialidad especialidad = new Especialidad(0, nombre, descripcion);
        especialidadDAO.insertar(especialidad);
    }

    /**
     * Registra un médico en la base de datos.
     */
    public void registrarMedico(String nombre, String apellido, int dni, int telefono,
                                String matricula, Especialidad especialidad)
            throws DatoInvalidoException, SQLException {

        validarTexto(nombre, "El nombre del médico es obligatorio.");
        validarTexto(apellido, "El apellido del médico es obligatorio.");
        validarTexto(matricula, "La matrícula del médico es obligatoria.");

        if (dni <= 0) {
            throw new DatoInvalidoException("El DNI del médico debe ser mayor a cero.");
        }


        if (especialidad == null) {
            throw new DatoInvalidoException("Debe seleccionar una especialidad.");
        }

        if (medicoDAO.existeDni(dni)) {
            throw new DatoInvalidoException("Ya existe un médico registrado con ese DNI.");
        }

        if (medicoDAO.existeMatricula(matricula)) {
            throw new DatoInvalidoException("Ya existe un médico registrado con esa matrícula.");
        }

        Medico medico = new Medico(
                0,
                nombre,
                apellido,
                dni,
                telefono,
                matricula,
                especialidad
        );

        medicoDAO.insertar(medico);
    }

    /**
     * Asigna un turno en la base de datos.
     * Antes de guardar, valida los datos y verifica disponibilidad médica.
     */
    public void asignarTurno(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora, String observacion)
            throws DatoInvalidoException, TurnoNoDisponibleException, SQLException {

        if (paciente == null) {
            throw new DatoInvalidoException("Debe seleccionar un paciente.");
        }

        if (medico == null) {
            throw new DatoInvalidoException("Debe seleccionar un médico.");
        }

        if (fecha == null) {
            throw new DatoInvalidoException("Debe ingresar una fecha.");
        }

        if (fecha.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se pueden asignar turnos con fechas anteriores a la actual.");
        }

        if (hora == null) {
            throw new DatoInvalidoException("Debe ingresar una hora.");
        }

        boolean disponible = turnoDAO.verificarDisponibilidad(
                medico.getId(),
                fecha,
                hora
        );

        if (!disponible) {
            throw new TurnoNoDisponibleException("El médico no tiene disponibilidad en la fecha y hora seleccionadas.");
        }

        EstadoTurno estadoReservado = new EstadoTurno(1, "Reservado");

        UsuarioAdministrativo usuario = new UsuarioAdministrativo(
                1,
                "admin",
                "1234",
                "Leandro",
                "Barbera"
        );

        Turno turno = new Turno(
                0,
                fecha,
                hora,
                observacion,
                paciente,
                medico,
                estadoReservado,
                usuario
        );

        turnoDAO.insertar(turno);
    }

    /**
     * Lista los pacientes registrados en MySQL.
     */
    public ArrayList<Paciente> listarPacientes() throws SQLException {
        return pacienteDAO.listar();
    }

    /**
     * Lista las especialidades registradas en MySQL.
     */
    public ArrayList<Especialidad> listarEspecialidades() throws SQLException {
        return especialidadDAO.listar();
    }

    /**
     * Lista los médicos registrados en MySQL.
     */
    public ArrayList<Medico> listarMedicos() throws SQLException {
        return medicoDAO.listar();
    }

    /**
     * Lista los turnos registrados en MySQL.
     */
    public ArrayList<Turno> listarTurnos() throws SQLException {
        return turnoDAO.listar();
    }
    /**
     * Lista todos los turnos registrados en una fecha determinada.
     *
     * Este método permite que la vista consulte la agenda completa de un día,
     * delegando la búsqueda en la capa DAO.
     */
    public ArrayList<Turno> listarTurnosPorFecha(LocalDate fecha)
            throws DatoInvalidoException, SQLException {

        // Se valida que la fecha no sea nula.
        if (fecha == null) {
            throw new DatoInvalidoException("Debe ingresar una fecha.");
        }

        // Se solicita al DAO la consulta de turnos por fecha.
        return turnoDAO.listarPorFecha(fecha);
    }

    /**
     * Lista los turnos activos de una especialidad médica en una fecha determinada.
     * Este método se utiliza para consultar la agenda de una especialidad excluyendo los turnos cancelados.
     */
    public ArrayList<Turno> listarTurnosPorEspecialidadYFecha(Especialidad especialidad, LocalDate fecha)
            throws DatoInvalidoException, SQLException {

        // Se valida que exista una especialidad seleccionada.
        if (especialidad == null) {
            throw new DatoInvalidoException("Debe seleccionar una especialidad.");
        }

        // Se valida que la fecha no sea nula.
        if (fecha == null) {
            throw new DatoInvalidoException("Debe ingresar una fecha.");
        }

        // Se solicita al DAO la consulta filtrada por especialidad y fecha.
        return turnoDAO.buscarPorEspecialidadYFecha(
                especialidad.getIdEspecialidad(),
                fecha
        );
    }

    /**
     * Busqueda de turnos por DNI de paciente
     */
    public ArrayList<Turno> buscarTurnosPorDni(int dni) throws SQLException {
        return turnoDAO.buscarPorDniPaciente(dni);
    }

    /**
     * Cancela un turno, modifica el estado en Base de Datos
     */
    public void cancelarTurno(int idTurno) throws SQLException {
        turnoDAO.cancelarTurno(idTurno);
    }
    /**
     * Exporta a un archivo TXT los turnos activos de una especialidad en una fecha determinada.
     */
    public String exportarTurnosPorEspecialidadYFecha(Especialidad especialidad, LocalDate fecha)
            throws DatoInvalidoException, SQLException, IOException {

        if (especialidad == null) {
            throw new DatoInvalidoException("Debe seleccionar una especialidad.");
        }

        if (fecha == null) {
            throw new DatoInvalidoException("Debe ingresar una fecha.");
        }

        ArrayList<Turno> turnos = turnoDAO.buscarPorEspecialidadYFecha(
                especialidad.getIdEspecialidad(),
                fecha
        );

        if (turnos.isEmpty()) {
            throw new DatoInvalidoException("No se encontraron turnos activos para la especialidad y fecha seleccionadas.");
        }

        String nombreEspecialidad = especialidad.getNombre()
                .replaceAll("[^a-zA-Z0-9]", "_");

        String nombreArchivo = "turnos_" + nombreEspecialidad + "_" + fecha + ".txt";

        ExportadorTurnosTXT.exportarTurnos(
                turnos,
                nombreArchivo,
                especialidad.getNombre(),
                fecha
        );

        return new File(nombreArchivo).getAbsolutePath();
    }
    /**
     * Valido que un texto obligatorio no sea nulo ni vacio
     */
    private void validarTexto(String texto, String mensajeError) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensajeError);
        }
    }
}