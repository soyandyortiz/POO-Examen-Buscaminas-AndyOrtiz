package buscaminas.modelo;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import buscaminas.modelo.excepciones.CoordenadaInvalidaException;

/**
 * Posición de una casilla dentro del tablero.
 * <p>
 * Internamente usa índices desde 0 (fila 0 = letra A, columna 0 = número 1), que son
 * los que necesita la matriz. Hacia el jugador se muestra siempre con letra y número,
 * por ejemplo {@code A5}. Es inmutable: una vez creada no cambia.
 */
public final class Coordenada implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Una letra seguida de uno o dos dígitos, sin importar espacios intermedios. */
    private static final Pattern FORMATO = Pattern.compile("^([A-Z])\\s*(\\d{1,2})$");

    private final int fila;
    private final int columna;

    /**
     * @param fila    índice de fila desde 0 (A = 0)
     * @param columna índice de columna desde 0 (1 = 0)
     */
    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Convierte lo que escribe el jugador (por ejemplo "A5", "a5" o " j 10 ") en una coordenada.
     * Solo revisa el formato; si la casilla existe o no lo decide el tablero.
     *
     * @param texto coordenada escrita por el jugador
     * @return la coordenada equivalente con índices desde 0
     * @throws CoordenadaInvalidaException si el texto no es una letra seguida de un número
     */
    public static Coordenada desdeTexto(String texto) throws CoordenadaInvalidaException {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /** @return índice de fila desde 0 */
    public int getFila() {
        return fila;
    }

    /** @return índice de columna desde 0 */
    public int getColumna() {
        return columna;
    }

    /**
     * Devuelve la coordenada que está desplazada respecto a esta.
     * Se usa para recorrer las ocho casillas vecinas.
     *
     * @param deltaFila    desplazamiento en filas (-1, 0 o 1)
     * @param deltaColumna desplazamiento en columnas (-1, 0 o 1)
     * @return nueva coordenada desplazada
     */
    public Coordenada desplazar(int deltaFila, int deltaColumna) {
        return new Coordenada(fila + deltaFila, columna + deltaColumna);
    }

    /** @return la coordenada como la ve el jugador, por ejemplo "A5" */
    public String aTexto() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Coordenada c && c.fila == fila && c.columna == columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return aTexto();
    }
}
