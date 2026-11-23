package buscaminas.modelo;

/** Lo que ocurre al descubrir una casilla. Cada subclase de {@link Casilla} decide cuál devuelve. */
public enum ResultadoDescubrimiento {
    /** Había una mina: el jugador pierde. */
    MINA,
    /** Casilla segura con minas alrededor: se muestra el número. */
    NUMERO,
    /** Casilla segura sin minas alrededor: se descubren también sus vecinas. */
    VACIA
}
