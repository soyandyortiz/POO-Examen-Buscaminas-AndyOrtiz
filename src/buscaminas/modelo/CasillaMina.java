package buscaminas.modelo;

/** Casilla que contiene una mina. Descubrirla hace perder la partida. */
public class CasillaMina extends Casilla {

    private static final long serialVersionUID = 1L;

    /** Símbolo que pide el enunciado para la ubicación de una mina. */
    public static final char SIMBOLO = 'X';

    /**
     * @param posicion lugar de la mina en el tablero
     */
    public CasillaMina(Coordenada posicion) {
        super(posicion);
    }

    @Override
    public boolean esMina() {
        return true;
    }

    @Override
    public char simboloDescubierto() {
        return SIMBOLO;
    }

    @Override
    protected ResultadoDescubrimiento resultadoAlDescubrir() {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }
}
