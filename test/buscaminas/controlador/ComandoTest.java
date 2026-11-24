package buscaminas.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import buscaminas.modelo.Coordenada;
import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.modelo.excepciones.CoordenadaInvalidaException;

@DisplayName("Comando: interpretación de lo que escribe el jugador")
class ComandoTest {

    @ParameterizedTest(name = "\"{0}\" -> {1} {2}")
    @CsvSource({"A5, DESCUBRIR, A5", "d b3, DESCUBRIR, B3", "M A5, MARCAR, A5", "ma5, MARCAR, A5",
            "D5, DESCUBRIR, D5", "M5, DESCUBRIR, M5", "j10, DESCUBRIR, J10"})
    void interpretaJugadasConCoordenada(String texto, Comando.Tipo tipo, String coordenada)
            throws BuscaminasException {
        Comando comando = Comando.desdeTexto(texto);
        assertEquals(tipo, comando.tipo());
        assertEquals(Coordenada.desdeTexto(coordenada), comando.coordenada());
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({"G, GUARDAR", "g, GUARDAR", "S, SALIR", "' s ', SALIR", "?, AYUDA"})
    void interpretaComandosSinCoordenada(String texto, Comando.Tipo tipo) throws BuscaminasException {
        Comando comando = Comando.desdeTexto(texto);
        assertEquals(tipo, comando.tipo());
        assertNull(comando.coordenada());
    }

    @ParameterizedTest
    @ValueSource(strings = {"hola", "M", "MA", "5", "A 5 6"})
    void rechazaCoordenadasMalEscritas(String texto) {
        assertThrows(CoordenadaInvalidaException.class, () -> Comando.desdeTexto(texto));
    }

    @Test
    void rechazaUnaJugadaVacia() {
        assertThrows(ComandoInvalidoException.class, () -> Comando.desdeTexto("  "));
        assertThrows(ComandoInvalidoException.class, () -> Comando.desdeTexto(null));
    }
}
