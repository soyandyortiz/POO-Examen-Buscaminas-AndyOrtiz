package buscaminas.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Coloca las minas en posiciones aleatorias y distintas.
 * <p>
 * Se arma la lista de todas las casillas, se baraja y se toman las primeras:
 * así nunca se repite una posición y no hace falta volver a intentar (KISS).
 */
public class GeneradorMinasAleatorio implements GeneradorMinas {

    private final Random aleatorio;

    /** Crea un generador con una semilla distinta en cada partida. */
    public GeneradorMinasAleatorio() {
        this(new Random());
    }

    /**
     * @param aleatorio fuente de números aleatorios (con semilla fija se puede repetir un tablero)
     */
    public GeneradorMinasAleatorio(Random aleatorio) {
        this.aleatorio = aleatorio;
    }

    @Override
    public Set<Coordenada> generar(int filas, int columnas, int cantidad) {
        if (cantidad < 0 || cantidad > filas * columnas) {
            throw new IllegalArgumentException("No se pueden colocar " + cantidad + " minas en "
                    + (filas * columnas) + " casillas");
        }
        List<Coordenada> todas = new ArrayList<>();
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                todas.add(new Coordenada(fila, columna));
            }
        }
        Collections.shuffle(todas, aleatorio);
        return new HashSet<>(todas.subList(0, cantidad));
    }
}
