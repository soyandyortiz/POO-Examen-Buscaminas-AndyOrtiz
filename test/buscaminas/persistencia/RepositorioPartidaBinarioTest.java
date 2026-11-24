package buscaminas.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import buscaminas.modelo.Coordenada;
import buscaminas.modelo.EstadoPartida;
import buscaminas.modelo.GeneradorMinasFijo;
import buscaminas.modelo.Jugador;
import buscaminas.modelo.Partida;
import buscaminas.modelo.Tablero;
import buscaminas.modelo.excepciones.BuscaminasException;

@DisplayName("Persistencia: guardar y continuar una partida con serialización")
class RepositorioPartidaBinarioTest {

    @TempDir
    Path carpeta;

    private RepositorioPartidaBinario repositorio;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioPartidaBinario(carpeta.resolve("partida.dat"));
    }

    private static Partida partidaAvanzada() throws BuscaminasException {
        Partida partida = new Partida(new Jugador("Andy"),
                new Tablero(new GeneradorMinasFijo("A1", "A2", "B1", "C5", "E5", "E6", "J10", "J9", "I10", "G2")));
        partida.descubrir(new Coordenada(1, 1));  // B2 (número)
        partida.descubrir(new Coordenada(5, 9));  // F10 (vacía, abre una zona)
        partida.alternarMarca(new Coordenada(0, 0)); // bandera en A1
        return partida;
    }

    @Test
    void alInicioNoHayPartidaGuardada() {
        assertFalse(repositorio.existePartidaGuardada());
        assertEquals("partida.dat", repositorio.getArchivo().getFileName().toString());
    }

    @Test
    @DisplayName("La partida cargada continúa exactamente donde quedó")
    void guardarYCargarConservaElEstado() throws Exception {
        Partida original = partidaAvanzada();
        repositorio.guardar(original);
        assertTrue(repositorio.existePartidaGuardada());

        Partida cargada = repositorio.cargar();
        assertEquals("Andy", cargada.getJugador().getNombre());
        assertEquals(2, cargada.getJugador().getJugadas());
        assertEquals(EstadoPartida.EN_CURSO, cargada.getEstado());
        assertEquals(original.getTablero().getSegurasDescubiertas(), cargada.getTablero().getSegurasDescubiertas());
        assertTrue(cargada.getTablero().casillaEn(0, 0).estaMarcada());
        assertTrue(cargada.getTablero().casillaEn(1, 1).estaDescubierta());
        assertTrue(cargada.getTablero().casillaEn(9, 9).esMina());
    }

    @Test
    void laPartidaCargadaSePuedeSeguirJugando() throws Exception {
        repositorio.guardar(partidaAvanzada());
        Partida cargada = repositorio.cargar();
        cargada.descubrir(new Coordenada(4, 4)); // E5 es mina
        assertEquals(EstadoPartida.PERDIDA, cargada.getEstado());
    }

    @Test
    void guardarDeNuevoReemplazaLaPartidaAnterior() throws Exception {
        repositorio.guardar(partidaAvanzada());
        Partida otra = new Partida(new Jugador("María"), new Tablero(3, 3, 1, new GeneradorMinasFijo("C3")));
        repositorio.guardar(otra);
        assertEquals("María", repositorio.cargar().getJugador().getNombre());
        assertFalse(Files.exists(carpeta.resolve("partida.dat.tmp")));
    }

    @Test
    void cargarSinPartidaGuardadaLanzaExcepcionClara() {
        PersistenciaException error = assertThrows(PersistenciaException.class, () -> repositorio.cargar());
        assertTrue(error.getMessage().contains("No hay ninguna partida guardada"));
    }

    @Test
    void unArchivoDanadoNoRompeElJuego() throws IOException {
        Files.writeString(repositorio.getArchivo(), "esto no es una partida");
        PersistenciaException error = assertThrows(PersistenciaException.class, () -> repositorio.cargar());
        assertTrue(error.getMessage().contains("dañado"));
    }

    @Test
    void unArchivoConOtroObjetoSeRechaza() throws IOException {
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(repositorio.getArchivo()))) {
            salida.writeObject("un texto serializado");
        }
        PersistenciaException error = assertThrows(PersistenciaException.class, () -> repositorio.cargar());
        assertTrue(error.getMessage().contains("no contiene una partida"));
    }

    @Test
    void unArchivoIncompletoSeReportaComoError() throws Exception {
        repositorio.guardar(partidaAvanzada());
        byte[] completo = Files.readAllBytes(repositorio.getArchivo());
        Files.write(repositorio.getArchivo(), java.util.Arrays.copyOf(completo, completo.length / 2));
        assertThrows(PersistenciaException.class, () -> repositorio.cargar());
    }

    @Test
    void eliminarBorraLaPartida() throws Exception {
        repositorio.guardar(partidaAvanzada());
        repositorio.eliminar();
        assertFalse(repositorio.existePartidaGuardada());
        repositorio.eliminar(); // borrar cuando no existe no es un error
    }

    @Test
    void noSePuedeGuardarEnUnaCarpetaInexistente() {
        RepositorioPartidaBinario invalido = new RepositorioPartidaBinario(carpeta.resolve("no/existe/partida.dat"));
        assertThrows(PersistenciaException.class, () -> invalido.guardar(partidaAvanzada()));
    }

    @Test
    void noSePuedeBorrarUnaCarpetaConContenido() throws IOException {
        Path carpetaConArchivos = Files.createDirectory(carpeta.resolve("ocupada"));
        Files.writeString(carpetaConArchivos.resolve("x.txt"), "x");
        assertThrows(PersistenciaException.class, () -> new RepositorioPartidaBinario(carpetaConArchivos).eliminar());
    }

    @Test
    void elConstructorPorDefectoUsaElArchivoDelJuego() {
        assertEquals(RepositorioPartidaBinario.ARCHIVO_POR_DEFECTO,
                new RepositorioPartidaBinario().getArchivo().toString());
    }
}
