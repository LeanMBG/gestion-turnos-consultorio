package vista;

import controlador.TurnoController;
import excepciones.DatoInvalidoException;
import excepciones.TurnoNoDisponibleException;
import modelo.Especialidad;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Ventana principal del sistema de gestión de turnos médicos
 *
 * Esta clase representa la Vista dentro del patrón MVC solicitado por la cátedra..
 * Permite al usuario interactuar con el sistema mediante una interfaz gráfica Swing.
 * Las acciones realizadas desde esta ventana se comunican con TurnoController,
 * que actúa como intermediario entre la vista, la lógica del sistema y la base de datos MySQL.
 */
public class VentanaPrincipal extends JFrame {

    private TurnoController controller;

    // Al iniciar la aplicación se usa la fecha actual como fecha sugerida.
    private LocalDate fechaSugerida = LocalDate.now();

    /*
     * Al iniciar la aplicación se usa la hora actual como hora sugerida.
     * Se eliminan segundos y nanosegundos para que el formato sea compatible con HH:MM.
     */
    private LocalTime horaSugerida = LocalTime.now().withSecond(0).withNano(0);

    /**
     * Constructor de la ventana principal.
     * Inicializo el controlador y construyo la interfaz gráfica.
     */
    public VentanaPrincipal() {
        controller = new TurnoController();

        setTitle("Sistema de gestión de turnos médicos");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
    }

