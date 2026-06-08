package excepciones;

/**
 * Excepción personalizada utilizada cuando no se encuentra un registro
 */
public class RegistroNoEncontradoException extends Exception {

    /**
     * Constructor que recibe el mensaje de error
     */
    public RegistroNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}