package buscaminas.modelo;

/**
 * Casilla sin mina. Conoce cuántas minas tiene alrededor (incluidas las diagonales);
 * ese número se calcula una sola vez al crear el tablero.
 */
public class CasillaSegura extends Casilla {

    private static final long serialVersionUID = 1L;

    /** Símbolo que pide el enunciado para un espacio vacío descubierto. */
    public static final char SIMBOLO_VACIA = 'V';

    private final int minasAdyacentes;

    /**
     * @param posicion        lugar de la casilla en el tablero
     * @param minasAdyacentes cantidad de minas en las ocho casillas vecinas (0 a 8)
     */
    public CasillaSegura(Coordenada posicion, int minasAdyacentes) {
        super(posicion);
        if (minasAdyacentes < 0 || minasAdyacentes > 8) {
            throw new IllegalArgumentException("Una casilla tiene entre 0 y 8 minas vecinas, no " + minasAdyacentes);
        }
        this.minasAdyacentes = minasAdyacentes;
    }

    /** @return cantidad de minas en las casillas vecinas */
    public int getMinasAdyacentes() {
        return minasAdyacentes;
    }

    /** @return {@code true} si no hay minas alrededor */
    public boolean esVacia() {
        return minasAdyacentes == 0;
    }

    @Override
    public boolean esMina() {
        return false;
    }

    @Override
    public char simboloDescubierto() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    @Override
    protected ResultadoDescubrimiento resultadoAlDescubrir() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }
}
