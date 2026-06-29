package dao;

import modelo.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase encargada de gestionar las operaciones de los pacientes contra la base de datos MySQL
 */
public class PacienteDAO implements RepositorioDAO<Paciente> {

    /**
     * Inserto un nuevo paciente en la base de datos
     */
    @Override
    public void insertar(Paciente paciente) throws SQLException {

        String sql = "INSERT INTO pacientes (nombre, apellido, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setInt(3, paciente.getDni());
            stmt.setInt(4, paciente.getTelefono());
            stmt.setString(5, paciente.getEmail());

            stmt.executeUpdate();
        }
    }

    /**
     * Listo todos los pacientes registrados en la base de datos
     */
    @Override
    public ArrayList<Paciente> listar() throws SQLException {

        ArrayList<Paciente> pacientes = new ArrayList<>();

        String sql = "SELECT id_paciente, nombre, apellido, dni, telefono, email FROM pacientes";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Paciente paciente = new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getInt("telefono"),
                        rs.getString("email")
                );

                pacientes.add(paciente);
            }
        }

        return pacientes;
    }

    /**
     * Busqueda de pacientes por DNI
     */
    public ArrayList<Paciente> buscarPorDni(int dni) throws SQLException {

        ArrayList<Paciente> pacientes = new ArrayList<>();

        String sql = "SELECT id_paciente, nombre, apellido, dni, telefono, email FROM pacientes WHERE dni = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, dni);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Paciente paciente = new Paciente(
                            rs.getInt("id_paciente"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getInt("dni"),
                            rs.getInt("telefono"),
                            rs.getString("email")
                    );

                    pacientes.add(paciente);
                }
            }
        }

        return pacientes;
    }

    /**
     * Verificacion de DNI duplicados, verifica si existe paciente registrado con el DNI
     */
    public boolean existeDni(int dni) throws SQLException {

        String sql = "SELECT COUNT(*) AS cantidad FROM pacientes WHERE dni = ?";

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