package principal;

import controlador.TurnoController;
import modelo.Turno;

import java.util.ArrayList;

/**
 * Clase de prueba para verificar el funcionamiento del controlador.
 */
public class PruebaTurnoController {

    public static void main(String[] args) {

        TurnoController controller = new TurnoController();

        try {
            ArrayList<Turno> turnos = controller.listarTurnos();

            System.out.println("Listado de turnos obtenido desde el controlador:");

            for (Turno turno : turnos) {
                System.out.println(
                        turno.getIdTurno() + " - " +
                                turno.getFecha() + " - " +
                                turno.getHora() + " - Paciente: " +
                                turno.getPaciente().getNombreCompleto() + " - Médico: " +
                                turno.getMedico().getNombreCompleto() + " - Estado: " +
                                turno.getEstadoTurno().getEstado()
                );
            }

        } catch (Exception e) {
            System.out.println("Error al probar el controlador.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}