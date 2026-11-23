package buscaminas.modelo.excepciones;

/**
 * Se lanza cuando el texto que escribe el jugador no tiene el formato
 * de una coordenada (una letra de fila seguida de un número de columna, por ejemplo A5).
 */
public class CoordenadaInvalidaException extends BuscaminasException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación del formato incorrecto
     */
    public CoordenadaInvalidaException(String mensaje) {
        super(mensaje);
    }

    /**
     * @param mensaje explicación del problema
     * @param causa   excepción original
     */
    public CoordenadaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
