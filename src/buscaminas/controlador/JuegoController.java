package buscaminas.controlador;

import buscaminas.modelo.EstadoPartida;
import buscaminas.modelo.GeneradorMinas;
import buscaminas.modelo.Jugador;
import buscaminas.modelo.Partida;
import buscaminas.modelo.ResultadoDescubrimiento;
import buscaminas.modelo.Tablero;
import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.persistencia.PersistenciaException;
import buscaminas.persistencia.RepositorioPartida;
import buscaminas.vista.Vista;

/**
 * Controlador del patrón MVC: recibe lo que el jugador elige en la {@link Vista},
 * ejecuta la acción sobre el modelo ({@link Partida}) o sobre el {@link RepositorioPartida}
 * y le pide a la vista que muestre el resultado.
 * <p>
 * Recibe sus tres dependencias por el constructor, todas como interfaces, para poder
 * reemplazarlas en las pruebas unitarias.
 */
public class JuegoController {

    private final Vista vista;
    private final RepositorioPartida repositorio;
    private final GeneradorMinas generadorMinas;
    private Partida partida;

    /**
     * @param vista          interfaz con el usuario
     * @param repositorio    dónde se guardan las partidas
     * @param generadorMinas cómo se colocan las minas en cada partida nueva
     */
    public JuegoController(Vista vista, RepositorioPartida repositorio, GeneradorMinas generadorMinas) {
        this.vista = vista;
        this.repositorio = repositorio;
        this.generadorMinas = generadorMinas;
    }

    /** Muestra el menú principal hasta que el jugador elija salir. */
    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenuPrincipal(repositorio.existePartidaGuardada());
            switch (opcion) {
                case Vista.OPCION_NUEVA -> iniciarNuevaPartida();
                case Vista.OPCION_CONTINUAR -> continuarPartidaGuardada();
                case Vista.OPCION_INSTRUCCIONES -> vista.mostrarInstrucciones();
                default -> vista.mostrarMensaje("¡Gracias por jugar!");
            }
        } while (opcion != Vista.OPCION_SALIR);
    }

    // ================================================================ inicio de partida

    private void iniciarNuevaPartida() {
        Jugador jugador = pedirJugador();
        partida = new Partida(jugador, new Tablero(generadorMinas));
        vista.mostrarMensaje("Partida nueva para " + jugador.getNombre() + ". ¡Suerte!");
        jugar();
    }

    /** Pide el nombre hasta que sea válido (el modelo decide qué es válido). */
    private Jugador pedirJugador() {
        while (true) {
            try {
                return new Jugador(vista.pedirNombreJugador());
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    private void continuarPartidaGuardada() {
        try {
            partida = repositorio.cargar();
            vista.mostrarMensaje("Partida de " + partida.getJugador().getNombre() + " cargada. Continúa donde la dejaste.");
            jugar();
        } catch (PersistenciaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    // ================================================================ ciclo de juego

    /** Repite: mostrar tablero, leer jugada, ejecutarla; hasta que la partida termine o el jugador salga. */
    private void jugar() {
        boolean seguirJugando = true;
        while (seguirJugando && !partida.estaTerminada()) {
            vista.mostrarPartida(partida);
            seguirJugando = procesarJugada(vista.pedirJugada());
        }
        if (partida.estaTerminada()) {
            vista.mostrarPartida(partida);
            anunciarResultado();
            borrarPartidaGuardada();
        }
    }

    /**
     * Interpreta y ejecuta una jugada. Los errores de la jugada (coordenada inválida,
     * casilla ya descubierta, casilla marcada, etc.) se muestran y la partida continúa.
     *
     * @return {@code false} si el jugador pidió salir al menú
     */
    boolean procesarJugada(String textoJugada) {
        try {
            Comando comando = Comando.desdeTexto(textoJugada);
            return ejecutar(comando);
        } catch (BuscaminasException e) {
            vista.mostrarError(e.getMessage());
            return true;
        }
    }

    private boolean ejecutar(Comando comando) throws BuscaminasException {
        switch (comando.tipo()) {
            case DESCUBRIR -> descubrir(comando);
            case MARCAR -> marcar(comando);
            case GUARDAR -> guardar();
            case AYUDA -> vista.mostrarInstrucciones();
            case SALIR -> {
                return !salirAlMenu();
            }
        }
        return true;
    }

    private void descubrir(Comando comando) throws BuscaminasException {
        ResultadoDescubrimiento resultado = partida.descubrir(comando.coordenada());
        if (resultado == ResultadoDescubrimiento.VACIA) {
            vista.mostrarMensaje("Casilla vacía: se descubrieron las casillas vecinas.");
        }
    }

    private void marcar(Comando comando) throws BuscaminasException {
        boolean marcada = partida.alternarMarca(comando.coordenada());
        vista.mostrarMensaje(marcada
                ? "Bandera colocada en " + comando.coordenada().aTexto()
                : "Bandera retirada de " + comando.coordenada().aTexto());
    }

    // ================================================================ persistencia

    private void guardar() {
        try {
            repositorio.guardar(partida);
            vista.mostrarMensaje("Partida guardada. Puede continuarla desde el menú principal.");
        } catch (PersistenciaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /** @return {@code true} si efectivamente se sale al menú */
    private boolean salirAlMenu() {
        if (vista.confirmar("¿Desea guardar la partida antes de salir?")) {
            guardar();
        }
        return true;
    }

    private void borrarPartidaGuardada() {
        try {
            repositorio.eliminar();
        } catch (PersistenciaException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void anunciarResultado() {
        if (partida.getEstado() == EstadoPartida.GANADA) {
            vista.mostrarMensaje("¡FELICIDADES " + partida.getJugador().getNombre().toUpperCase()
                    + "! Descubriste todas las casillas seguras en " + partida.getJugador().getJugadas() + " jugadas.");
        } else {
            vista.mostrarMensaje("¡BOOM! Pisaste una mina. Las X muestran dónde estaban. Fin de la partida.");
        }
    }

    /** @return partida actual (para las pruebas) */
    Partida getPartida() {
        return partida;
    }
}
