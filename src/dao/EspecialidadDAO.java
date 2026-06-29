package dao;

import modelo.Especialidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase encargada de gestionar las operaciones de la especialidad en la base de datos
 */
public class EspecialidadDAO implements RepositorioDAO<Especialidad> {

    /**
     * Inserta una nueva especialidad en la base de datos
     */
    @Override
    public void insertar(Especialidad especialidad) throws SQLException {

        String sql = "INSERT INTO especialidades (nombre, descripcion) VALUES (?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, especialidad.getNombre());
            stmt.setString(2, especialidad.getDescripcion());

            stmt.executeUpdate();
        }
    }

    /**
     * Listo todas las especialidades registradas en la base de datos
     */
    @Override
    public ArrayList<Especialidad> listar() throws SQLException {

        ArrayList<Especialidad> especialidades = new ArrayList<>();

        String sql = "SELECT id_especialidad, nombre, descripcion FROM especialidades";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Especialidad especialidad = new Especialidad(
                        rs.getInt("id_especialidad"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );

                especialidades.add(especialidad);
            }
        }

        return especialidades;
    }
}