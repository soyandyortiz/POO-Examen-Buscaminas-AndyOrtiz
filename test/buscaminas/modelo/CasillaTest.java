package buscaminas.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import buscaminas.modelo.excepciones.BuscaminasException;
import buscaminas.modelo.excepciones.CasillaMarcadaException;
import buscaminas.modelo.excepciones.CasillaYaDescubiertaException;

@DisplayName("Casilla: herencia y polimorfismo de CasillaMina y CasillaSegura")
class CasillaTest {

    private static final Coordenada B2 = new Coordenada(1, 1);

    @Test
    void unaCasillaNuevaEstaCubierta() {
        Casilla casilla = new CasillaSegura(B2, 0);
        assertTrue(casilla.estaCubierta());
        assertFalse(casilla.estaMarcada());
        assertFalse(casilla.estaDescubierta());
        assertEquals(B2, casilla.getPosicion());
    }

    @Test
    @DisplayName("Polimorfismo: la mina devuelve MINA y se dibuja con X")
    void descubrirUnaMina() throws BuscaminasException {
        Casilla mina = new CasillaMina(B2);
        assertEquals(ResultadoDescubrimiento.MINA, mina.descubrir());
        assertTrue(mina.esMina());
        assertEquals('X', mina.simboloDescubierto());
    }

    @Test
    @DisplayName("Polimorfismo: la casilla sin minas vecinas es VACIA y se dibuja con V")
    void descubrirUnaCasillaVacia() throws BuscaminasException {
        CasillaSegura vacia = new CasillaSegura(B2, 0);
        assertEquals(ResultadoDescubrimiento.VACIA, vacia.descubrir());
        assertTrue(vacia.esVacia());
        assertFalse(vacia.esMina());
        assertEquals('V', vacia.simboloDescubierto());
    }

    @ParameterizedTest(name = "{0} minas vecinas se muestran como ''{0}''")
    @ValueSource(ints = {1, 3, 8})
    void descubrirUnaCasillaConNumero(int minasVecinas) throws BuscaminasException {
        CasillaSegura casilla = new CasillaSegura(B2, minasVecinas);
        assertEquals(ResultadoDescubrimiento.NUMERO, casilla.descubrir());
        assertEquals(Character.forDigit(minasVecinas, 10), casilla.simboloDescubierto());
        assertEquals(minasVecinas, casilla.getMinasAdyacentes());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 9})
    void rechazaCantidadesImposiblesDeMinasVecinas(int minasVecinas) {
        assertThrows(IllegalArgumentException.class, () -> new CasillaSegura(B2, minasVecinas));
    }

    @Test
    void noSePuedeDescubrirDosVeces() throws BuscaminasException {
        Casilla casilla = new CasillaSegura(B2, 2);
        casilla.descubrir();
        CasillaYaDescubiertaException error = assertThrows(CasillaYaDescubiertaException.class, casilla::descubrir);
        assertTrue(error.getMessage().contains("B2"));
    }

    @Test
    @DisplayName("Una casilla marcada no se descubre por accidente")
    void unaCasillaMarcadaNoSeDescubre() throws BuscaminasException {
        Casilla mina = new CasillaMina(B2);
        assertTrue(mina.alternarMarca());
        assertThrows(CasillaMarcadaException.class, mina::descubrir);
        assertTrue(mina.estaMarcada());
    }

    @Test
    void laMarcaSePoneYSeQuita() throws BuscaminasException {
        Casilla casilla = new CasillaSegura(B2, 1);
        assertTrue(casilla.alternarMarca());
        assertFalse(casilla.alternarMarca());
        assertTrue(casilla.estaCubierta());
    }

    @Test
    void noSePuedeMarcarUnaCasillaDescubierta() throws BuscaminasException {
        Casilla casilla = new CasillaSegura(B2, 1);
        casilla.descubrir();
        assertThrows(CasillaYaDescubiertaException.class, casilla::alternarMarca);
    }
}
