package archivos;

import modelo.Turno;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase que se encarga de exportar los turnos médicos a un archivo de texto.
 * Permite aplicar el manejo de archivos necesario para el proyecto EFIP.
 */
public class ExportadorTurnosTXT {

    /**
     * Exporta una lista de turnos a un archivo TXT con formato tipo de agenda médica.
     * Los turnos están agrupados por médico y se muestran ordenados para facilitar la atención diaria si lo requieren
     */
    public static void exportarTurnos(ArrayList<Turno> turnos, String rutaArchivo,
                                      String especialidad, LocalDate fecha) throws IOException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {

            writer.println("AGENDA MÉDICA DIARIA");
            writer.println("Especialidad: " + especialidad);
            writer.println("Fecha: " + fecha);
            writer.println("Cantidad de turnos activos: " + turnos.size());
            writer.println("============================================================");
            writer.println();

            if (turnos.isEmpty()) {
                writer.println("No se encontraron turnos activos para la especialidad y fecha seleccionadas.");
                return;
            }

            /*
             * Se agrupan los turnos por médico.
             * Esto permite generar una agenda clara para cada profesional.
             */
            Map<String, ArrayList<Turno>> turnosPorMedico = new LinkedHashMap<>();

            for (Turno turno : turnos) {
                String nombreMedico = turno.getMedico().getNombreCompleto();

                if (!turnosPorMedico.containsKey(nombreMedico)) {
                    turnosPorMedico.put(nombreMedico, new ArrayList<>());
                }

                turnosPorMedico.get(nombreMedico).add(turno);
            }

            for (String medico : turnosPorMedico.keySet()) {

                writer.println("Médico: " + medico);
                writer.println("------------------------------------------------------------");
                writer.printf("%-8s %-22s %-12s %-12s %-25s%n",
                        "Hora", "Paciente", "DNI", "Teléfono", "Observación");
                writer.println("------------------------------------------------------------");

                ArrayList<Turno> turnosDelMedico = turnosPorMedico.get(medico);

                for (Turno turno : turnosDelMedico) {
                    writer.printf("%-8s %-22s %-12s %-12s %-25s%n",
                            turno.getHora(),
                            turno.getPaciente().getNombreCompleto(),
                            turno.getPaciente().getDni(),
                            turno.getPaciente().getTelefono(),
                            turno.getObservacion()
                    );
                }

                writer.println();
            }

            writer.println("============================================================");
            writer.println("Archivo generado por el Sistema de Gestión de Turnos Médicos.");
        }
    }
}