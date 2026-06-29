package modelo;

/**
 * Clase que representa el estado de un turno dentro del sistema
 * Ejemplos de estados posibles: Reservado, Cancelado o Atendido.
 */
public class EstadoTurno {

    // Atributos privados para aplicar encapsulamiento.
    private int idEstado;
    private String estado;

    /**
     * Constructor de la clase EstadoTurno.
     * Permite inicializar un estado con su identificador y descripción.
     */
    public EstadoTurno(int idEstado, String estado) {
        this.idEstado = idEstado;
        this.estado = estado;
    }

    // Métodos getter y setter para acceder y modificar los atributos privados.
    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el estado como texto.
     */
    @Override
    public String toString() {
        return estado;
    }
}