package buscaminas.modelo.excepciones;

/**
 * Excepción base de todas las situaciones propias del juego.
 * <p>
 * Es una excepción comprobada (checked): obliga a quien hace una jugada a decidir
 * qué hacer si la jugada no es válida, en lugar de dejar que el programa se caiga.
 */
public class BuscaminasException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación del problema que se mostrará al jugador
     */
    public BuscaminasException(String mensaje) {
        super(mensaje);
    }

    /**
     * @param mensaje explicación del problema que se mostrará al jugador
     * @param causa   excepción original que provocó este error
     */
    public BuscaminasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
