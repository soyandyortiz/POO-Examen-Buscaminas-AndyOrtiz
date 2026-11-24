package buscaminas.vista;

import buscaminas.modelo.Partida;

/**
 * Operaciones de presentación que necesita el controlador.
 * Al ser una interfaz, el controlador se puede probar con una vista simulada.
 */
public interface Vista {

    /** Opción del menú principal para empezar una partida nueva. */
    int OPCION_NUEVA = 1;
    /** Opción del menú principal para continuar la partida guardada. */
    int OPCION_CONTINUAR = 2;
    /** Opción del menú principal para ver las instrucciones. */
    int OPCION_INSTRUCCIONES = 3;
    /** Opción del menú principal para salir. */
    int OPCION_SALIR = 0;

    /**
     * Muestra el menú principal y devuelve una opción válida.
     *
     * @param hayPartidaGuardada si se debe ofrecer "Continuar partida"
     * @return opción elegida
     */
    int mostrarMenuPrincipal(boolean hayPartidaGuardada);

    /** @return nombre escrito por el jugador */
    String pedirNombreJugador();

    /**
     * Dibuja el tablero y los datos de la partida.
     *
     * @param partida partida a mostrar
     */
    void mostrarPartida(Partida partida);

    /** @return la jugada escrita por el jugador (por ejemplo "A5", "M A5", "G" o "S") */
    String pedirJugada();

    /** Explica cómo se juega y qué significa cada símbolo. */
    void mostrarInstrucciones();

    /**
     * @param pregunta pregunta de sí o no
     * @return {@code true} si el jugador respondió que sí
     */
    boolean confirmar(String pregunta);

    /** @param mensaje información para el jugador */
    void mostrarMensaje(String mensaje);

    /** @param mensaje descripción de un error */
    void mostrarError(String mensaje);
}
