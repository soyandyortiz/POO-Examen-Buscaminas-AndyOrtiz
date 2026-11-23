package buscaminas.modelo;

/** Los tres estados por los que puede pasar una casilla durante la partida. */
public enum EstadoCasilla {
    /** Todavía no se sabe qué hay debajo. */
    CUBIERTA,
    /** El jugador la marcó porque cree que tiene una mina. */
    MARCADA,
    /** Ya se descubrió y se muestra su contenido. */
    DESCUBIERTA
}
