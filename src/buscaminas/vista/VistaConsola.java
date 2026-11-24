package buscaminas.vista;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import buscaminas.modelo.CasillaSoloLectura;
import buscaminas.modelo.Partida;
import buscaminas.modelo.Tablero;

/**
 * Interfaz de usuario en consola. Solo dibuja y lee datos; las reglas del juego
 * están en el modelo y las decisiones en el controlador.
 */
public class VistaConsola implements Vista {

    /** Casilla todavía cubierta. */
    public static final char SIMBOLO_CUBIERTA = '-';
    /** Casilla marcada por el jugador como posible mina (bandera). */
    public static final char SIMBOLO_MARCA = 'B';

    private static final String SEPARADOR = "=".repeat(52);
    private static final String FORMATO_CELDA = "%3s";

    private final Scanner entrada;
    private final PrintStream salida;

    /**
     * @param entrada de dónde se leen los datos (normalmente el teclado)
     * @param salida  dónde se escribe (normalmente la consola)
     */
    public VistaConsola(Scanner entrada, PrintStream salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    // ================================================================ menú

    @Override
    public int mostrarMenuPrincipal(boolean hayPartidaGuardada) {
        salida.println();
        salida.println(SEPARADOR);
        salida.println("               B U S C A M I N A S");
        salida.println(SEPARADOR);
        salida.println(" " + OPCION_NUEVA + ". Nueva partida");
        if (hayPartidaGuardada) {
            salida.println(" " + OPCION_CONTINUAR + ". Continuar partida guardada");
        }
        salida.println(" " + OPCION_INSTRUCCIONES + ". Instrucciones");
        salida.println(" " + OPCION_SALIR + ". Salir");
        salida.println(SEPARADOR);
        while (true) {
            int opcion = leerEntero("Seleccione una opción: ");
            if (esOpcionValida(opcion, hayPartidaGuardada)) {
                return opcion;
            }
            mostrarError("La opción " + opcion + " no está en el menú");
        }
    }

    private static boolean esOpcionValida(int opcion, boolean hayPartidaGuardada) {
        return opcion == OPCION_NUEVA || opcion == OPCION_INSTRUCCIONES || opcion == OPCION_SALIR
                || (opcion == OPCION_CONTINUAR && hayPartidaGuardada);
    }

    /**
     * Lee un número entero con {@link Scanner#nextInt()}. Si el jugador escribe letras,
     * Scanner lanza {@link InputMismatchException}: se captura, se descarta la línea
     * y se vuelve a pedir el dato.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            salida.print(mensaje);
            try {
                int valor = entrada.nextInt();
                entrada.nextLine(); // descarta el salto de línea que deja nextInt
                return valor;
            } catch (InputMismatchException e) {
                entrada.nextLine(); // descarta lo que no era un número
                mostrarError("Debe ingresar un número entero");
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Se terminó la entrada de datos", e);
            }
        }
    }

    @Override
    public String pedirNombreJugador() {
        return leerLineaNoVacia("Nombre del jugador: ");
    }

    @Override
    public String pedirJugada() {
        salida.println("Jugada -> [A5] descubrir | [M A5] marcar/desmarcar | [G] guardar | [S] salir | [?] ayuda");
        return leerLineaNoVacia("> ");
    }

    @Override
    public boolean confirmar(String pregunta) {
        String respuesta = leerLineaNoVacia(pregunta + " (s/n): ");
        return respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí");
    }

    private String leerLineaNoVacia(String mensaje) {
        while (true) {
            salida.print(mensaje);
            if (!entrada.hasNextLine()) {
                throw new IllegalStateException("Se terminó la entrada de datos");
            }
            String linea = entrada.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
        }
    }

    // ================================================================ tablero

    @Override
    public void mostrarPartida(Partida partida) {
        Tablero tablero = partida.getTablero();
        salida.println();
        salida.printf("Jugador: %s | Minas por marcar: %d | Jugadas: %d | Seguras descubiertas: %d/%d%n",
                partida.getJugador().getNombre(), partida.getMinasPendientes(), partida.getJugador().getJugadas(),
                tablero.getSegurasDescubiertas(), tablero.getTotalSeguras());
        salida.println(encabezadoDeColumnas(tablero.getColumnas()));
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            salida.println(filaComoTexto(tablero, fila));
        }
        salida.println("Leyenda: - cubierta | B marcada | V vacía | 1-8 minas vecinas | X mina");
    }

    private static String encabezadoDeColumnas(int columnas) {
        StringBuilder linea = new StringBuilder("   ");
        for (int columna = 1; columna <= columnas; columna++) {
            linea.append(String.format(FORMATO_CELDA, columna));
        }
        return linea.toString();
    }

    private static String filaComoTexto(Tablero tablero, int fila) {
        StringBuilder linea = new StringBuilder(" " + (char) ('A' + fila) + " ");
        for (int columna = 0; columna < tablero.getColumnas(); columna++) {
            linea.append(String.format(FORMATO_CELDA, simbolo(tablero.casillaEn(fila, columna))));
        }
        return linea.toString();
    }

    /** Elige el símbolo según el estado de la casilla. */
    static char simbolo(CasillaSoloLectura casilla) {
        if (casilla.estaMarcada()) {
            return SIMBOLO_MARCA;
        }
        return casilla.estaDescubierta() ? casilla.simboloDescubierto() : SIMBOLO_CUBIERTA;
    }

    // ================================================================ mensajes

    @Override
    public void mostrarInstrucciones() {
        salida.println();
        salida.println("---------------------- INSTRUCCIONES ----------------------");
        salida.println("El tablero tiene 10x10 casillas y 10 minas escondidas.");
        salida.println("Objetivo: descubrir todas las casillas que NO tienen mina.");
        salida.println("  A5    descubre la casilla de la fila A, columna 5.");
        salida.println("  M A5  pone o quita una bandera (B) donde crees que hay una mina.");
        salida.println("        Una casilla con bandera no se puede descubrir por accidente.");
        salida.println("  G     guarda la partida para continuarla después.");
        salida.println("  S     vuelve al menú principal.");
        salida.println("Los números indican cuántas minas hay en las 8 casillas vecinas.");
        salida.println("Si descubres una casilla vacía (V), se abren solas sus vecinas.");
        salida.println("-----------------------------------------------------------");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        salida.println(">> " + mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        salida.println("[ERROR] " + mensaje);
    }
}
