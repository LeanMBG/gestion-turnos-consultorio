package modelo;

/**
 * Clase que representa una persona dentro del sistema.
 * Contiene atributos comunes para pacientes y medicos
 * Se utiliza como base para aplicar herencia, Paciente y Medico comparten datos como nombre, apellido, DNI y telefono.
 */
public abstract class Persona {

    // Atributos privados para aplicar encapsulamiento.
    private int id;
    private String nombre;
    private String apellido;
    private int dni;
    private int telefono;

    /**
     * Constructor de la clase Persona. Permite inicializar los datos comunes de una persona.
     */
    public Persona(int id, String nombre, String apellido, int dni, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    // Metodos getter: para acceder a los datos privados de la clase
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getDni() {
        return dni;
    }

    public int getTelefono() {
        return telefono;
    }

    // Metodos setter: permite modificar algunos datos de la persona
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve el nombre y apellido unidos. Puede ser reutilizado por clases hijas
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Metodo abstracto para implementar por las clases hijas.
     * Permite aplicar polimorfismo. Paciente y Medico pueden devolver una descripcion diferente.
     */
    public abstract String obtenerDescripcion();
}