    /**
     * Inicializo los componentes principales de la ventana.
     */
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(9, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Inicialización de botones principales del sistema.
        JButton btnEspecialidad = new JButton("Registrar especialidad");
        JButton btnPaciente = new JButton("Registrar paciente");
        JButton btnMedico = new JButton("Registrar médico");
        JButton btnAsignarTurno = new JButton("Asignar turno");
        JButton btnConsultarTurnos = new JButton("Consultar turnos");
        JButton btnBuscarPorDni = new JButton("Buscar turnos por DNI");
        JButton btnCancelarTurno = new JButton("Cancelar turno");
        JButton btnExportarTurnos = new JButton("Exportar turnos a TXT");
        JButton btnSalir = new JButton("Salir");

        // Agrego los botones al panel principal.
        panelPrincipal.add(btnEspecialidad);
        panelPrincipal.add(btnPaciente);
        panelPrincipal.add(btnMedico);
        panelPrincipal.add(btnAsignarTurno);
        panelPrincipal.add(btnConsultarTurnos);
        panelPrincipal.add(btnBuscarPorDni);
        panelPrincipal.add(btnCancelarTurno);
        panelPrincipal.add(btnExportarTurnos);
        panelPrincipal.add(btnSalir);

        add(panelPrincipal, BorderLayout.CENTER);

        // Acciones que estan asociadas a cada botón
        btnEspecialidad.addActionListener(e -> mostrarFormularioEspecialidad());
        btnPaciente.addActionListener(e -> mostrarFormularioPaciente());
        btnMedico.addActionListener(e -> mostrarFormularioMedico());
        btnAsignarTurno.addActionListener(e -> mostrarFormularioAsignarTurno());
        btnConsultarTurnos.addActionListener(e -> consultarTurnos());
        btnBuscarPorDni.addActionListener(e -> buscarTurnosPorDni());
        btnCancelarTurno.addActionListener(e -> cancelarTurno());
        btnExportarTurnos.addActionListener(e -> exportarTurnosTxt());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    /**
     * Muestra el formulario para registrar una especialidad médica
     */
    private void mostrarFormularioEspecialidad() {
        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();

        Object[] campos = {
                "Nombre:", txtNombre,
                "Descripción:", txtDescripcion
        };

        int opcion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Registrar especialidad",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                controller.registrarEspecialidad(
                        txtNombre.getText(),
                        txtDescripcion.getText()
                );

                JOptionPane.showMessageDialog(this, "Especialidad registrada correctamente.");

            } catch (DatoInvalidoException | SQLException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    /**
     * Muestra el formulario para registrar un paciente
     */
    private void mostrarFormularioPaciente() {
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();

        Object[] campos = {
                "Nombre:", txtNombre,
                "Apellido:", txtApellido,
                "DNI:", txtDni,
                "Teléfono:", txtTelefono,
                "Email:", txtEmail
        };

        int opcion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Registrar paciente",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                int telefono = Integer.parseInt(txtTelefono.getText());

                controller.registrarPaciente(
                        txtNombre.getText(),
                        txtApellido.getText(),
                        dni,
                        telefono,
                        txtEmail.getText()
                );

                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");

            } catch (NumberFormatException ex) {
                mostrarError("El DNI y el teléfono deben ser valores numéricos.");
            } catch (DatoInvalidoException | SQLException ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    /**
     * Muestra el formulario para registrar un médico
     */
    private void mostrarFormularioMedico() {
        try {
            ArrayList<Especialidad> especialidades = controller.listarEspecialidades();

            if (especialidades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe registrar al menos una especialidad antes de cargar el médico.");
                return;
            }

            JTextField txtNombre = new JTextField();
            JTextField txtApellido = new JTextField();
            JTextField txtDni = new JTextField();
            JTextField txtTelefono = new JTextField();
            JTextField txtMatricula = new JTextField();
            JComboBox<Especialidad> comboEspecialidad = new JComboBox<>();

            // Cargo las especialidades existentes para asociar el médico a una de ellas
            for (Especialidad especialidad : especialidades) {
                comboEspecialidad.addItem(especialidad);
            }

            Object[] campos = {
                    "Nombre:", txtNombre,
                    "Apellido:", txtApellido,
                    "DNI:", txtDni,
                    "Teléfono:", txtTelefono,
                    "Matrícula:", txtMatricula,
                    "Especialidad:", comboEspecialidad
            };

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    campos,
                    "Registrar médico",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                int dni = Integer.parseInt(txtDni.getText());
                int telefono = Integer.parseInt(txtTelefono.getText());
                Especialidad especialidadSeleccionada = (Especialidad) comboEspecialidad.getSelectedItem();

                controller.registrarMedico(
                        txtNombre.getText(),
                        txtApellido.getText(),
                        dni,
                        telefono,
                        txtMatricula.getText(),
                        especialidadSeleccionada
                );

                JOptionPane.showMessageDialog(this, "Médico registrado correctamente.");
            }

        } catch (NumberFormatException ex) {
            mostrarError("El DNI y el teléfono deben ser valores numéricos.");
        } catch (DatoInvalidoException | SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Muestra el formulario para asignar un turno
     */
    private void mostrarFormularioAsignarTurno() {
        try {
            ArrayList<Paciente> pacientes = controller.listarPacientes();
            ArrayList<Medico> medicos = controller.listarMedicos();

            if (pacientes.isEmpty() || medicos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir al menos un paciente y un médico para asignar un turno.");
                return;
            }

            JComboBox<Paciente> comboPaciente = new JComboBox<>();
            JComboBox<Medico> comboMedico = new JComboBox<>();

            // Cargo pacientes y médicos desde la base de datos
            for (Paciente paciente : pacientes) {
                comboPaciente.addItem(paciente);
            }

            for (Medico medico : medicos) {
                comboMedico.addItem(medico);
            }

            // Se propone la fecha y hora sugeridas por el sistema
            JTextField txtFecha = new JTextField(obtenerFechaSugeridaTexto());
            JTextField txtHora = new JTextField(obtenerHoraSugeridaTexto());
            JTextField txtObservacion = new JTextField();

            Object[] campos = {
                    "Paciente:", comboPaciente,
                    "Médico:", comboMedico,
                    "Fecha (AAAA-MM-DD):", txtFecha,
                    "Hora (HH:MM):", txtHora,
                    "Observación:", txtObservacion
            };

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    campos,
                    "Asignar turno",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                Paciente pacienteSeleccionado = (Paciente) comboPaciente.getSelectedItem();
                Medico medicoSeleccionado = (Medico) comboMedico.getSelectedItem();

                // Convierto la fecha ingresada a LocalDate.
                LocalDate fecha = LocalDate.parse(txtFecha.getText());

                // Convierto la hora ingresada a LocalTime.
                LocalTime hora = LocalTime.parse(txtHora.getText());

                // Actualizo fecha y hora sugeridas para reutilizarlas en próximas operaciones
                actualizarFechaSugerida(fecha);
                actualizarHoraSugerida(hora);

                controller.asignarTurno(
                        pacienteSeleccionado,
                        medicoSeleccionado,
                        fecha,
                        hora,
                        txtObservacion.getText()
                );

                JOptionPane.showMessageDialog(this, "Turno registrado correctamente.");
            }

        } catch (DatoInvalidoException | TurnoNoDisponibleException | SQLException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("La fecha debe tener formato AAAA-MM-DD y la hora formato HH:MM.");
        }
    }

    /**
     * Consulto turnos. Al ingresar, el usuario puede elegir entre:
     * 1. Ver todos los turnos registrados.
     * 2. Ver turnos de una fecha determinada.
     * 3. Ver turnos activos por especialidad y fecha.
     */
    private void consultarTurnos() {

        // Opciones que se muestran al usuario al presionar el botón "Consultar turnos".
        String[] opciones = {
                "Ver todos los turnos registrados",
                "Ver turnos por fecha",
                "Ver turnos activos por especialidad y fecha"
        };

        // Muestro un cuadro de diálogo para seleccionar el tipo de consulta
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el tipo de consulta que desea realizar:",
                "Consultar turnos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        // Si el usuario cancela el cuadro, no se realiza ninguna acción
        if (seleccion == null) {
            return;
        }

        // Según la opción elegida, se llama al método específico
        switch (seleccion) {
            case "Ver todos los turnos registrados":
                consultarTodosLosTurnos();
                break;

            case "Ver turnos por fecha":
                consultarTurnosPorFecha();
                break;

            case "Ver turnos activos por especialidad y fecha":
                consultarTurnosPorEspecialidadYFecha();
                break;
        }
    }

    /**
     * Consulta todos los turnos registrados en base de datos.
     * Esta opción muestra la consulta general, incluyendo turnos reservados, cancelados o atendidos.
     */
    private void consultarTodosLosTurnos() {
        try {
            ArrayList<Turno> turnos = controller.listarTurnos();

            mostrarTablaTurnos(turnos, "Consulta general de turnos");

        } catch (SQLException ex) {
            mostrarError("Error al consultar la base de datos: " + ex.getMessage());
        }
    }

    /**
     * Consulta los turnos registrados en una fecha determinada.
     * Esta opción permite visualizar la agenda completa de un día, manteniendo visible el estado de cada turno
     */
    private void consultarTurnosPorFecha() {

        String fechaTexto = JOptionPane.showInputDialog(
                this,
                "Ingrese la fecha a consultar (AAAA-MM-DD):",
                obtenerFechaSugeridaTexto()
        );

        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
            return;
        }

        try {
            // Conversión del texto ingresado a LocalDate.
            LocalDate fecha = LocalDate.parse(fechaTexto);

            // Guardo la fecha consultada para proponerla en otros formularios.
            actualizarFechaSugerida(fecha);

            ArrayList<Turno> turnos = controller.listarTurnosPorFecha(fecha);

            if (turnos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontraron turnos registrados para la fecha ingresada."
                );
            } else {
                mostrarTablaTurnos(turnos, "Turnos registrados el día " + fecha);
            }

        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());

        } catch (SQLException ex) {
            mostrarError("Error al consultar la base de datos: " + ex.getMessage());

        } catch (Exception ex) {
            mostrarError("La fecha debe tener formato AAAA-MM-DD.");
        }
    }

    /**
     * Consulta turnos activos filtrados por especialidad médica y fecha
     */
    private void consultarTurnosPorEspecialidadYFecha() {
        try {
            ArrayList<Especialidad> especialidades = controller.listarEspecialidades();

            if (especialidades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir al menos una especialidad registrada.");
                return;
            }

            JComboBox<Especialidad> comboEspecialidad = new JComboBox<>();

            // Cargo las especialidades dentro del ComboBox.
            for (Especialidad especialidad : especialidades) {
                comboEspecialidad.addItem(especialidad);
            }

            // Fecha sugerida por el sistema.
            JTextField txtFecha = new JTextField(obtenerFechaSugeridaTexto());

            Object[] campos = {
                    "Especialidad:", comboEspecialidad,
                    "Fecha (AAAA-MM-DD):", txtFecha
            };

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    campos,
                    "Consultar turnos por especialidad y fecha",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                Especialidad especialidadSeleccionada = (Especialidad) comboEspecialidad.getSelectedItem();

                // Se convierte la fecha ingresada a LocalDate.
                LocalDate fecha = LocalDate.parse(txtFecha.getText());

                // Actualizo la fecha sugerida para próximas consultas o exportaciones.
                actualizarFechaSugerida(fecha);

                ArrayList<Turno> turnos = controller.listarTurnosPorEspecialidadYFecha(
                        especialidadSeleccionada,
                        fecha
                );

                if (turnos.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se encontraron turnos activos para la especialidad y fecha seleccionadas."
                    );
                } else {
                    mostrarTablaTurnos(turnos, "Turnos activos por especialidad y fecha");
                }
            }

        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());

        } catch (SQLException ex) {
            mostrarError("Error al consultar la base de datos: " + ex.getMessage());

        } catch (Exception ex) {
            mostrarError("La fecha debe tener formato AAAA-MM-DD.");
        }
    }
    /**
     * Obtiene los turnos activos disponibles para cancelar.
     * Permite elegir si se muestran todos los turnos activos, si se busca por DNI o si se busca por nombre/apellido del paciente.
     */
    private ArrayList<Turno> obtenerTurnosActivosParaCancelar() throws SQLException {

        String[] opciones = {
                "Ver todos los turnos activos",
                "Buscar por DNI del paciente",
                "Buscar por nombre o apellido del paciente"
        };

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione cómo desea buscar el turno a cancelar:",
                "Buscar turno para cancelar",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return null;
        }

        ArrayList<Turno> turnos;

        switch (seleccion) {
            case "Buscar por DNI del paciente":
                String dniTexto = JOptionPane.showInputDialog(this, "Ingrese el DNI del paciente:");

                if (dniTexto == null || dniTexto.trim().isEmpty()) {
                    return null;
                }

                try {
                    int dni = Integer.parseInt(dniTexto);
                    turnos = controller.buscarTurnosPorDni(dni);
                } catch (NumberFormatException ex) {
                    mostrarError("El DNI debe ser numérico.");
                    return null;
                }
                break;

            case "Buscar por nombre o apellido del paciente":
                String textoBusqueda = JOptionPane.showInputDialog(
                        this,
                        "Ingrese nombre o apellido del paciente:"
                );

                if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
                    return null;
                }

                turnos = filtrarTurnosPorNombreOApellido(
                        controller.listarTurnos(),
                        textoBusqueda
                );
                break;

            default:
                turnos = controller.listarTurnos();
                break;
        }

        return filtrarTurnosActivos(turnos);
    }
    /**
     * Filtra una lista de turnos dejando solamente aquellos que no están cancelados.
     */
    private ArrayList<Turno> filtrarTurnosActivos(ArrayList<Turno> turnos) {
        ArrayList<Turno> turnosActivos = new ArrayList<>();

        for (Turno turno : turnos) {
            if (!turno.getEstadoTurno().getEstado().equalsIgnoreCase("Cancelado")) {
                turnosActivos.add(turno);
            }
        }

        return turnosActivos;
    }
    /**
     * Filtra turnos por nombre o apellido del paciente.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     */
    private ArrayList<Turno> filtrarTurnosPorNombreOApellido(ArrayList<Turno> turnos, String textoBusqueda) {
        ArrayList<Turno> resultado = new ArrayList<>();

        String texto = textoBusqueda.toLowerCase().trim();

        for (Turno turno : turnos) {
            String nombrePaciente = turno.getPaciente().getNombre().toLowerCase();
            String apellidoPaciente = turno.getPaciente().getApellido().toLowerCase();
            String nombreCompleto = turno.getPaciente().getNombreCompleto().toLowerCase();

            if (nombrePaciente.contains(texto)
                    || apellidoPaciente.contains(texto)
                    || nombreCompleto.contains(texto)) {
                resultado.add(turno);
            }
        }

        return resultado;
    }

    /**
     * Busca turnos por DNI del paciente.
     */
    private void buscarTurnosPorDni() {
        String dniTexto = JOptionPane.showInputDialog(this, "Ingrese el DNI del paciente:");

        if (dniTexto == null || dniTexto.trim().isEmpty()) {
            return;
        }

        try {
            int dni = Integer.parseInt(dniTexto);
            ArrayList<Turno> turnos = controller.buscarTurnosPorDni(dni);

            if (turnos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron turnos para el DNI ingresado.");
            } else {
                mostrarTablaTurnos(turnos, "Turnos encontrados por DNI");
            }

        } catch (NumberFormatException ex) {
            mostrarError("El DNI debe ser numérico.");
        } catch (SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Cancela un turno activo modificando su estado en MySQL.
     * Antes de mostrar los turnos permite filtrar por DNI, nombre o apellido del paciente.
     */
    private void cancelarTurno() {
        try {
            ArrayList<Turno> turnosParaCancelar = obtenerTurnosActivosParaCancelar();

            if (turnosParaCancelar == null) {
                return;
            }

            if (turnosParaCancelar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron turnos activos para cancelar.");
                return;
            }

            JComboBox<Turno> comboTurnos = new JComboBox<>();

            // Cargo en el combo solo los turnos activos encontrados según el filtro aplicado.
            for (Turno turno : turnosParaCancelar) {
                comboTurnos.addItem(turno);
            }

            Object[] campos = {
                    "Seleccione el turno a cancelar:", comboTurnos
            };

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    campos,
                    "Cancelar turno",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                Turno turnoSeleccionado = (Turno) comboTurnos.getSelectedItem();

                if (turnoSeleccionado == null) {
                    return;
                }

                int confirmar = JOptionPane.showConfirmDialog(
                        this,
                        "¿Confirma la cancelación del turno seleccionado?\n\n" +
                                "Paciente: " + turnoSeleccionado.getPaciente().getNombreCompleto() + "\n" +
                                "DNI: " + turnoSeleccionado.getPaciente().getDni() + "\n" +
                                "Fecha: " + turnoSeleccionado.getFecha() + "\n" +
                                "Hora: " + turnoSeleccionado.getHora(),
                        "Confirmar cancelación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmar == JOptionPane.YES_OPTION) {
                    controller.cancelarTurno(turnoSeleccionado.getIdTurno());
                    JOptionPane.showMessageDialog(this, "Turno cancelado correctamente.");

                    // Luego de cancelar, muestro nuevamente la tabla con todos los turnos para verificar el cambio de estado.
                    mostrarTablaTurnos(controller.listarTurnos(), "Turnos actualizados");
                }
            }

        } catch (SQLException ex) {
            mostrarError("Error al consultar la base de datos: " + ex.getMessage());
        }
    }

    /**
     * Exportación de turnos activos por especialidad y fecha a un archivo TXT.
     */
    private void exportarTurnosTxt() {
        try {
            ArrayList<Especialidad> especialidades = controller.listarEspecialidades();

            if (especialidades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir al menos una especialidad registrada.");
                return;
            }

            JComboBox<Especialidad> comboEspecialidad = new JComboBox<>();

            // Cargo las especialidades para que el usuario elija cuál exportar.
            for (Especialidad especialidad : especialidades) {
                comboEspecialidad.addItem(especialidad);
            }

            // Se propone la última fecha consultada o la fecha actual si no se consultó otra fecha.
            JTextField txtFecha = new JTextField(obtenerFechaSugeridaTexto());

            Object[] campos = {
                    "Especialidad:", comboEspecialidad,
                    "Fecha (AAAA-MM-DD):", txtFecha
            };

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    campos,
                    "Exportar turnos a TXT",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                Especialidad especialidadSeleccionada = (Especialidad) comboEspecialidad.getSelectedItem();

                // Convierto la fecha ingresada a LocalDate.
                LocalDate fecha = LocalDate.parse(txtFecha.getText());

                // Guardo la fecha utilizada para mantener coherencia entre consulta y exportación.
                actualizarFechaSugerida(fecha);

                String rutaArchivo = controller.exportarTurnosPorEspecialidadYFecha(
                        especialidadSeleccionada,
                        fecha
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Archivo generado correctamente:\n" + rutaArchivo
                );
            }

        } catch (DatoInvalidoException ex) {
            mostrarError(ex.getMessage());

        } catch (SQLException ex) {
            mostrarError("Error al consultar la base de datos: " + ex.getMessage());

        } catch (Exception ex) {
            mostrarError("La fecha debe tener formato AAAA-MM-DD.");
        }
    }

    /**
     * Muestra una lista de turnos en una tabla Swing
     * Incluye el DNI del paciente para facilidad de control y cancelación de turnos si fuera necesario.
     */
    private void mostrarTablaTurnos(ArrayList<Turno> turnos, String titulo) {
        String[] columnas = {
                "ID",
                "Fecha",
                "Hora",
                "Paciente",
                "DNI Paciente",
                "Médico",
                "Especialidad",
                "Estado",
                "Observación"
        };

        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        // Cada turno se convierte en una fila de la tabla.
        for (Turno turno : turnos) {
            Object[] fila = {
                    turno.getIdTurno(),
                    turno.getFecha(),
                    turno.getHora(),
                    turno.getPaciente().getNombreCompleto(),
                    turno.getPaciente().getDni(),
                    turno.getMedico().getNombreCompleto(),
                    turno.getMedico().getEspecialidad().getNombre(),
                    turno.getEstadoTurno().getEstado(),
                    turno.getObservacion()
            };

            modeloTabla.addRow(fila);
        }

        JTable tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);

        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setSize(1000, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.add(scrollPane);
        dialogo.setVisible(true);
    }

    /**
     * Muestra mensajes de error controlados.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Devuelve la fecha sugerida en formato de texto.
     * Se utiliza para completar los campos de fecha en formularios de asignación, consulta y exportación
     */
    private String obtenerFechaSugeridaTexto() {
        return fechaSugerida.toString();
    }

    /**
     * Devuelve la hora sugerida en formato de texto.
     * Se utiliza para completar el campo de hora al asignar un turno
     */
    private String obtenerHoraSugeridaTexto() {
        return horaSugerida.toString();
    }

    /**
     * Actualiza la fecha sugerida del sistema, permitiendo que si el usuario consulta una fecha determinada,
     * esa misma fecha se proponga luego en otros formularios.
     */
    private void actualizarFechaSugerida(LocalDate fecha) {
        if (fecha != null) {
            fechaSugerida = fecha;
        }
    }

    /**
     * Actualiza la hora sugerida del sistema.
     * Esto permite que, si el usuario asigna un turno en determinado horario, ese horario quede propuesto en la próxima asignación.
     */
    private void actualizarHoraSugerida(LocalTime hora) {
        if (hora != null) {
            horaSugerida = hora;
        }
    }
}