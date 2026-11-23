package buscaminas.modelo.excepciones;

/**
 * Se lanza cuando una coordenada tiene buen formato pero queda fuera del tablero
 * (por ejemplo K5 o A11 en un tablero de 10x10). Normalmente envuelve la
 * {@link ArrayIndexOutOfBoundsException} producida al acceder a la matriz.
 */
public class CoordenadaFueraDeRangoException extends CoordenadaInvalidaException {

    private static final long serialVersionUID = 1L;

    /**
     * @param coordenadaTexto coordenada escrita por el jugador (por ejemplo "K5")
     * @param causa           excepción de índice fuera de rango que se produjo
     */
    public CoordenadaFueraDeRangoException(String coordenadaTexto, Throwable causa) {
        super("La casilla " + coordenadaTexto + " está fuera del tablero", causa);
    }
}
