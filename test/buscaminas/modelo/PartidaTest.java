package buscaminas.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.modelo.excepciones.JuegoTerminadoException;

@DisplayName("Partida: reglas de victoria y derrota")
class PartidaTest {

    private Partida partida;

    @BeforeEach
    void preparar() {
        // tablero 3x3 con una mina en la esquina C3
        partida = new Partida(new Jugador("Andy"), new Tablero(3, 3, 1, new GeneradorMinasFijo("C3")));
    }

    @Test
    void empiezaEnCurso() {
        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());
        assertFalse(partida.estaTerminada());
        assertNotNull(partida.getFechaInicio());
        assertEquals("Andy", partida.getJugador().getNombre());
    }

    @Test
    @DisplayName("Derrota: descubrir una mina termina la partida y muestra las minas")
    void pierdeAlDescubrirUnaMina() throws BuscaminasException {
        assertEquals(ResultadoDescubrimiento.MINA, partida.descubrir(new Coordenada(2, 2)));
        assertEquals(EstadoPartida.PERDIDA, partida.getEstado());
        assertTrue(partida.getTablero().casillaEn(2, 2).estaDescubierta());
    }

    @Test
    @DisplayName("Victoria: descubrir todas las casillas seguras gana la partida")
    void ganaAlDescubrirTodasLasSeguras() throws BuscaminasException {
        partida.descubrir(new Coordenada(0, 0)); // A1 es vacía y abre casi todo
        assertEquals(EstadoPartida.GANADA, partida.getEstado());
        assertEquals(1, partida.getJugador().getJugadas());
    }

    @Test
    void sigueEnCursoMientrasFaltenCasillas() throws BuscaminasException {
        partida.descubrir(new Coordenada(1, 1)); // B2 tiene un número
        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());
    }

    @Test
    void noSePuedeJugarDespuesDeTerminar() throws BuscaminasException {
        partida.descubrir(new Coordenada(2, 2));
        assertThrows(JuegoTerminadoException.class, () -> partida.descubrir(new Coordenada(0, 0)));
        assertThrows(JuegoTerminadoException.class, () -> partida.alternarMarca(new Coordenada(0, 0)));
    }

    @Test
    void lasBanderasDescuentanMinasPendientes() throws BuscaminasException {
        assertEquals(1, partida.getMinasPendientes());
        assertTrue(partida.alternarMarca(new Coordenada(2, 2)));
        assertEquals(0, partida.getMinasPendientes());
        assertTrue(partida.alternarMarca(new Coordenada(0, 0)));
        assertEquals(-1, partida.getMinasPendientes());
        assertFalse(partida.alternarMarca(new Coordenada(0, 0)));
    }

    @Test
    void necesitaJugadorYTablero() {
        Tablero tablero = new Tablero(3, 3, 1, new GeneradorMinasFijo("C3"));
        assertThrows(IllegalArgumentException.class, () -> new Partida(null, tablero));
        assertThrows(IllegalArgumentException.class, () -> new Partida(new Jugador("Andy"), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "Un nombre demasiado largo para el tablero"})
    void elJugadorNecesitaUnNombreValido(String nombre) {
        assertThrows(IllegalArgumentException.class, () -> new Jugador(nombre));
    }

    @Test
    void elNombreSeGuardaSinEspaciosSobrantes() {
        assertEquals("Andy", new Jugador("  Andy ").getNombre());
        assertThrows(IllegalArgumentException.class, () -> new Jugador(null));
    }
}
