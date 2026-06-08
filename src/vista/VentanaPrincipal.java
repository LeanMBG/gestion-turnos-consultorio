package vista;

import excepciones.DatoInvalidoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.TurnoNoDisponibleException;
import modelo.Turno;
import servicio.SistemaTurnos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Ventana principal del sistema de gestión de turnos médicos.
 * Esta clase representa la pantalla inicial de la aplicación.
 */
public class VentanaPrincipal extends JFrame {

    // Objeto principal que contiene la lógica del sistema.
    private SistemaTurnos sistema;

    /**
     * Constructor de la ventana principal.
     * Inicializa el sistema, configura la ventana y carga los componentes gráficos.
     */
    public VentanaPrincipal() {
        this.sistema = new SistemaTurnos();

        // Configuración básica de la ventana principal.
        setTitle("Sistema de Gestión de Turnos Médicos");
        setSize(500, 450);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
    }

    /**
     * Método encargado de crear y organizar los componentes visuales
     * de la ventana principal.
     */
    private void inicializarComponentes() {

        // Panel principal con BorderLayout para organizar título y botones.
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        // Título superior de la aplicación.
        JLabel titulo = new JLabel("Sistema de Gestión de Turnos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        // Panel donde se ubican los botones del menú principal.
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(8, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        // Creación de botones principales del sistema.
        JButton btnRegistrarPaciente = new JButton("Registrar paciente");
        JButton btnRegistrarMedico = new JButton("Registrar médico");
        JButton btnRegistrarEspecialidad = new JButton("Registrar especialidad");
        JButton btnAsignarTurno = new JButton("Asignar turno");
        JButton btnConsultarTurnos = new JButton("Consultar turnos");
        JButton btnBuscarTurnosPorDni = new JButton("Buscar turnos por DNI");
        JButton btnCancelarTurno = new JButton("Cancelar turno");
        JButton btnSalir = new JButton("Salir");

        // Agregado de botones al panel del menú.
        panelBotones.add(btnRegistrarPaciente);
        panelBotones.add(btnRegistrarMedico);
        panelBotones.add(btnRegistrarEspecialidad);
        panelBotones.add(btnAsignarTurno);
        panelBotones.add(btnConsultarTurnos);
        panelBotones.add(btnBuscarTurnosPorDni);
        panelBotones.add(btnCancelarTurno);
        panelBotones.add(btnSalir);

        // Agregado del título y el panel de botones al panel principal.
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Agregado del panel principal a la ventana.
        add(panelPrincipal);

        /*
         * Eventos asociados a los botones del menú principal.
         * Cada botón invoca un formulario o una acción específica del sistema.
         * De esta manera se conecta la interfaz gráfica Swing con la lógica de negocio.
         */

        // Abre el formulario para registrar un nuevo paciente.
        btnRegistrarPaciente.addActionListener(e -> mostrarFormularioPaciente());

        // Abre el formulario para registrar un nuevo médico.
        btnRegistrarMedico.addActionListener(e -> mostrarFormularioMedico());

        // Abre el formulario para registrar una nueva especialidad médica.
        btnRegistrarEspecialidad.addActionListener(e -> mostrarFormularioEspecialidad());

        // Abre el formulario para asignar un turno a un paciente con un médico.
        btnAsignarTurno.addActionListener(e -> mostrarFormularioAsignarTurno());

        // Muestra una tabla con todos los turnos registrados.
        btnConsultarTurnos.addActionListener(e -> mostrarListadoTurnos());

        // Permite buscar turnos registrados a partir del DNI del paciente.
        btnBuscarTurnosPorDni.addActionListener(e -> mostrarBusquedaTurnosPorDni());

        // Permite cancelar un turno seleccionándolo desde una lista.
        btnCancelarTurno.addActionListener(e -> mostrarFormularioCancelarTurno());

        // Cierra la aplicación.
        btnSalir.addActionListener(e -> System.exit(0));
    }

    /**
     * Muestra un formulario modal para registrar una especialidad médica.
     * Este formulario permite cargar nombre y descripción de la especialidad.
     */
    private void mostrarFormularioEspecialidad() {

        JDialog dialogo = new JDialog(this, "Registrar especialidad", true);
        dialogo.setSize(400, 220);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel del formulario con dos columnas: etiqueta y campo de texto.
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(txtDescripcion);

        // Panel inferior donde se colocan los botones del formulario.
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        /*
         * Evento del botón Guardar.
         * Toma los datos ingresados, llama a la lógica del sistema
         * y maneja posibles errores mediante excepciones.
         */
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();

                sistema.registrarEspecialidad(nombre, descripcion);

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Especialidad registrada correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialogo.dispose();

            } catch (DatoInvalidoException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        ex.getMessage(),
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Cierra el formulario sin guardar datos.
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setVisible(true);
    }

    /**
     * Muestra un formulario modal para registrar un paciente.
     * Permite cargar datos básicos y valida que los campos obligatorios sean correctos.
     */
    private void mostrarFormularioPaciente() {

        JDialog dialogo = new JDialog(this, "Registrar paciente", true);
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel del formulario con etiquetas y campos de entrada.
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblApellido);
        panelFormulario.add(txtApellido);
        panelFormulario.add(lblDni);
        panelFormulario.add(txtDni);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(lblEmail);
        panelFormulario.add(txtEmail);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        /*
         * Evento del botón Guardar.
         * Convierte DNI y teléfono a enteros, llama a la lógica del sistema
         * y muestra mensajes de éxito o error según corresponda.
         */
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int dni = Integer.parseInt(txtDni.getText());
                int telefono = Integer.parseInt(txtTelefono.getText());
                String email = txtEmail.getText();

                sistema.registrarPaciente(nombre, apellido, dni, telefono, email);

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Paciente registrado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialogo.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "DNI y teléfono deben ser valores numéricos.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (DatoInvalidoException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        ex.getMessage(),
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Cierra el formulario sin guardar datos.
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setVisible(true);
    }

    /**
     * Muestra un formulario modal para registrar un médico.
     * Para registrar un médico debe existir al menos una especialidad cargada.
     */
    private void mostrarFormularioMedico() {

        // Validación previa: un médico debe estar asociado a una especialidad.
        if (sistema.getEspecialidades().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe registrar al menos una especialidad antes de cargar un médico.",
                    "Especialidad requerida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JDialog dialogo = new JDialog(this, "Registrar médico", true);
        dialogo.setSize(480, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel del formulario con etiquetas y campos de entrada.
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblMatricula = new JLabel("Matrícula:");
        JTextField txtMatricula = new JTextField();

        JLabel lblEspecialidad = new JLabel("Especialidad:");
        JComboBox<modelo.Especialidad> comboEspecialidad =
                new JComboBox<>(sistema.getEspecialidades().toArray(new modelo.Especialidad[0]));

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblApellido);
        panelFormulario.add(txtApellido);
        panelFormulario.add(lblDni);
        panelFormulario.add(txtDni);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(lblMatricula);
        panelFormulario.add(txtMatricula);
        panelFormulario.add(lblEspecialidad);
        panelFormulario.add(comboEspecialidad);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        /*
         * Evento del botón Guardar.
         * Convierte DNI y teléfono a enteros, obtiene la especialidad seleccionada
         * y llama al método registrarMedico de la lógica del sistema.
         */
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int dni = Integer.parseInt(txtDni.getText());
                int telefono = Integer.parseInt(txtTelefono.getText());
                String matricula = txtMatricula.getText();

                modelo.Especialidad especialidadSeleccionada =
                        (modelo.Especialidad) comboEspecialidad.getSelectedItem();

                sistema.registrarMedico(
                        nombre,
                        apellido,
                        dni,
                        telefono,
                        matricula,
                        especialidadSeleccionada
                );

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Médico registrado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialogo.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "DNI y teléfono deben ser valores numéricos.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (DatoInvalidoException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        ex.getMessage(),
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Cierra el formulario sin guardar datos.
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setVisible(true);
    }

    /**
     * Muestra un formulario modal para asignar un turno médico.
     * El turno requiere seleccionar paciente, médico, fecha, hora y una observación opcional.
     */
    private void mostrarFormularioAsignarTurno() {

        // Validación previa: para asignar un turno deben existir pacientes y médicos registrados.
        if (sistema.getPacientes().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe registrar al menos un paciente antes de asignar un turno.",
                    "Paciente requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (sistema.getMedicos().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe registrar al menos un médico antes de asignar un turno.",
                    "Médico requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JDialog dialogo = new JDialog(this, "Asignar turno", true);
        dialogo.setSize(500, 380);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel del formulario.
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblPaciente = new JLabel("Paciente:");
        JComboBox<modelo.Paciente> comboPaciente =
                new JComboBox<>(sistema.getPacientes().toArray(new modelo.Paciente[0]));

        JLabel lblMedico = new JLabel("Médico:");
        JComboBox<modelo.Medico> comboMedico =
                new JComboBox<>(sistema.getMedicos().toArray(new modelo.Medico[0]));

        JLabel lblFecha = new JLabel("Fecha (AAAA-MM-DD):");
        JTextField txtFecha = new JTextField();

        JLabel lblHora = new JLabel("Hora (HH:MM):");
        JTextField txtHora = new JTextField();

        JLabel lblObservacion = new JLabel("Observación:");
        JTextField txtObservacion = new JTextField();

        panelFormulario.add(lblPaciente);
        panelFormulario.add(comboPaciente);
        panelFormulario.add(lblMedico);
        panelFormulario.add(comboMedico);
        panelFormulario.add(lblFecha);
        panelFormulario.add(txtFecha);
        panelFormulario.add(lblHora);
        panelFormulario.add(txtHora);
        panelFormulario.add(lblObservacion);
        panelFormulario.add(txtObservacion);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        /*
         * Evento del botón Guardar.
         * Convierte fecha y hora al formato correspondiente, verifica disponibilidad
         * y registra el turno si no existe superposición.
         */
        btnGuardar.addActionListener(e -> {
            try {
                modelo.Paciente pacienteSeleccionado =
                        (modelo.Paciente) comboPaciente.getSelectedItem();

                modelo.Medico medicoSeleccionado =
                        (modelo.Medico) comboMedico.getSelectedItem();

                LocalDate fecha = LocalDate.parse(txtFecha.getText());
                LocalTime hora = LocalTime.parse(txtHora.getText());
                String observacion = txtObservacion.getText();

                sistema.asignarTurno(
                        pacienteSeleccionado,
                        medicoSeleccionado,
                        fecha,
                        hora,
                        observacion,
                        sistema.getUsuarioAdministrador()
                );

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Turno asignado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialogo.dispose();

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "La fecha debe tener formato AAAA-MM-DD y la hora formato HH:MM.",
                        "Formato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (DatoInvalidoException | TurnoNoDisponibleException ex) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        ex.getMessage(),
                        "No se pudo asignar el turno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Cierra el formulario sin guardar datos.
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setVisible(true);
    }

    /**
     * Muestra una ventana con el listado completo de turnos registrados.
     * Antes de mostrar la información, ordena los turnos por fecha y hora.
     */
    private void mostrarListadoTurnos() {

        if (sistema.getTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay turnos registrados.",
                    "Consulta de turnos",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Ordena los turnos por fecha y hora antes de mostrarlos.
        sistema.ordenarTurnosPorFechaYHora();

        mostrarTablaTurnos(sistema.getTurnos(), "Consultar turnos");
    }

    /**
     * Muestra una lista de turnos en una tabla.
     * Este método permite reutilizar la visualización tanto para consultar todos los turnos
     * como para mostrar resultados de búsqueda.
     */
    private void mostrarTablaTurnos(ArrayList<Turno> listaTurnos, String tituloVentana) {

        JDialog dialogo = new JDialog(this, tituloVentana, true);
        dialogo.setSize(850, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        String[] columnas = {
                "ID",
                "Fecha",
                "Hora",
                "Paciente",
                "Médico",
                "Estado",
                "Observación"
        };

        /*
         * DefaultTableModel permite cargar datos en una JTable.
         * Se sobrescribe isCellEditable para evitar que el usuario edite la tabla directamente.
         */
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Recorre la lista recibida y agrega cada turno como una fila de la tabla.
        for (Turno turno : listaTurnos) {
            Object[] fila = {
                    turno.getIdTurno(),
                    turno.getFecha(),
                    turno.getHora(),
                    turno.getPaciente().getNombreCompleto(),
                    turno.getMedico().getNombreCompleto(),
                    turno.getEstadoTurno().getEstado(),
                    turno.getObservacion()
            };

            modeloTabla.addRow(fila);
        }

        JTable tablaTurnos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTurnos);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        dialogo.add(scrollPane, BorderLayout.CENTER);
        dialogo.add(panelBoton, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }

    /**
     * Permite buscar turnos asociados a un paciente mediante su DNI.
     * El resultado se muestra en una tabla, reutilizando la lista de turnos del sistema.
     */
    private void mostrarBusquedaTurnosPorDni() {

        if (sistema.getTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay turnos registrados para buscar.",
                    "Buscar turnos",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String dniIngresado = JOptionPane.showInputDialog(
                this,
                "Ingrese el DNI del paciente:",
                "Buscar turnos por DNI",
                JOptionPane.QUESTION_MESSAGE
        );

        // Si el usuario cancela o cierra la ventana, no se realiza ninguna acción.
        if (dniIngresado == null) {
            return;
        }

        try {
            int dni = Integer.parseInt(dniIngresado.trim());

            /*
             * Se llama al método de búsqueda definido en SistemaTurnos.
             * Este método recorre la lista de turnos y devuelve aquellos
             * que correspondan al DNI ingresado.
             */
            ArrayList<Turno> turnosEncontrados = sistema.buscarTurnosPorDniPaciente(dni);

            if (turnosEncontrados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontraron turnos para el DNI ingresado.",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            mostrarTablaTurnos(turnosEncontrados, "Turnos encontrados para DNI: " + dni);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe ser un valor numérico.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Permite cancelar un turno registrado seleccionándolo desde una lista desplegable.
     * Se muestran únicamente los turnos activos, evitando cancelar nuevamente turnos ya cancelados.
     */
    private void mostrarFormularioCancelarTurno() {

        if (sistema.getTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay turnos registrados para cancelar.",
                    "Cancelar turno",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ComboBox que contendrá únicamente los turnos que no estén cancelados.
        JComboBox<Turno> comboTurnos = new JComboBox<>();

        for (Turno turno : sistema.getTurnos()) {
            boolean estaCancelado = turno.getEstadoTurno().getEstado().equalsIgnoreCase("Cancelado");

            if (!estaCancelado) {
                comboTurnos.addItem(turno);
            }
        }

        if (comboTurnos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay turnos activos para cancelar.",
                    "Cancelar turno",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblSeleccion = new JLabel("Seleccione el turno que desea cancelar:");

        panel.add(lblSeleccion, BorderLayout.NORTH);
        panel.add(comboTurnos, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cancelar turno",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Turno turnoSeleccionado = (Turno) comboTurnos.getSelectedItem();

                if (turnoSeleccionado != null) {
                    sistema.cancelarTurno(turnoSeleccionado.getIdTurno());

                    JOptionPane.showMessageDialog(
                            this,
                            "Turno cancelado correctamente.",
                            "Cancelación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (RegistroNoEncontradoException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Turno no encontrado",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}