package modelo;

/**
 * Clase que representa una especialidad médica dentro del sistema
 * Una especialidad puede estar asociada a uno o varios médicos
 */
public class Especialidad {

    // Atributos privados para aplicar encapsulamiento.
    private int idEspecialidad;
    private String nombre;
    private String descripcion;

    /**
     * Constructor de la clase Especialidad
     * Permite inicializar la especialidad con su identificador, nombre y descripción.
     */
    public Especialidad(int idEspecialidad, String nombre, String descripcion) {
        this.idEspecialidad = idEspecialidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Métodos getter y setter para acceder y modificar los atributos privados
    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el nombre de la especialidad como texto
     */
    @Override
    public String toString() {
        return nombre;
    }
}