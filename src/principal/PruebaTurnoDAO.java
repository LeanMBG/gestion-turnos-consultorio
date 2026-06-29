package principal;

import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.TurnoDAO;
import modelo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase de prueba para verificar operaciones de turnos con MySQL.
 */
public class PruebaTurnoDAO {

    public static void main(String[] args) {

        PacienteDAO pacienteDAO = new PacienteDAO();
        MedicoDAO medicoDAO = new MedicoDAO();
        TurnoDAO turnoDAO = new TurnoDAO();

        try {
            ArrayList<Paciente> pacientes = pacienteDAO.listar();
            ArrayList<Medico> medicos = medicoDAO.listar();

            if (pacientes.isEmpty() || medicos.isEmpty()) {
                System.out.println("Debe existir al menos un paciente y un médico para asignar un turno.");
                return;
            }

            Paciente paciente = pacientes.get(0);
            Medico medico = medicos.get(0);

            LocalDate fecha = LocalDate.now().plusDays(10);
            LocalTime hora = LocalTime.of(11, 30);

            boolean disponible = turnoDAO.verificarDisponibilidad(
                    medico.getId(),
                    fecha,
                    hora
            );

            if (disponible) {

                EstadoTurno estadoReservado = new EstadoTurno(1, "Reservado");

                UsuarioAdministrativo usuario = new UsuarioAdministrativo(
                        1,
                        "admin",
                        "1234",
                        "Leandro",
                        "Barbera"
                );

                Turno turno = new Turno(
                        0,
                        fecha,
                        hora,
                        "Turno de prueba desde Java",
                        paciente,
                        medico,
                        estadoReservado,
                        usuario
                );

                turnoDAO.insertar(turno);

                System.out.println("Turno insertado correctamente.");

            } else {
                System.out.println("El médico no tiene disponibilidad en esa fecha y horario.");
            }

            System.out.println();
            System.out.println("Listado de turnos:");

            ArrayList<Turno> turnos = turnoDAO.listar();

            for (Turno t : turnos) {
                System.out.println(
                        t.getIdTurno() + " - " +
                                t.getFecha() + " - " +
                                t.getHora() + " - Paciente: " +
                                t.getPaciente().getNombreCompleto() + " - Médico: " +
                                t.getMedico().getNombreCompleto() + " - Estado: " +
                                t.getEstadoTurno().getEstado()
                );
            }

            System.out.println();
            System.out.println("Búsqueda de turnos por DNI del paciente: " + paciente.getDni());

            ArrayList<Turno> turnosPorDni = turnoDAO.buscarPorDniPaciente(paciente.getDni());

            for (Turno t : turnosPorDni) {
                System.out.println(
                        t.getIdTurno() + " - " +
                                t.getFecha() + " - " +
                                t.getHora() + " - Estado: " +
                                t.getEstadoTurno().getEstado()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al trabajar con turnos en la base de datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}