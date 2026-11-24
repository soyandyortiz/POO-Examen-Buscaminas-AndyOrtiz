package buscaminas.controlador;

import buscaminas.modelo.excepciones.BuscaminasException;

/** Se lanza cuando el jugador escribe una jugada que el juego no reconoce. */
public class ComandoInvalidoException extends BuscaminasException {

    private static final long serialVersionUID = 1L;

    /**
     * @param texto lo que escribió el jugador
     */
    public ComandoInvalidoException(String texto) {
        super("No se reconoce la jugada '" + texto + "'. Escriba ? para ver la ayuda");
    }
}
