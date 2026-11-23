package buscaminas.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.modelo.excepciones.JuegoTerminadoException;

/**
 * Una partida de Buscaminas: reúne al {@link Jugador} con su {@link Tablero} y aplica
 * las reglas de victoria y derrota.
 * <p>
 * Es el único punto de entrada para modificar el juego, por eso es lo que se guarda
 * en el archivo (implementa {@link Serializable}) y lo que usa el controlador.
 */
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Jugador jugador;
    private final Tablero tablero;
    private final LocalDateTime fechaInicio;
    private EstadoPartida estado = EstadoPartida.EN_CURSO;

    /**
     * @param jugador quien juega
     * @param tablero tablero ya creado con sus minas
     */
    public Partida(Jugador jugador, Tablero tablero) {
        if (jugador == null || tablero == null) {
            throw new IllegalArgumentException("Una partida necesita un jugador y un tablero");
        }
        this.jugador = jugador;
        this.tablero = tablero;
        this.fechaInicio = LocalDateTime.now();
    }

    /**
     * Descubre una casilla y actualiza el estado de la partida.
     *
     * @param coordenada casilla elegida por el jugador
     * @return qué había en la casilla
     * @throws BuscaminasException si la partida terminó, la coordenada no existe,
     *                             la casilla ya estaba descubierta o está marcada
     */
    public ResultadoDescubrimiento descubrir(Coordenada coordenada) throws BuscaminasException {
        verificarEnCurso();
        ResultadoDescubrimiento resultado = tablero.descubrir(coordenada);
        jugador.registrarJugada();
        if (resultado == ResultadoDescubrimiento.MINA) {
            estado = EstadoPartida.PERDIDA;
            tablero.revelarMinas();
        } else if (tablero.todasLasSegurasDescubiertas()) {
            estado = EstadoPartida.GANADA;
        }
        return resultado;
    }

    /**
     * Pone o quita la marca de posible mina.
     *
     * @param coordenada casilla elegida por el jugador
     * @return {@code true} si quedó marcada, {@code false} si se quitó la marca
     * @throws BuscaminasException si la partida terminó, la coordenada no existe o ya está descubierta
     */
    public boolean alternarMarca(Coordenada coordenada) throws BuscaminasException {
        verificarEnCurso();
        return tablero.alternarMarca(coordenada);
    }

    private void verificarEnCurso() throws JuegoTerminadoException {
        if (estaTerminada()) {
            throw new JuegoTerminadoException();
        }
    }

    /** @return {@code true} si la partida ya se ganó o se perdió */
    public boolean estaTerminada() {
        return estado != EstadoPartida.EN_CURSO;
    }

    /** @return minas que faltan por marcar según las marcas del jugador (puede ser negativo) */
    public int getMinasPendientes() {
        return tablero.getTotalMinas() - tablero.contarMarcas();
    }

    /** @return estado actual de la partida */
    public EstadoPartida getEstado() {
        return estado;
    }

    /** @return jugador de la partida */
    public Jugador getJugador() {
        return jugador;
    }

    /** @return tablero de la partida (sus métodos públicos son solo de consulta) */
    public Tablero getTablero() {
        return tablero;
    }

    /** @return momento en que empezó la partida */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
}
