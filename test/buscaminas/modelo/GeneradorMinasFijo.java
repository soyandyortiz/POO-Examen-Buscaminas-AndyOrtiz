package buscaminas.modelo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Generador para las pruebas: coloca las minas en las coordenadas indicadas,
 * así cada prueba sabe exactamente cómo es el tablero.
 */
public class GeneradorMinasFijo implements GeneradorMinas {

    private final Set<Coordenada> minas = new LinkedHashSet<>();

    /**
     * @param coordenadas minas escritas como en el juego, por ejemplo "A1", "C3"
     */
    public GeneradorMinasFijo(String... coordenadas) {
        for (String texto : coordenadas) {
            int fila = Character.toUpperCase(texto.charAt(0)) - 'A';
            int columna = Integer.parseInt(texto.substring(1)) - 1;
            minas.add(new Coordenada(fila, columna));
        }
    }

    @Override
    public Set<Coordenada> generar(int filas, int columnas, int cantidad) {
        return minas;
    }
}
