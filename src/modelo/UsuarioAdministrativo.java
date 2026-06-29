package modelo;

/**
 * Clase que representa al usuario administrativo del sistema.
 * Este usuario es quien gestiona las operaciones principales del prototipo, como registrar pacientes, médicos, especialidades y turnos.
 */
public class UsuarioAdministrativo {

    // Atributos privados para aplicar encapsulamiento.
    private int idUsuario;
    private String usuario;
    private String clave;
    private String nombre;
    private String apellido;

    /**
     * Constructor de la clase UsuarioAdministrativo.
     * Permite inicializar un usuario con sus datos principales.
     */
    public UsuarioAdministrativo(int idUsuario, String usuario, String clave, String nombre, String apellido) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Métodos getter para obtener los datos del usuario.
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    // Métodos setter para modificar datos del usuario.
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve el nombre completo del usuario administrativo.
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Valida si el usuario y la clave ingresados coinciden con los datos almacenados en el objeto.
     */
    public boolean iniciarSesion(String usuarioIngresado, String claveIngresada) {
        return this.usuario.equals(usuarioIngresado) && this.clave.equals(claveIngresada);
    }

    /**
     * Metodo simple para representar el cierre de sesión.
     * Solo muestra el mensaje por consola.
     */
    public void cerrarSesion() {
        System.out.println("Sesión finalizada.");
    }

    /**
     * Devuelvo el nombre en formato tipo texto
     */
    @Override
    public String toString() {
        return getNombreCompleto();
    }
}