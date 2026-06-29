package modelo;

/**
 * Clase que representa a un paciente del consultorio médico.
 * Hereda datos comunes de la clase Persona.
 */
public class Paciente extends Persona {

    // Atributo del paciente
    private String email;

    /**
     * Constructor de la clase Paciente.
     * Utiliza super() para inicializar los atributos heredados de Persona y luego inicializa el atributo propio email.
     */
    public Paciente(int id, String nombre, String apellido, int dni, int telefono, String email) {
        super(id, nombre, apellido, dni, telefono);
        this.email = email;
    }

    //  Permiten obtener y actualizar el correo electrónico asociado al paciente
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Implementacion del metodo abstracto definido en Persona.
     * Esto permite aplicar polimorfismo, ya que cada clase hija puede devolver una descripción distinta
     */
    @Override
    public String obtenerDescripcion() {
        return "Paciente: " + getNombreCompleto() + " - DNI: " + getDni();
    }

    /**
     * Devuelve el nombre completo del pacient en formato tipo Texto
     * Este metodo se utiliza para mostrar pacientes en las listas o el ComboBox.
     */
    @Override
    public String toString() {
        return getNombreCompleto();
    }
}