package buscaminas.modelo.excepciones;

/**
 * Se lanza si se intenta hacer una jugada en una partida que ya terminó (ganada o perdida).
 */
public class JuegoTerminadoException extends BuscaminasException {

    private static final long serialVersionUID = 1L;

    /** Crea la excepción con el mensaje estándar. */
    public JuegoTerminadoException() {
        super("La partida ya terminó; inicie una nueva partida");
    }
}
