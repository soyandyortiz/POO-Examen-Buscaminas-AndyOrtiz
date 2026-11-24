package buscaminas.persistencia;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import buscaminas.modelo.Partida;

/**
 * Guarda la partida completa en un archivo binario usando serialización de objetos.
 * <p>
 * Se guarda el objeto {@link Partida}, que contiene al jugador y al tablero con todas
 * sus casillas, así que al cargarlo el juego continúa exactamente donde quedó.
 */
public class RepositorioPartidaBinario implements RepositorioPartida {

    /** Nombre del archivo que se crea en la carpeta desde donde se ejecuta el juego. */
    public static final String ARCHIVO_POR_DEFECTO = "partida_guardada.dat";

    private final Path archivo;

    /** Usa el archivo {@value #ARCHIVO_POR_DEFECTO}. */
    public RepositorioPartidaBinario() {
        this(Path.of(ARCHIVO_POR_DEFECTO));
    }

    /**
     * @param archivo ruta del archivo binario
     */
    public RepositorioPartidaBinario(Path archivo) {
        this.archivo = archivo;
    }

    /** @return ruta del archivo usado */
    public Path getArchivo() {
        return archivo;
    }

    @Override
    public boolean existePartidaGuardada() {
        return Files.isRegularFile(archivo);
    }

    /**
     * Escribe primero en un archivo temporal y luego lo mueve sobre el definitivo:
     * si algo falla a mitad de la escritura, la partida guardada anterior no se daña.
     */
    @Override
    public void guardar(Partida partida) throws PersistenciaException {
        Path temporal = archivo.resolveSibling(archivo.getFileName() + ".tmp");
        try {
            try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(temporal))) {
                salida.writeObject(partida);
            }
            Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar la partida: " + e.getMessage(), e);
        }
    }

    @Override
    public Partida cargar() throws PersistenciaException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo))) {
            Object leido = entrada.readObject();
            if (!(leido instanceof Partida partida)) {
                throw new PersistenciaException("El archivo " + archivo.getFileName() + " no contiene una partida");
            }
            return partida;
        } catch (NoSuchFileException e) {
            throw new PersistenciaException("No hay ninguna partida guardada", e);
        } catch (InvalidClassException e) {
            throw new PersistenciaException("La partida se guardó con otra versión del juego", e);
        } catch (StreamCorruptedException e) {
            throw new PersistenciaException("El archivo de la partida está dañado", e);
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("No se pudo leer la partida guardada: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar() throws PersistenciaException {
        try {
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo borrar la partida guardada: " + e.getMessage(), e);
        }
    }
}
