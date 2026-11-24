package buscaminas.controlador;

import buscaminas.modelo.Partida;
import buscaminas.persistencia.PersistenciaException;
import buscaminas.persistencia.RepositorioPartida;

/** Repositorio falso: guarda la partida en memoria y puede simular fallos. */
class RepositorioEnMemoria implements RepositorioPartida {

    Partida guardada;
    boolean fallar;
    int vecesGuardada;

    @Override
    public boolean existePartidaGuardada() {
        return guardada != null;
    }

    @Override
    public void guardar(Partida partida) throws PersistenciaException {
        verificarFallo();
        guardada = partida;
        vecesGuardada++;
    }

    @Override
    public Partida cargar() throws PersistenciaException {
        verificarFallo();
        if (guardada == null) {
            throw new PersistenciaException("No hay ninguna partida guardada");
        }
        return guardada;
    }

    @Override
    public void eliminar() throws PersistenciaException {
        verificarFallo();
        guardada = null;
    }

    private void verificarFallo() throws PersistenciaException {
        if (fallar) {
            throw new PersistenciaException("Error de disco simulado");
        }
    }
}
