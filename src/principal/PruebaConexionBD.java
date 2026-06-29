package principal;

import dao.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase utilizada para probar la conexión entre Java y MySQL
 */
public class PruebaConexionBD {

    public static void main(String[] args) {

        try {
            Connection conexion = ConexionBD.obtenerConexion();

            if (conexion != null) {
                System.out.println("Conexión exitosa a la base de datos gestion_turnos.");
                conexion.close();
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}