package dao;

import modelo.Especialidad;
import modelo.Medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase encargada de gestionar las operaciones de médicos en la base de datos
 */
public class MedicoDAO implements RepositorioDAO<Medico> {

    /**
     * Inserto un nuevo médico en la base de datos
     */
    @Override
    public void insertar(Medico medico) throws SQLException {

        String sql = "INSERT INTO medicos (nombre, apellido, dni, matricula, telefono, id_especialidad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setInt(3, medico.getDni());
            stmt.setString(4, medico.getMatricula());
            stmt.setInt(5, medico.getTelefono());
            stmt.setInt(6, medico.getEspecialidad().getIdEspecialidad());

            stmt.executeUpdate();
        }
    }

    /**
     * Listo todos los médicos registrados en la base de datos, incluyendo su especialidad
     */
    @Override
    public ArrayList<Medico> listar() throws SQLException {

        ArrayList<Medico> medicos = new ArrayList<>();

        String sql = """
                SELECT 
                    m.id_medico,
                    m.nombre AS medico_nombre,
                    m.apellido AS medico_apellido,
                    m.dni AS medico_dni,
                    m.telefono AS medico_telefono,
                    m.matricula,
                    e.id_especialidad,
                    e.nombre AS especialidad_nombre,
                    e.descripcion AS especialidad_descripcion
                FROM medicos m
                INNER JOIN especialidades e ON m.id_especialidad = e.id_especialidad
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

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

                medicos.add(medico);
            }
        }

        return medicos;
    }
    /**
     * Verifica si existe un médico registrado con la matrícula previamente para no duplicar informacion
     */
    public boolean existeMatricula(String matricula) throws SQLException {

        String sql = "SELECT COUNT(*) AS cantidad FROM medicos WHERE matricula = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, matricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad") > 0;
                }
            }
        }

        return false;
    }
    /**
     * Verifica si existe un médico registrado con el DNI indicado para no duplicar información
     */
    public boolean existeDni(int dni) throws SQLException {

        String sql = "SELECT COUNT(*) AS cantidad FROM medicos WHERE dni = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, dni);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad") > 0;
                }
            }
        }

        return false;
    }
}