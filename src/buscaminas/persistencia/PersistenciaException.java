package buscaminas.persistencia;

/**
 * Error al guardar o cargar una partida. Envuelve las excepciones de bajo nivel
 * ({@link java.io.IOException}, {@link ClassNotFoundException}) con un mensaje
 * que el jugador pueda entender.
 */
public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación para el jugador
     */
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    /**
     * @param mensaje explicación para el jugador
     * @param causa   excepción original
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
