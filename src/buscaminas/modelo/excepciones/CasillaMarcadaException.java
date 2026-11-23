package buscaminas.modelo.excepciones;

/**
 * Se lanza cuando el jugador intenta descubrir una casilla que él mismo marcó como
 * posible mina. Es la protección que pide el enunciado para no pisar una mina por accidente:
 * primero debe quitar la marca.
 */
public class CasillaMarcadaException extends BuscaminasException {

    private static final long serialVersionUID = 1L;

    /**
     * @param coordenadaTexto casilla marcada (por ejemplo "C7")
     */
    public CasillaMarcadaException(String coordenadaTexto) {
        super("La casilla " + coordenadaTexto + " está marcada como mina; quite la marca antes de descubrirla");
    }
}
