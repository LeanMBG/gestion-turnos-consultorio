package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que representa un turno médico dentro del sistema.
 * Un turno vincula a un paciente, un médico, un estado y un usuario administrativo.
 */
public class Turno {

    // Atributos principales del turno.
    private int idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private String observacion;

    // Relaciones del turno con otras clases del modelo.
    private Paciente paciente;
    private Medico medico;
    private EstadoTurno estadoTurno;
    private UsuarioAdministrativo usuarioAdministrativo;

    /**
     * Constructor de la clase Turno.
     * Permite inicializar un turno con todos sus datos principales.
     */
    public Turno(int idTurno, LocalDate fecha, LocalTime hora, String observacion,
                 Paciente paciente, Medico medico, EstadoTurno estadoTurno,
                 UsuarioAdministrativo usuarioAdministrativo) {

        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.observacion = observacion;
        this.paciente = paciente;
        this.medico = medico;
        this.estadoTurno = estadoTurno;
        this.usuarioAdministrativo = usuarioAdministrativo;
    }

    // Métodos getter: permiten obtener los datos del turno.
    public int getIdTurno() {
        return idTurno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getObservacion() {
        return observacion;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public EstadoTurno getEstadoTurno() {
        return estadoTurno;
    }

    public UsuarioAdministrativo getUsuarioAdministrativo() {
        return usuarioAdministrativo;
    }

    // Métodos setter: permiten modificar los datos del turno.
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public void setUsuarioAdministrativo(UsuarioAdministrativo usuarioAdministrativo) {
        this.usuarioAdministrativo = usuarioAdministrativo;
    }

    /**
     * Cambia el estado del turno a Reservado.
     * Se utiliza para representar un turno confirmado o asignado.
     */
    public void confirmar() {
        this.estadoTurno = new EstadoTurno(1, "Reservado");
    }

    /**
     * Cambia el estado del turno a Cancelado.
     * No elimina el turno, sino que conserva el registro modificando su estado.
     */
    public void cancelar() {
        this.estadoTurno = new EstadoTurno(2, "Cancelado");
    }

    /**
     * Permite modificar fecha, hora y observación del turno.
     */
    public void modificar(LocalDate nuevaFecha, LocalTime nuevaHora, String nuevaObservacion) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.observacion = nuevaObservacion;
    }

    /**
     * Devuelve una representación en formato tipo texto del turno.
     * Este metodo se utiliza, por ejemplo, para mostrar los turnos en listas o en ComboBox.
     */
    @Override
    public String toString() {
        return "Turno #" + idTurno +
                " | Fecha: " + fecha +
                " | Hora: " + hora +
                " | Paciente: " + paciente.getNombreCompleto() +
                " | Médico: " + medico.getNombreCompleto() +
                " | Estado: " + estadoTurno.getEstado();
    }
}