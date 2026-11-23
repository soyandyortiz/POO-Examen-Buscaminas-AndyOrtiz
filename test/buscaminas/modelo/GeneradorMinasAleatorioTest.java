package buscaminas.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class GeneradorMinasAleatorioTest {

    @RepeatedTest(20)
    void colocaDiezMinasDistintasDentroDelTablero() {
        Set<Coordenada> minas = new GeneradorMinasAleatorio().generar(10, 10, 10);
        assertEquals(10, minas.size());
        for (Coordenada mina : minas) {
            assertTrue(mina.getFila() >= 0 && mina.getFila() < 10);
            assertTrue(mina.getColumna() >= 0 && mina.getColumna() < 10);
        }
    }

    @Test
    void conLaMismaSemillaSaleElMismoTablero() {
        assertEquals(new GeneradorMinasAleatorio(new Random(7)).generar(10, 10, 10),
                new GeneradorMinasAleatorio(new Random(7)).generar(10, 10, 10));
        assertNotEquals(new GeneradorMinasAleatorio(new Random(7)).generar(10, 10, 10),
                new GeneradorMinasAleatorio(new Random(8)).generar(10, 10, 10));
    }

    @Test
    void rechazaCantidadesImposibles() {
        GeneradorMinasAleatorio generador = new GeneradorMinasAleatorio();
        assertThrows(IllegalArgumentException.class, () -> generador.generar(3, 3, 10));
        assertThrows(IllegalArgumentException.class, () -> generador.generar(3, 3, -1));
    }
}
