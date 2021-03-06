package Categoria;

import Compra.Compra;
import Compra.Item;
import Entidad.EntidadBase;
import Entidad.EntidadJuridica;
import Exceptions.BloqueoDeAgregarEntidadesBaseException;
import Exceptions.EntidadJuridicaBloqueadaException;
import Exceptions.MontoMaximoExcedidoException;
import Factory.CategoriasFactory;
import Factory.DireccionesFactory;
import Factory.EntidadesFactory;
import Factory.ItemsFactory;
import MedioDePago.MedioDePago;
import MedioDePago.PagoEnEfectivo;
import Repositorios.RepositorioDeMonedas.RepositorioDeMonedasMock;
import Moneda.CodigoMoneda;
import Proveedor.Proveedor;
import Repositorios.RepositorioDeMonedas.RepositorioDeMonedas;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class TestCategoria {
    private RepositorioDeMonedas repositorioDeMonedas;
    private EntidadJuridica entidadJuridica;
    private EntidadBase entidadBase;
    private Categoria categoria;
    private Item item;
    private Compra compra;
    private Proveedor proveedor;
    private MedioDePago medioDePago;

    @Before
    public void init() {
        repositorioDeMonedas = new RepositorioDeMonedasMock();

        DireccionesFactory.direccionStub9DeJulio();
        entidadJuridica = EntidadesFactory.empresaMedianaTramo2();
        entidadBase = EntidadesFactory.baseRandom();
        categoria = CategoriasFactory.ong();

        proveedor = EntidadesFactory.personaHumana();
        medioDePago = new PagoEnEfectivo();
        item = ItemsFactory.itemNValuadoEn(4,40);
        compra = new Compra(repositorioDeMonedas, entidadJuridica, LocalDate.now(), medioDePago, CodigoMoneda.ARS, 1);
        compra.agregarItem(item);
    }

    @Test
    public void categoriaMePermiteAgregarOQuitarComportamiento(){
        Validador validador1 = new ValidadorMontoMaximo(BigDecimal.valueOf(50));
        Validador validador2 = new ValidadorNoMasEntidadesbase();

        categoria.agregarValidador(validador1);
        categoria.agregarValidador(validador2);
        categoria.eliminarValidador(validador1);

        assertEquals(1, categoria.getValidadores().size());
    }

    @Test(expected = BloqueoDeAgregarEntidadesBaseException.class)
    public void categoriaMePermiteAgregarCompraPeroNoAgregarEntidadBase() {
        categoria.agregarValidador(new ValidadorMontoMaximo(BigDecimal.valueOf(170)));
        categoria.agregarValidador(new ValidadorNoMasEntidadesbase());
        entidadJuridica.agregarCategoria(categoria);

        entidadJuridica.agregarCompra(compra);
        entidadJuridica.agregarEntidadBase(entidadBase);
    }

    @Test(expected = MontoMaximoExcedidoException.class)
    public void categoriaNoMePermiteAgregarCompraYaQueExcediElMontoMaximo(){
        categoria.agregarValidador(new ValidadorMontoMaximo(BigDecimal.valueOf(150)));
        entidadJuridica.agregarCategoria(categoria);
        entidadJuridica.agregarCompra(compra);
    }

    @Test(expected = EntidadJuridicaBloqueadaException.class)
    public void CategoriaNoLePermiteALaBaseSerAgregadaAUnaJuridica(){
        categoria.agregarValidador(new ValidadorEntidadJuridicaBloqueada(entidadJuridica));
        entidadBase.agregarCategoria(categoria);
        entidadJuridica.agregarEntidadBase(entidadBase);
    }
}
