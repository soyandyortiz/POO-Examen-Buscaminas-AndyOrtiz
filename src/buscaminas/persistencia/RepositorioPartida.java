package buscaminas.persistencia;

import buscaminas.modelo.Partida;

/**
 * Dónde y cómo se guarda una partida para continuarla después.
 * El controlador depende de esta interfaz y no de la forma concreta de guardar.
 */
public interface RepositorioPartida {

    /** @return {@code true} si hay una partida guardada */
    boolean existePartidaGuardada();

    /**
     * Guarda la partida reemplazando la anterior.
     *
     * @param partida partida en curso
     * @throws PersistenciaException si no se pudo escribir
     */
    void guardar(Partida partida) throws PersistenciaException;

    /**
     * @return la partida guardada
     * @throws PersistenciaException si no existe o el archivo está dañado
     */
    Partida cargar() throws PersistenciaException;

    /**
     * Borra la partida guardada (se usa cuando la partida termina).
     *
     * @throws PersistenciaException si no se pudo borrar
     */
    void eliminar() throws PersistenciaException;
}
