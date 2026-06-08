package excepciones;

/**
 * Excepción utilizada cuando el usuario ingresa datos inválidos o incompletos en los formularios del sistema
 */
public class DatoInvalidoException extends Exception {

    /**
     * Constructor que recibe el mensaje de error
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}