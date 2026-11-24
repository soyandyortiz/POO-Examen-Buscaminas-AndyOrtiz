package buscaminas.controlador;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import buscaminas.modelo.Partida;
import buscaminas.vista.Vista;

/** Vista falsa para las pruebas: responde con datos preparados y guarda lo que se le pide mostrar. */
class VistaSimulada implements Vista {

    private final Deque<String> respuestas = new ArrayDeque<>();
    final List<String> mensajes = new ArrayList<>();
    final List<String> errores = new ArrayList<>();
    int tablerosMostrados;
    int instruccionesMostradas;

    VistaSimulada responder(Object... valores) {
        for (Object valor : valores) {
            respuestas.add(String.valueOf(valor));
        }
        return this;
    }

    private String siguiente() {
        if (respuestas.isEmpty()) {
            throw new IllegalStateException("La prueba no preparó suficientes respuestas");
        }
        return respuestas.poll();
    }

    @Override
    public int mostrarMenuPrincipal(boolean hayPartidaGuardada) {
        return Integer.parseInt(siguiente());
    }

    @Override
    public String pedirNombreJugador() {
        return siguiente();
    }

    @Override
    public void mostrarPartida(Partida partida) {
        tablerosMostrados++;
    }

    @Override
    public String pedirJugada() {
        return siguiente();
    }

    @Override
    public void mostrarInstrucciones() {
        instruccionesMostradas++;
    }

    @Override
    public boolean confirmar(String pregunta) {
        return siguiente().equalsIgnoreCase("s");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajes.add(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        errores.add(mensaje);
    }
}
