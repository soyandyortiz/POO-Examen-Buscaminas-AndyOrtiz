package buscaminas.modelo.excepciones;

/**
 * Se lanza cuando el jugador intenta descubrir o marcar una casilla que ya fue descubierta.
 */
public class CasillaYaDescubiertaException extends BuscaminasException {

    private static final long serialVersionUID = 1L;

    /**
     * @param coordenadaTexto casilla que ya estaba descubierta (por ejemplo "B3")
     */
    public CasillaYaDescubiertaException(String coordenadaTexto) {
        super("La casilla " + coordenadaTexto + " ya está descubierta");
    }
}
