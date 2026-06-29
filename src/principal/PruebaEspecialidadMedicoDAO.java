package principal;

import dao.EspecialidadDAO;
import dao.MedicoDAO;
import modelo.Especialidad;
import modelo.Medico;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase de prueba para verificar operaciones de especialidades y médicos con MySQL.
 */
public class PruebaEspecialidadMedicoDAO {

    public static void main(String[] args) {

        EspecialidadDAO especialidadDAO = new EspecialidadDAO();
        MedicoDAO medicoDAO = new MedicoDAO();

        try {
            Especialidad especialidad = new Especialidad(
                    0,
                    "Dermatología",
                    "Atención de enfermedades de la piel"
            );

            especialidadDAO.insertar(especialidad);

            System.out.println("Especialidad insertada correctamente.");

            ArrayList<Especialidad> especialidades = especialidadDAO.listar();

            System.out.println("Listado de especialidades:");

            for (Especialidad e : especialidades) {
                System.out.println(
                        e.getIdEspecialidad() + " - " +
                                e.getNombre() + " - " +
                                e.getDescripcion()
                );
            }

            Especialidad especialidadParaMedico = especialidades.get(0);

            Medico medico = new Medico(
                    0,
                    "Médico",
                    "Prueba",
                    25111222,
                    155006666,
                    "MP-9999",
                    especialidadParaMedico
            );

            medicoDAO.insertar(medico);

            System.out.println("Médico insertado correctamente.");

            ArrayList<Medico> medicos = medicoDAO.listar();

            System.out.println("Listado de médicos:");

            for (Medico m : medicos) {
                System.out.println(
                        m.getId() + " - " +
                                m.getNombreCompleto() +
                                " - DNI: " + m.getDni() +
                                " - Matrícula: " + m.getMatricula() +
                                " - Especialidad: " + m.getEspecialidad().getNombre()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al trabajar con especialidades o médicos en la base de datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}