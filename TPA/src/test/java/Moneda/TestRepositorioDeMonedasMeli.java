package Moneda;
import Moneda.CodigoMoneda;
import Compra.RepositorioDeMonedasMock;
import Repositorios.RepositorioDeLocaciones.LocacionNoEncontradaException;
import Repositorios.RepositorioDeMonedas.*;
import Usuario.Usuario;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRepositorioDeMonedasMeli {
	@Test()
    public void encuentroMonedaEnAPI() {
		RepositorioDeMonedasMeli repositorio = new RepositorioDeMonedasMeli();
		
		Moneda moneda = repositorio.getMoneda(CodigoMoneda.ARS);
		
		Assert.assertEquals(CodigoMoneda.ARS, moneda.getCodigo());
		Assert.assertEquals("Peso argentino", moneda.getDescripcion());
	}

	@Test(expected = MonedaNoEncontradaException.class)
	public void noEncuentroMonedaEnAPI() {
		RepositorioDeMonedasMeli repositorio = new RepositorioDeMonedasMeli();
		repositorio.getMoneda(CodigoMoneda.AUD);
	}
}