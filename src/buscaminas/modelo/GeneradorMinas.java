package buscaminas.modelo;

import java.util.Set;

/**
 * Decide en qué casillas se colocan las minas.
 * <p>
 * El tablero depende de esta interfaz y no de una clase concreta (inversión de
 * dependencias): en el juego se usa {@link GeneradorMinasAleatorio} y en las pruebas
 * unitarias un generador con posiciones fijas, para que los resultados sean predecibles.
 */
@FunctionalInterface
public interface GeneradorMinas {

    /**
     * @param filas    filas del tablero
     * @param columnas columnas del tablero
     * @param cantidad número de minas a colocar
     * @return posiciones distintas donde irán las minas
     */
    Set<Coordenada> generar(int filas, int columnas, int cantidad);
}
