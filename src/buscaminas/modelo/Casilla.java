package buscaminas.modelo;

import java.io.Serializable;

import buscaminas.modelo.excepciones.CasillaMarcadaException;
import buscaminas.modelo.excepciones.CasillaYaDescubiertaException;

/**
 * Clase base de las casillas del tablero.
 * <p>
 * Guarda lo que tienen en común todas las casillas (posición y estado) y define
 * el proceso de descubrir una casilla con el patrón <i>método plantilla</i>:
 * {@link #descubrir()} hace las validaciones comunes y luego pregunta a la subclase
 * qué pasa al descubrirla mediante {@link #resultadoAlDescubrir()}. Gracias al
 * polimorfismo, el tablero no necesita saber si la casilla es una mina o no.
 * <p>
 * Los métodos que cambian el estado no son públicos: solo el paquete {@code modelo}
 * (es decir, el {@link Tablero}) puede modificar una casilla.
 */
public abstract class Casilla implements CasillaSoloLectura, Serializable {

    private static final long serialVersionUID = 1L;

    private final Coordenada posicion;
    private EstadoCasilla estado = EstadoCasilla.CUBIERTA;

    /**
     * @param posicion lugar que ocupa la casilla en el tablero
     */
    protected Casilla(Coordenada posicion) {
        this.posicion = posicion;
    }

    @Override
    public Coordenada getPosicion() {
        return posicion;
    }

    @Override
    public EstadoCasilla getEstado() {
        return estado;
    }

    /**
     * Descubre la casilla por pedido del jugador.
     *
     * @return qué había en la casilla (mina, número o vacía)
     * @throws CasillaYaDescubiertaException si ya estaba descubierta
     * @throws CasillaMarcadaException       si el jugador la había marcado
     */
    ResultadoDescubrimiento descubrir() throws CasillaYaDescubiertaException, CasillaMarcadaException {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /**
     * Pone o quita la marca de posible mina.
     *
     * @return {@code true} si quedó marcada, {@code false} si se quitó la marca
     * @throws CasillaYaDescubiertaException si la casilla ya está descubierta
     */
    boolean alternarMarca() throws CasillaYaDescubiertaException {
        throw new UnsupportedOperationException("TDD: pendiente de implementar");
    }

    /** Muestra la casilla al final de la partida sin aplicar las reglas de una jugada. */
    void revelar() {
        estado = EstadoCasilla.DESCUBIERTA;
    }

    /**
     * Cada tipo de casilla indica qué ocurre cuando se descubre (polimorfismo).
     *
     * @return resultado de descubrir esta casilla
     */
    protected abstract ResultadoDescubrimiento resultadoAlDescubrir();
}
