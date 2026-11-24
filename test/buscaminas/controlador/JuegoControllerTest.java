package buscaminas.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import buscaminas.modelo.EstadoPartida;
import buscaminas.modelo.GeneradorMinasFijo;

@DisplayName("Controlador: flujos completos del juego con vista y repositorio simulados")
class JuegoControllerTest {

    private static final int NUEVA = 1;
    private static final int CONTINUAR = 2;
    private static final int INSTRUCCIONES = 3;
    private static final int SALIR = 0;

    /** Minas en la fila J completa: descubrir A1 abre filas A-H y deja la fila I por descubrir. */
    private static final GeneradorMinasFijo MINAS_EN_FILA_J =
            new GeneradorMinasFijo("J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10");

    private VistaSimulada vista;
    private RepositorioEnMemoria repositorio;
    private JuegoController controlador;

    @BeforeEach
    void preparar() {
        vista = new VistaSimulada();
        repositorio = new RepositorioEnMemoria();
        controlador = new JuegoController(vista, repositorio, MINAS_EN_FILA_J);
    }

    @Test
    @DisplayName("Victoria: descubrir A1 abre todas las casillas seguras")
    void partidaGanada() {
        vista.responder(NUEVA, "Andy", "A1", SALIR);
        controlador.iniciar();
        assertEquals(EstadoPartida.GANADA, controlador.getPartida().getEstado());
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.contains("FELICIDADES ANDY")));
    }

    @Test
    @DisplayName("Derrota: descubrir una mina termina la partida")
    void partidaPerdida() {
        vista.responder(NUEVA, "Andy", "J5", SALIR);
        controlador.iniciar();
        assertEquals(EstadoPartida.PERDIDA, controlador.getPartida().getEstado());
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.contains("BOOM")));
    }

    @Test
    void lasJugadasInvalidasMuestranErrorYLaPartidaSigue() {
        vista.responder(NUEVA, "Andy", "K1", "hola", "", "M I1", "I1", "M I1", "I1", "I1", "A1", SALIR);
        controlador.iniciar();
        assertEquals(5, vista.errores.size(), vista.errores.toString());
        assertTrue(vista.errores.get(0).contains("fuera del tablero"));
        assertTrue(vista.errores.get(3).contains("marcada"));
        assertTrue(vista.errores.get(4).contains("ya está descubierta"));
    }

    @Test
    void elNombreSePideHastaQueSeaValido() {
        vista.responder(NUEVA, "   ", "Andy", "A1", SALIR);
        controlador.iniciar();
        assertEquals(1, vista.errores.size());
        assertEquals("Andy", controlador.getPartida().getJugador().getNombre());
    }

    @Test
    @DisplayName("Guardar, salir y continuar la misma partida")
    void guardarYContinuar() {
        vista.responder(NUEVA, "Andy", "M I1", "G", "S", "n", CONTINUAR, "M I1", "A1", SALIR);
        controlador.iniciar();
        assertEquals(1, repositorio.vecesGuardada);
        assertEquals(EstadoPartida.GANADA, controlador.getPartida().getEstado());
        assertNull(repositorio.guardada, "al terminar la partida se borra la partida guardada");
    }

    @Test
    void alSalirPuedeGuardar() {
        vista.responder(NUEVA, "Andy", "S", "s", SALIR);
        controlador.iniciar();
        assertNotNull(repositorio.guardada);
        assertFalse(repositorio.guardada.estaTerminada());
    }

    @Test
    void continuarSinPartidaGuardadaMuestraError() {
        vista.responder(CONTINUAR, SALIR);
        controlador.iniciar();
        assertTrue(vista.errores.get(0).contains("No hay ninguna partida guardada"));
    }

    @Test
    void losErroresDeDiscoSeMuestranSinCerrarElJuego() {
        repositorio.fallar = true;
        vista.responder(NUEVA, "Andy", "G", "J1", SALIR);
        controlador.iniciar();
        assertEquals(2, vista.errores.size()); // al guardar y al intentar borrar al final
        assertTrue(vista.errores.get(0).contains("Error de disco simulado"));
    }

    @Test
    void muestraInstruccionesDesdeElMenuYDuranteLaPartida() {
        vista.responder(INSTRUCCIONES, NUEVA, "Andy", "?", "A1", SALIR);
        controlador.iniciar();
        assertEquals(2, vista.instruccionesMostradas);
        assertTrue(vista.tablerosMostrados >= 2);
    }

    @Test
    void informaCuandoSeAbreUnaZonaVaciaYCuandoSeMarca() {
        vista.responder(NUEVA, "Andy", "M I1", "M I1", "A1", SALIR);
        controlador.iniciar();
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.startsWith("Bandera colocada en I1")));
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.startsWith("Bandera retirada de I1")));
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.contains("Casilla vacía")));
    }
}
