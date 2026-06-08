package modelo;

/**
 * Clase que representa a un médico del consultorio.
 * Hereda datos comunes de la clase Persona.
 */
public class Medico extends Persona {

    // Atributos propios del médico.
    private String matricula;
    private Especialidad especialidad;

    /**
     * Constructor de la clase Medico.
     * Utiliza super() para inicializar los atributos heredados de Persona y luego inicializa matrícula y especialidad.
     */
    public Medico(int id, String nombre, String apellido, int dni, int telefono, String matricula, Especialidad especialidad) {
        super(id, nombre, apellido, dni, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    // Getter y setter de matrícula.
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    // Getter y setter de especialidad.
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Implementación del método abstracto definido en Persona.
     * Permite aplicar polimorfismo, ya que Medico devuelve una descripción propia distinta a la de Paciente.
     */
    @Override
    public String obtenerDescripcion() {
        return "Médico: " + getNombreCompleto() + " - Matrícula: " + matricula;
    }

    /**
     * Devuelve una representación tipo texto del médico.
     * Se utiliza para mostrar médicos en listas o en el ComboBox
     */
    @Override
    public String toString() {
        return getNombreCompleto() + " - " + especialidad.getNombre();
    }
}
