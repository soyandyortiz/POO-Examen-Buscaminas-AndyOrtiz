package buscaminas.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.modelo.excepciones.CasillaMarcadaException;
import buscaminas.modelo.excepciones.CasillaYaDescubiertaException;
import buscaminas.modelo.excepciones.CoordenadaFueraDeRangoException;

@DisplayName("Tablero: construcción, números, expansión y fin del juego")
class TableroTest {

    private static Coordenada c(String texto) {
        return new Coordenada(texto.charAt(0) - 'A', Integer.parseInt(texto.substring(1)) - 1);
    }

    /** Tablero 10x10 con las 10 minas en posiciones conocidas. */
    private static Tablero tableroConocido() {
        return new Tablero(new GeneradorMinasFijo("A1", "A2", "B1", "C5", "E5", "E6", "J10", "J9", "I10", "G2"));
    }

    @Nested
    @DisplayName("Construcción")
    class Construccion {

        @Test
        void elTableroEstandarEsDe10x10Con10Minas() {
            Tablero tablero = tableroConocido();
            assertEquals(10, tablero.getFilas());
            assertEquals(10, tablero.getColumnas());
            assertEquals(10, tablero.getTotalMinas());
            assertEquals(90, tablero.getTotalSeguras());
        }

        @Test
        void colocaLasMinasDondeIndicaElGenerador() {
            Tablero tablero = tableroConocido();
            assertTrue(tablero.casillaEn(0, 0).esMina());
            assertTrue(tablero.casillaEn(9, 9).esMina());
            assertFalse(tablero.casillaEn(5, 5).esMina());
            assertInstanceOf(CasillaMina.class, tablero.casillaEn(2, 4));
            assertInstanceOf(CasillaSegura.class, tablero.casillaEn(2, 3));
        }

        @Test
        void todasLasCasillasEmpiezanCubiertas() {
            Tablero tablero = tableroConocido();
            for (int fila = 0; fila < 10; fila++) {
                for (int columna = 0; columna < 10; columna++) {
                    assertTrue(tablero.casillaEn(fila, columna).estaCubierta());
                }
            }
        }

        @Test
        @DisplayName("Cuenta las minas vecinas incluyendo diagonales")
        void cuentaMinasVecinas() {
            Tablero tablero = tableroConocido();
            assertEquals(3, ((CasillaSegura) tablero.casillaEn(1, 1)).getMinasAdyacentes()); // B2: A1, A2, B1
            assertEquals(3, ((CasillaSegura) tablero.casillaEn(3, 4)).getMinasAdyacentes()); // D5: C5, E5, E6
            assertEquals(3, ((CasillaSegura) tablero.casillaEn(8, 8)).getMinasAdyacentes()); // I9: J10, J9, I10
            assertEquals(0, ((CasillaSegura) tablero.casillaEn(5, 9)).getMinasAdyacentes()); // F10
        }

        @Test
        void rechazaConfiguracionesInvalidas() {
            GeneradorMinas generador = new GeneradorMinasFijo("A1");
            assertThrows(IllegalArgumentException.class, () -> new Tablero(0, 5, 1, generador));
            assertThrows(IllegalArgumentException.class, () -> new Tablero(27, 5, 1, generador));
            assertThrows(IllegalArgumentException.class, () -> new Tablero(3, 3, 0, generador));
            assertThrows(IllegalArgumentException.class, () -> new Tablero(3, 3, 9, generador));
        }

        @Test
        void rechazaGeneradoresQueNoCumplen() {
            assertThrows(IllegalArgumentException.class, () -> new Tablero(new GeneradorMinasFijo("A1")));
            assertThrows(IllegalArgumentException.class, () -> new Tablero(3, 3, 1, (f, c, n) -> Set.of(new Coordenada(5, 5))));
            assertThrows(IllegalArgumentException.class, () -> new Tablero(3, 3, 1, (f, c, n) -> null));
        }
    }

    @Nested
    @DisplayName("Jugadas")
    class Jugadas {

