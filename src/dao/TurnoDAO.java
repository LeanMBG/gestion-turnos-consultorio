package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Clase encargada de gestionar las operaciones de turnoscontra la base de datos
 */
public class TurnoDAO {

    /**
     * Inserto un nuevo turno en la base de datos
     */
    public void insertar(Turno turno) throws SQLException {

        String sql = """
                INSERT INTO turnos 
                (fecha, hora, observacion, id_paciente, id_medico, id_estado, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(turno.getFecha()));
            stmt.setTime(2, Time.valueOf(turno.getHora()));
            stmt.setString(3, turno.getObservacion());
            stmt.setInt(4, turno.getPaciente().getId());
            stmt.setInt(5, turno.getMedico().getId());
            stmt.setInt(6, turno.getEstadoTurno().getIdEstado());
            stmt.setInt(7, turno.getUsuarioAdministrativo().getIdUsuario());

            stmt.executeUpdate();
        }
    }

    /**
     * Listo todos los turnos registrados en la base de datos
     */
    public ArrayList<Turno> listar() throws SQLException {

        ArrayList<Turno> turnos = new ArrayList<>();

        String sql = """
                SELECT
                    t.id_turno,
                    t.fecha,
                    t.hora,
                    t.observacion,

                    p.id_paciente,
                    p.nombre AS paciente_nombre,
                    p.apellido AS paciente_apellido,
                    p.dni AS paciente_dni,
                    p.telefono AS paciente_telefono,
                    p.email AS paciente_email,

                    m.id_medico,
                    m.nombre AS medico_nombre,
                    m.apellido AS medico_apellido,
                    m.dni AS medico_dni,
                    m.telefono AS medico_telefono,
                    m.matricula,

                    e.id_especialidad,
                    e.nombre AS especialidad_nombre,
                    e.descripcion AS especialidad_descripcion,

                    et.id_estado,
                    et.estado,

                    u.id_usuario,
                    u.usuario,
                    u.clave,
                    u.nombre AS usuario_nombre,
                    u.apellido AS usuario_apellido

                FROM turnos t
                INNER JOIN pacientes p ON t.id_paciente = p.id_paciente
                INNER JOIN medicos m ON t.id_medico = m.id_medico
                INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad
                INNER JOIN estados_turno et ON t.id_estado = et.id_estado
                INNER JOIN usuarios u ON t.id_usuario = u.id_usuario
                ORDER BY t.fecha, t.hora
                """;
        /**
         * se utiliza try-with-resources para abrir la conexión, preparar la consulta y ejecutar el resultado
         * Este bloque asegura que la conexión, el PreparedStatement y el ResultSet se cierren automáticamente al finalizar
         * incluso si ocurre un error de base de datos
         * */
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                turnos.add(construirTurnoDesdeResultSet(rs));
            }
        }

        return turnos;
    }

    /**
     * Busca turnos asociados al DNI de un paciente
     */
    public ArrayList<Turno> buscarPorDniPaciente(int dni) throws SQLException {

        ArrayList<Turno> turnos = new ArrayList<>();

        String sql = """
                SELECT
                    t.id_turno,
                    t.fecha,
                    t.hora,
                    t.observacion,

                    p.id_paciente,
                    p.nombre AS paciente_nombre,
                    p.apellido AS paciente_apellido,
                    p.dni AS paciente_dni,
                    p.telefono AS paciente_telefono,
                    p.email AS paciente_email,

                    m.id_medico,
                    m.nombre AS medico_nombre,
                    m.apellido AS medico_apellido,
                    m.dni AS medico_dni,
                    m.telefono AS medico_telefono,
                    m.matricula,

                    e.id_especialidad,
                    e.nombre AS especialidad_nombre,
                    e.descripcion AS especialidad_descripcion,

                    et.id_estado,
                    et.estado,

                    u.id_usuario,
                    u.usuario,
                    u.clave,
                    u.nombre AS usuario_nombre,
                    u.apellido AS usuario_apellido

                FROM turnos t
                INNER JOIN pacientes p ON t.id_paciente = p.id_paciente
                INNER JOIN medicos m ON t.id_medico = m.id_medico
                INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad
                INNER JOIN estados_turno et ON t.id_estado = et.id_estado
                INNER JOIN usuarios u ON t.id_usuario = u.id_usuario
                WHERE p.dni = ?
                ORDER BY t.fecha, t.hora
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, dni);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turnos.add(construirTurnoDesdeResultSet(rs));
                }
            }
        }

        return turnos;
    }

    /**
     * Verifica si un médico se encuentra disponible en una fecha y hora determinada
     * Los turnos cancelados no bloquean la disponibilidad para poder tomar fecha y hora en que se encontraba
     */
    public boolean verificarDisponibilidad(int idMedico, java.time.LocalDate fecha, java.time.LocalTime hora) throws SQLException {

        String sql = """
                SELECT COUNT(*) AS cantidad
                FROM turnos
                WHERE id_medico = ?
                AND fecha = ?
                AND hora = ?
                AND id_estado <> 2
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idMedico);
            stmt.setDate(2, Date.valueOf(fecha));
            stmt.setTime(3, Time.valueOf(hora));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad") == 0;
                }
            }
        }

        return false;
    }

    /**
     * Cancela un turno modificando su estado a Cancelado
     */
    public void cancelarTurno(int idTurno) throws SQLException {

        String sql = "UPDATE turnos SET id_estado = 2 WHERE id_turno = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idTurno);
            stmt.executeUpdate();
        }
    }
    /**
     * Busca turnos activos por especialidad médica y fecha.
     * Excluyo los turnos cancelados para mostrar solo turnos vigentes.
     */
    public ArrayList<Turno> buscarPorEspecialidadYFecha(int idEspecialidad, java.time.LocalDate fecha) throws SQLException {

        ArrayList<Turno> turnos = new ArrayList<>();

        String sql = """
            SELECT
                t.id_turno,
                t.fecha,
                t.hora,
                t.observacion,

                p.id_paciente,
                p.nombre AS paciente_nombre,
                p.apellido AS paciente_apellido,
                p.dni AS paciente_dni,
                p.telefono AS paciente_telefono,
                p.email AS paciente_email,

                m.id_medico,
                m.nombre AS medico_nombre,
                m.apellido AS medico_apellido,
                m.dni AS medico_dni,
                m.telefono AS medico_telefono,
                m.matricula,

                e.id_especialidad,
                e.nombre AS especialidad_nombre,
                e.descripcion AS especialidad_descripcion,

                et.id_estado,
                et.estado,

                u.id_usuario,
                u.usuario,
                u.clave,
                u.nombre AS usuario_nombre,
                u.apellido AS usuario_apellido

            FROM turnos t
            INNER JOIN pacientes p ON t.id_paciente = p.id_paciente
            INNER JOIN medicos m ON t.id_medico = m.id_medico
            INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad
            INNER JOIN estados_turno et ON t.id_estado = et.id_estado
            INNER JOIN usuarios u ON t.id_usuario = u.id_usuario
            WHERE e.id_especialidad = ?
            AND t.fecha = ?
            AND t.id_estado <> 2
            ORDER BY m.apellido, m.nombre, t.hora
            """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idEspecialidad);
            stmt.setDate(2, Date.valueOf(fecha));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turnos.add(construirTurnoDesdeResultSet(rs));
                }
            }
        }

        return turnos;
    }
    /**
     * Lista todos los turnos registrados en una fecha determinada.
     * Este método se utiliza para consultar la agenda completa de un día.
     * No excluye turnos cancelados. Permite visualizar el estado real de cada turno registrado en esa fecha.
     */
    public ArrayList<Turno> listarPorFecha(java.time.LocalDate fecha) throws SQLException {

        // Lista donde se almacenarán los turnos obtenidos desde MySQL.
        ArrayList<Turno> turnos = new ArrayList<>();

        /*
         * Consulta SQL. Se recuperan datos del turno, paciente, médico, especialidad, estado del turno y usuario administrativo.
         */
        String sql = """
            SELECT
                t.id_turno,
                t.fecha,
                t.hora,
                t.observacion,

                p.id_paciente,
                p.nombre AS paciente_nombre,
                p.apellido AS paciente_apellido,
                p.dni AS paciente_dni,
                p.telefono AS paciente_telefono,
                p.email AS paciente_email,

                m.id_medico,
                m.nombre AS medico_nombre,
                m.apellido AS medico_apellido,
                m.dni AS medico_dni,
                m.telefono AS medico_telefono,
                m.matricula,

                e.id_especialidad,
                e.nombre AS especialidad_nombre,
                e.descripcion AS especialidad_descripcion,

                et.id_estado,
                et.estado,

                u.id_usuario,
                u.usuario,
                u.clave,
                u.nombre AS usuario_nombre,
                u.apellido AS usuario_apellido

            FROM turnos t
            INNER JOIN pacientes p ON t.id_paciente = p.id_paciente
            INNER JOIN medicos m ON t.id_medico = m.id_medico
            INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad
            INNER JOIN estados_turno et ON t.id_estado = et.id_estado
            INNER JOIN usuarios u ON t.id_usuario = u.id_usuario
            WHERE t.fecha = ?
            ORDER BY t.hora
            """;

        /*
         * try-with-resources cierra automáticamente la conexión, el PreparedStatement y el ResultSet.
         */
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            // Se asigna la fecha ingresada por el usuario al parámetro de la consulta
            stmt.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = stmt.executeQuery()) {

                // Recorrido de resultados y conversión de objetos Turno
                while (rs.next()) {
                    turnos.add(construirTurnoDesdeResultSet(rs));
                }
            }
        }

        // Devuelve la lista de turnos encontrados
        return turnos;
    }
    /**
     * Construye objeto Turno a partir de un ResultSet
     * Evito repetir código en las consultas de listado y búsqueda
     */
    private Turno construirTurnoDesdeResultSet(ResultSet rs) throws SQLException {

        Paciente paciente = new Paciente(
                rs.getInt("id_paciente"),
                rs.getString("paciente_nombre"),
                rs.getString("paciente_apellido"),
                rs.getInt("paciente_dni"),
                rs.getInt("paciente_telefono"),
                rs.getString("paciente_email")
        );

        Especialidad especialidad = new Especialidad(
                rs.getInt("id_especialidad"),
                rs.getString("especialidad_nombre"),
                rs.getString("especialidad_descripcion")
        );

        Medico medico = new Medico(
                rs.getInt("id_medico"),
                rs.getString("medico_nombre"),
                rs.getString("medico_apellido"),
                rs.getInt("medico_dni"),
                rs.getInt("medico_telefono"),
                rs.getString("matricula"),
                especialidad
        );

        EstadoTurno estadoTurno = new EstadoTurno(
                rs.getInt("id_estado"),
                rs.getString("estado")
        );

        UsuarioAdministrativo usuario = new UsuarioAdministrativo(
                rs.getInt("id_usuario"),
                rs.getString("usuario"),
                rs.getString("clave"),
                rs.getString("usuario_nombre"),
                rs.getString("usuario_apellido")
        );

        return new Turno(
                rs.getInt("id_turno"),
                rs.getDate("fecha").toLocalDate(),
                rs.getTime("hora").toLocalTime(),
                rs.getString("observacion"),
                paciente,
                medico,
                estadoTurno,
                usuario
        );
    }
}