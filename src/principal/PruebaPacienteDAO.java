package principal;

import dao.PacienteDAO;
import modelo.Paciente;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase de prueba para verificar operaciones de pacientes con MySQL
 */
public class PruebaPacienteDAO {

    public static void main(String[] args) {

        PacienteDAO pacienteDAO = new PacienteDAO();

        try {
            Paciente paciente = new Paciente(
                    0,
                    "Paciente",
                    "Prueba",
                    40111222,
                    155001234,
                    "paciente.prueba@gmail.com"
            );

            pacienteDAO.insertar(paciente);

            System.out.println("Paciente insertado correctamente.");

            ArrayList<Paciente> pacientes = pacienteDAO.listar();

            System.out.println("Listado de pacientes:");

            for (Paciente p : pacientes) {
                System.out.println(
                        p.getId() + " - " +
                                p.getNombreCompleto() + " - DNI: " +
                                p.getDni() + " - Email: " +
                                p.getEmail()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al trabajar con pacientes en la base de datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}