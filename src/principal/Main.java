package principal;

import vista.VentanaPrincipal;

import javax.swing.*;

/**
 * Clase principal del sistema. Contiene el metodo main, que es el punto de entrada de la aplicación.
 */
public class Main {

    /**
     * Metodo principal que inicia la aplicación
     * Se utiliza SwingUtilities.invokeLater para asegurar que la interfaz gráfica se ejecute correctamente
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}