        @Test
        void descubrirUnNumeroSoloAbreEsaCasilla() throws BuscaminasException {
            Tablero tablero = tableroConocido();
            assertEquals(ResultadoDescubrimiento.NUMERO, tablero.descubrir(c("B2")));
            assertEquals(1, tablero.getSegurasDescubiertas());
            assertTrue(tablero.casillaEn(1, 1).estaDescubierta());
            assertTrue(tablero.casillaEn(1, 2).estaCubierta());
        }

        @Test
        @DisplayName("Una casilla vacía abre en cadena las vecinas hasta los números")
        void descubrirUnaVaciaExpande() throws BuscaminasException {
            Tablero tablero = new Tablero(5, 5, 1, new GeneradorMinasFijo("E5"));
            assertEquals(ResultadoDescubrimiento.VACIA, tablero.descubrir(c("A1")));
            assertEquals(24, tablero.getSegurasDescubiertas());
            assertTrue(tablero.todasLasSegurasDescubiertas());
            assertTrue(tablero.casillaEn(4, 4).estaCubierta());
            assertEquals('1', tablero.casillaEn(3, 3).simboloDescubierto());
        }

        @Test
        void laExpansionSeDetieneEnLosNumeros() throws BuscaminasException {
            // una fila completa de minas (C) separa la parte de arriba del resto
            Tablero tablero = new Tablero(5, 5, 5, new GeneradorMinasFijo("C1", "C2", "C3", "C4", "C5"));
            assertEquals(ResultadoDescubrimiento.VACIA, tablero.descubrir(c("A3")));
            assertEquals(10, tablero.getSegurasDescubiertas()); // filas A y B
            assertEquals('2', tablero.casillaEn(1, 0).simboloDescubierto()); // B1 es número del borde
            assertTrue(tablero.casillaEn(2, 0).estaCubierta());  // la mina C1 no se abre
            assertTrue(tablero.casillaEn(3, 0).estaCubierta());  // D1 queda del otro lado
        }

        @Test
        void laExpansionRespetaLasBanderas() throws BuscaminasException {
            Tablero tablero = new Tablero(5, 5, 1, new GeneradorMinasFijo("E5"));
            tablero.alternarMarca(c("C3"));
            tablero.descubrir(c("A1"));
            assertTrue(tablero.casillaEn(2, 2).estaMarcada());
            assertFalse(tablero.todasLasSegurasDescubiertas());
            assertEquals(1, tablero.contarMarcas());
        }

        @Test
        void descubrirUnaMinaDevuelveMina() throws BuscaminasException {
            Tablero tablero = tableroConocido();
            assertEquals(ResultadoDescubrimiento.MINA, tablero.descubrir(c("C5")));
            assertEquals(0, tablero.getSegurasDescubiertas());
        }

        @Test
        @DisplayName("Fuera del tablero: ArrayIndexOutOfBounds se convierte en excepción propia")
        void coordenadaFueraDelTablero() {
            Tablero tablero = tableroConocido();
            CoordenadaFueraDeRangoException error = assertThrows(CoordenadaFueraDeRangoException.class,
                    () -> tablero.descubrir(c("K1")));
            assertInstanceOf(ArrayIndexOutOfBoundsException.class, error.getCause());
            assertThrows(CoordenadaFueraDeRangoException.class, () -> tablero.alternarMarca(c("A11")));
            assertThrows(CoordenadaFueraDeRangoException.class, () -> tablero.descubrir(new Coordenada(-1, 0)));
        }

        @Test
        void erroresDeCasillaSePropaganDesdeElTablero() throws BuscaminasException {
            Tablero tablero = tableroConocido();
            tablero.descubrir(c("B2"));
            assertThrows(CasillaYaDescubiertaException.class, () -> tablero.descubrir(c("B2")));
            tablero.alternarMarca(c("A1"));
            assertThrows(CasillaMarcadaException.class, () -> tablero.descubrir(c("A1")));
        }

        @Test
        void alPerderSeRevelanTodasLasMinas() {
            Tablero tablero = tableroConocido();
            tablero.revelarMinas();
            assertTrue(tablero.casillaEn(4, 4).estaDescubierta());
            assertTrue(tablero.casillaEn(9, 9).estaDescubierta());
            assertTrue(tablero.casillaEn(5, 5).estaCubierta());
        }
    }
}
