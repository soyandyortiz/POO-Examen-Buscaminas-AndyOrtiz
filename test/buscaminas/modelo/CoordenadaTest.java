package buscaminas.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import buscaminas.modelo.excepciones.CoordenadaInvalidaException;

@DisplayName("Coordenada: conversión entre texto del jugador e índices de la matriz")
class CoordenadaTest {

    @ParameterizedTest(name = "\"{0}\" -> fila {1}, columna {2}")
    @CsvSource({"A1, 0, 0", "A5, 0, 4", "J10, 9, 9", "c7, 2, 6", "' b 3 ', 1, 2"})
    void conviertePosicionesValidas(String texto, int fila, int columna) throws CoordenadaInvalidaException {
        Coordenada coordenada = Coordenada.desdeTexto(texto);
        assertEquals(fila, coordenada.getFila());
        assertEquals(columna, coordenada.getColumna());
    }

    @ParameterizedTest(name = "\"{0}\" no es una coordenada")
    @ValueSource(strings = {"5A", "AA", "A", "12", "A100", "#3", "A-1"})
    @NullAndEmptySource
    void rechazaFormatosInvalidos(String texto) {
        assertThrows(CoordenadaInvalidaException.class, () -> Coordenada.desdeTexto(texto));
    }

    @Test
    void elFormatoEsValidoAunqueQuedeFueraDelTablero() throws CoordenadaInvalidaException {
        // K11 tiene buen formato; que no exista lo decide el tablero, no la coordenada
        assertEquals(new Coordenada(10, 10), Coordenada.desdeTexto("K11"));
    }

    @Test
    void seMuestraComoLaVeElJugador() {
        assertEquals("A1", new Coordenada(0, 0).aTexto());
        assertEquals("J10", new Coordenada(9, 9).toString());
    }

    @Test
    void desplazarDevuelveLaVecina() {
        assertEquals(new Coordenada(1, 3), new Coordenada(2, 2).desplazar(-1, 1));
    }

    @Test
    void igualdadPorFilaYColumna() {
        assertEquals(new Coordenada(3, 4), new Coordenada(3, 4));
        assertEquals(new Coordenada(3, 4).hashCode(), new Coordenada(3, 4).hashCode());
        assertNotEquals(new Coordenada(3, 4), new Coordenada(4, 3));
        assertNotEquals(new Coordenada(3, 4), "D5");
    }
}
