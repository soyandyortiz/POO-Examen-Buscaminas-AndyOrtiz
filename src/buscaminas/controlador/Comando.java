package buscaminas.controlador;

import java.util.Locale;

import buscaminas.modelo.Coordenada;
import buscaminas.modelo.excepciones.CoordenadaInvalidaException;

/**
 * Jugada escrita por el jugador ya interpretada.
 * <p>
 * Formatos aceptados (sin importar mayúsculas):
 * <ul>
 * <li>{@code A5} o {@code D A5}: descubrir la casilla A5</li>
 * <li>{@code M A5} o {@code MA5}: marcar o desmarcar la casilla A5</li>
 * <li>{@code G}: guardar la partida</li>
 * <li>{@code S}: salir al menú</li>
 * <li>{@code ?}: ver la ayuda</li>
 * </ul>
 *
 * @param tipo       acción que se quiere hacer
 * @param coordenada casilla afectada, o {@code null} si la acción no usa una casilla
 */
public record Comando(Tipo tipo, Coordenada coordenada) {

    /** Acciones posibles durante la partida. */
    public enum Tipo {
        /** Descubrir una casilla. */
        DESCUBRIR,
        /** Poner o quitar una bandera. */
        MARCAR,
        /** Guardar la partida en el archivo. */
        GUARDAR,
        /** Volver al menú principal. */
        SALIR,
        /** Mostrar las instrucciones. */
        AYUDA
    }

    /**
     * Interpreta el texto escrito por el jugador.
     *
     * @param texto jugada escrita
     * @return el comando correspondiente
     * @throws ComandoInvalidoException    si no es ninguna jugada conocida
     * @throws CoordenadaInvalidaException si la coordenada no tiene el formato correcto
     */
    public static Comando desdeTexto(String texto) throws ComandoInvalidoException, CoordenadaInvalidaException {
        String limpio = texto == null ? "" : texto.trim().toUpperCase(Locale.ROOT);
        switch (limpio) {
            case "G":
                return new Comando(Tipo.GUARDAR, null);
            case "S":
                return new Comando(Tipo.SALIR, null);
            case "?":
                return new Comando(Tipo.AYUDA, null);
            case "":
                throw new ComandoInvalidoException(texto);
            default:
                return comandoConCoordenada(limpio);
        }
    }

    private static Comando comandoConCoordenada(String texto) throws CoordenadaInvalidaException {
        if (texto.length() > 1 && texto.charAt(0) == 'M' && !Character.isDigit(texto.charAt(1))) {
            return new Comando(Tipo.MARCAR, Coordenada.desdeTexto(texto.substring(1)));
        }
        if (texto.length() > 1 && texto.charAt(0) == 'D' && !Character.isDigit(texto.charAt(1))) {
            return new Comando(Tipo.DESCUBRIR, Coordenada.desdeTexto(texto.substring(1)));
        }
        return new Comando(Tipo.DESCUBRIR, Coordenada.desdeTexto(texto));
    }
}
