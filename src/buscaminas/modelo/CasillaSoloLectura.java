package buscaminas.modelo;

/**
 * Lo que la vista puede consultar de una casilla, sin poder modificarla.
 * <p>
 * El tablero entrega sus casillas a través de esta interfaz para que la vista
 * pueda dibujarlas pero no cambiar su estado (encapsulamiento).
 */
public interface CasillaSoloLectura {

    /** @return posición de la casilla en el tablero */
    Coordenada getPosicion();

    /** @return estado actual: cubierta, marcada o descubierta */
    EstadoCasilla getEstado();

    /** @return {@code true} si la casilla contiene una mina */
    boolean esMina();

    /**
     * Símbolo que se muestra cuando la casilla está descubierta:
     * {@code X} para una mina, {@code V} para un espacio vacío o el número de minas vecinas.
     *
     * @return carácter que representa el contenido de la casilla
     */
    char simboloDescubierto();

    /** @return {@code true} si la casilla sigue cubierta (sin marcar) */
    default boolean estaCubierta() {
        return getEstado() == EstadoCasilla.CUBIERTA;
    }

    /** @return {@code true} si el jugador la marcó como posible mina */
    default boolean estaMarcada() {
        return getEstado() == EstadoCasilla.MARCADA;
    }

    /** @return {@code true} si ya fue descubierta */
    default boolean estaDescubierta() {
        return getEstado() == EstadoCasilla.DESCUBIERTA;
    }
}
