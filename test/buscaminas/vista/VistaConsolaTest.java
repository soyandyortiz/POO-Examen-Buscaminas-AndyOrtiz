package buscaminas.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import buscaminas.modelo.Coordenada;
import buscaminas.modelo.GeneradorMinasFijo;
import buscaminas.modelo.Jugador;
import buscaminas.modelo.Partida;
import buscaminas.modelo.Tablero;
import buscaminas.modelo.excepciones.BuscaminasException;

@DisplayName("Vista de consola: dibujo del tablero y lectura segura de datos")
class VistaConsolaTest {

    private final ByteArrayOutputStream salida = new ByteArrayOutputStream();

    private VistaConsola vistaCon(String... lineas) {
        String texto = String.join("\n", lineas) + (lineas.length > 0 ? "\n" : "");
        return new VistaConsola(new Scanner(new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), new PrintStream(salida, true, StandardCharsets.UTF_8));
    }

    private String mostrado() {
        return salida.toString(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("InputMismatchException: si se escriben letras en el menú se vuelve a pedir")
    void elMenuToleraLetras() {
        VistaConsola vista = vistaCon("abc", "7", "2", "1");
        assertEquals(1, vista.mostrarMenuPrincipal(false));
        assertTrue(mostrado().contains("Debe ingresar un número entero"));
        assertTrue(mostrado().contains("La opción 7 no está en el menú"));
        assertTrue(mostrado().contains("La opción 2 no está en el menú"));
    }

    @Test
    void elMenuOfreceContinuarSoloSiHayPartidaGuardada() {
        assertEquals(2, vistaCon("2").mostrarMenuPrincipal(true));
        assertTrue(mostrado().contains("Continuar partida guardada"));
    }

    @Test
    @DisplayName("Dibuja el tablero con letras A-J, números 1-10 y los símbolos del juego")
    void dibujaElTablero() throws BuscaminasException {
        Partida partida = new Partida(new Jugador("Andy"),
                new Tablero(new GeneradorMinasFijo("J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10")));
        partida.alternarMarca(new Coordenada(8, 0)); // bandera en I1
        partida.descubrir(new Coordenada(0, 0));     // A1 vacía
        vistaCon().mostrarPartida(partida);
        String texto = mostrado();
        assertTrue(texto.contains("  1  2  3  4  5  6  7  8  9 10"));
        assertTrue(texto.contains(" A   V  V  V  V  V  V  V  V  V  V"));
        assertTrue(texto.contains(" I   B  3  3  3  3  3  3  3  3  2"));
        assertTrue(texto.contains(" J   -  -  -  -  -  -  -  -  -  -"));
        assertTrue(texto.contains("Jugador: Andy | Minas por marcar: 9"));
    }

    @Test
    void alPerderSeVenLasMinasConX() throws BuscaminasException {
        Partida partida = new Partida(new Jugador("Andy"), new Tablero(3, 3, 1, new GeneradorMinasFijo("C3")));
        partida.descubrir(new Coordenada(2, 2));
        vistaCon().mostrarPartida(partida);
        assertTrue(mostrado().contains(" C   -  -  X"));
    }

    @Test
    void leeNombreYJugadaIgnorandoLineasVacias() {
        VistaConsola vista = vistaCon("", "  Andy ", "", "a5");
        assertEquals("Andy", vista.pedirNombreJugador());
        assertEquals("a5", vista.pedirJugada());
    }

    @Test
    void confirmaConSoSi() {
        assertTrue(vistaCon("s").confirmar("¿Guardar?"));
        assertTrue(vistaCon("Sí").confirmar("¿Guardar?"));
        assertFalse(vistaCon("n").confirmar("¿Guardar?"));
    }

    @Test
    void muestraInstruccionesMensajesYErrores() {
        VistaConsola vista = vistaCon();
        vista.mostrarInstrucciones();
        vista.mostrarMensaje("hola");
        vista.mostrarError("falló");
        assertTrue(mostrado().contains("INSTRUCCIONES"));
        assertTrue(mostrado().contains(">> hola"));
        assertTrue(mostrado().contains("[ERROR] falló"));
    }

    @Test
    void avisaSiSeTerminaLaEntrada() {
        assertThrows(IllegalStateException.class, () -> vistaCon().pedirNombreJugador());
        assertThrows(IllegalStateException.class, () -> vistaCon().mostrarMenuPrincipal(false));
    }
}
