package dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz genérica que define las operaciones básicas de acceso a datos
 * La letra T representa cualquier tipo de entidad del sistema, por ejemplo: Paciente, Médico, Turno, etc.
 * Al usar esta interfaz, las clases DAO pueden implementar los mismos métodos obligatorios, favorece la reutilización para el acceso a la base de datos
 */
public interface RepositorioDAO<T> {

    /**
     * Inserto una entidad en la base de datos (obj que guardo, y valido si hay error al acceder a la BD)
     */
    void insertar(T entidad) throws SQLException;

    /**
     * Listo todas las entidades registradas en la base de datos
     */
    ArrayList<T> listar() throws SQLException;
}