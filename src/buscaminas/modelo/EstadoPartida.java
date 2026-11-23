package buscaminas.modelo;

/** Situación de la partida. */
public enum EstadoPartida {
    /** El jugador todavía puede hacer jugadas. */
    EN_CURSO,
    /** Se descubrieron todas las casillas seguras. */
    GANADA,
    /** Se descubrió una mina. */
    PERDIDA
}
