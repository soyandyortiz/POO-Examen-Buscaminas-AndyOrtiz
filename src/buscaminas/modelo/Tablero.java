package buscaminas.modelo;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

import buscaminas.modelo.excepciones.CasillaMarcadaException;
import buscaminas.modelo.excepciones.CasillaYaDescubiertaException;
import buscaminas.modelo.excepciones.CoordenadaFueraDeRangoException;

/**
 * Tablero del Buscaminas: una matriz de casillas con minas colocadas al azar.
 * <p>
 * Se relaciona con {@link Casilla} por composición: el tablero crea sus casillas y
 * estas no existen fuera de él. Para colocar las minas usa un {@link GeneradorMinas},
 * del que solo depende mientras se construye.
 * <p>
 * Las operaciones que cambian el tablero no son públicas; se usan a través de
 * {@link Partida}, que es quien controla las reglas del juego.
 */
public final class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Tamaño fijo que pide el enunciado. */
    public static final int FILAS = 10;
    /** Tamaño fijo que pide el enunciado. */
    public static final int COLUMNAS = 10;
    /** Cantidad fija de minas que pide el enunciado. */
    public static final int MINAS = 10;

    private static final int MAXIMO_FILAS = 26; // una letra por fila (A-Z)

    private final Casilla[][] casillas;
    private final int totalMinas;
    private int segurasDescubiertas;

    /**
     * Crea el tablero estándar de 10x10 con 10 minas.
     *
     * @param generador estrategia que decide dónde van las minas
     */
    public Tablero(GeneradorMinas generador) {
        this(FILAS, COLUMNAS, MINAS, generador);
    }

    /**
     * Crea un tablero de cualquier tamaño (se usa sobre todo en las pruebas).
     *
     * @param filas     número de filas (1 a 26)
     * @param columnas  número de columnas (1 a 99)
     * @param minas     número de minas (al menos 1 y menos que el total de casillas)
     * @param generador estrategia que decide dónde van las minas
     */
    public Tablero(int filas, int columnas, int minas, GeneradorMinas generador) {
        validarDimensiones(filas, columnas, minas);
        this.casillas = new Casilla[filas][columnas];
        this.totalMinas = minas;
        Set<Coordenada> posicionesMinas = generador.generar(filas, columnas, minas);
        validarMinasGeneradas(posicionesMinas, filas, columnas, minas);
        colocarCasillas(posicionesMinas);
    }

    private static void validarDimensiones(int filas, int columnas, int minas) {
        if (filas < 1 || filas > MAXIMO_FILAS || columnas < 1 || columnas > 99) {
            throw new IllegalArgumentException("Tamaño de tablero no permitido: " + filas + "x" + columnas);
        }
        if (minas < 1 || minas >= filas * columnas) {
            throw new IllegalArgumentException("La cantidad de minas debe estar entre 1 y " + (filas * columnas - 1));
        }
    }

    private static void validarMinasGeneradas(Set<Coordenada> minas, int filas, int columnas, int esperadas) {
        if (minas == null || minas.size() != esperadas) {
            throw new IllegalArgumentException("El generador debía colocar " + esperadas + " minas distintas");
        }
        for (Coordenada mina : minas) {
            if (!estaDentro(mina, filas, columnas)) {
                throw new IllegalArgumentException("El generador colocó una mina fuera del tablero: " + mina);
            }
        }
    }

    /** Crea cada casilla: una mina o una casilla segura con su número de minas vecinas. */
    private void colocarCasillas(Set<Coordenada> posicionesMinas) {
        for (int fila = 0; fila < getFilas(); fila++) {
            for (int columna = 0; columna < getColumnas(); columna++) {
                Coordenada posicion = new Coordenada(fila, columna);
                casillas[fila][columna] = posicionesMinas.contains(posicion)
                        ? new CasillaMina(posicion)
                        : new CasillaSegura(posicion, contarMinasVecinas(posicion, posicionesMinas));
            }
        }
    }

    private int contarMinasVecinas(Coordenada posicion, Set<Coordenada> posicionesMinas) {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    // ================================================================ jugadas

    /**
     * Descubre una casilla. Si está vacía, descubre también las vecinas vacías y sus
     * bordes numerados, como en el Buscaminas original.
     *
     * @param coordenada casilla elegida por el jugador
     * @return qué había en la casilla elegida
     * @throws CoordenadaFueraDeRangoException si la coordenada no existe en el tablero
     * @throws CasillaYaDescubiertaException  si ya estaba descubierta
     * @throws CasillaMarcadaException        si el jugador la había marcado
     */
    ResultadoDescubrimiento descubrir(Coordenada coordenada)
            throws CoordenadaFueraDeRangoException, CasillaYaDescubiertaException, CasillaMarcadaException {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /**
     * Descubre en cadena las casillas alrededor de una casilla vacía.
     * Se usa una cola en lugar de recursión para que el recorrido sea fácil de seguir
     * y no dependa de la profundidad de la pila.
     */
    private void expandirDesde(Coordenada inicio) {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /**
     * Pone o quita la marca de posible mina en una casilla.
     *
     * @param coordenada casilla elegida
     * @return {@code true} si quedó marcada
     * @throws CoordenadaFueraDeRangoException si la coordenada no existe en el tablero
     * @throws CasillaYaDescubiertaException  si la casilla ya está descubierta
     */
    boolean alternarMarca(Coordenada coordenada)
            throws CoordenadaFueraDeRangoException, CasillaYaDescubiertaException {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /** Descubre todas las minas para mostrarlas cuando el jugador pierde. */
    void revelarMinas() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /**
     * Busca una casilla a partir de lo que eligió el jugador. Si los índices salen de la
     * matriz, Java lanza {@link ArrayIndexOutOfBoundsException}; aquí se captura y se
     * convierte en un error del juego con un mensaje claro.
     */
    private Casilla obtener(Coordenada coordenada) throws CoordenadaFueraDeRangoException {
        try {
            return casillas[coordenada.getFila()][coordenada.getColumna()];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CoordenadaFueraDeRangoException(coordenada.aTexto(), e);
        }
    }

    // ================================================================ consultas

    private static boolean estaDentro(Coordenada c, int filas, int columnas) {
        return c.getFila() >= 0 && c.getFila() < filas && c.getColumna() >= 0 && c.getColumna() < columnas;
    }

    /**
     * Permite a la vista leer una casilla sin poder modificarla.
     *
     * @param fila    índice de fila desde 0
     * @param columna índice de columna desde 0
     * @return la casilla en modo solo lectura
     */
    public CasillaSoloLectura casillaEn(int fila, int columna) {
        return casillas[fila][columna];
    }

    /** @return {@code true} cuando todas las casillas sin mina están descubiertas (victoria) */
    public boolean todasLasSegurasDescubiertas() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /** @return número de filas */
    public int getFilas() {
        return casillas.length;
    }

    /** @return número de columnas */
    public int getColumnas() {
        return casillas[0].length;
    }

    /** @return cantidad de minas del tablero */
    public int getTotalMinas() {
        return totalMinas;
    }

    /** @return cantidad de casillas que no tienen mina */
    public int getTotalSeguras() {
        return getFilas() * getColumnas() - totalMinas;
    }

    /** @return casillas seguras que ya se descubrieron */
    public int getSegurasDescubiertas() {
        return segurasDescubiertas;
    }

    /** @return cuántas casillas marcó el jugador */
    public int contarMarcas() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }
}
