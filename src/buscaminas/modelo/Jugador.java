package buscaminas.modelo;

import java.io.Serializable;

/** Persona que juega la partida. Lleva la cuenta de las jugadas que ha hecho. */
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Límite para que el nombre se vea bien en la cabecera del tablero. */
    public static final int LONGITUD_MAXIMA_NOMBRE = 30;

    private final String nombre;
    private int jugadas;

    /**
     * @param nombre nombre del jugador (no vacío, máximo 30 caracteres)
     */
    public Jugador(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }
        if (nombre.trim().length() > LONGITUD_MAXIMA_NOMBRE) {
            throw new IllegalArgumentException("El nombre no puede tener más de " + LONGITUD_MAXIMA_NOMBRE + " caracteres");
        }
        this.nombre = nombre.trim();
    }

    /** @return nombre del jugador */
    public String getNombre() {
        return nombre;
    }

    /** @return cantidad de casillas que el jugador ha descubierto */
    public int getJugadas() {
        return jugadas;
    }

    /** Suma una jugada al contador; solo lo usa {@link Partida}. */
    void registrarJugada() {
        jugadas++;
    }
}
