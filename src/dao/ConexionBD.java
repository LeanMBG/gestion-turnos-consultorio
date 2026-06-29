package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
* Clase para establecer conexión con la base de datos MySQL
*/

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_turnos";
    private static final String USUARIO = "turnos_app";
    private static final String CLAVE = "turnos1234";

    /**
     * Devuelvo conexión activa a la base de datos gestion_turnos
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}