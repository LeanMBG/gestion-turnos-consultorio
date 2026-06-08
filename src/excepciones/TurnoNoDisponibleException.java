package excepciones;

/**
 * Excepción personalizada utilizada cuando se intenta asignar un turno en una fecha y horario donde el médico no posee disponibilidad.
 */
public class TurnoNoDisponibleException extends Exception {

    /**
     * Constructor que recibe el mensaje de error
     */
    public TurnoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}