package buscaminas.app;

import java.util.Scanner;

import buscaminas.controlador.JuegoController;
import buscaminas.modelo.GeneradorMinasAleatorio;
import buscaminas.persistencia.RepositorioPartidaBinario;
import buscaminas.vista.VistaConsola;

/**
 * Punto de entrada del juego. Crea las implementaciones concretas y se las
 * entrega al controlador (inyección de dependencias por constructor).
 */
public class Buscaminas {

    /**
     * @param args no se usan
     */
    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            JuegoController controlador = new JuegoController(
                    new VistaConsola(teclado, System.out),
                    new RepositorioPartidaBinario(),
                    new GeneradorMinasAleatorio());
            controlador.iniciar();
        }
    }
}
