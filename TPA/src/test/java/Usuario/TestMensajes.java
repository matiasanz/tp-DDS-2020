package Usuario;
import Factory.MensajeFactory;
import org.junit.Test;

import Compra.Compra;
import Factory.ComprasFactory;
import Factory.UsuariosFactory;
import TareasProgramadas.ValidacionEgresosAutomatica.ValidacionDeEgresos;

import static org.junit.Assert.*;

import org.junit.After;

import java.time.LocalDateTime;

public class TestMensajes {
	
	Usuario usuario1 = UsuariosFactory.usuarioStub();
	
	@After
	public void end(){
		usuario1.vaciarBandeja();
	}
	
	@Test()
	public void unUsuarioSeCreaSinNotificaciones(){
		assert(usuario1.getBandejaDeMensajes().isEmpty());
	}
	
	@Test()
    public void unUsuarioRecibeYEliminaMensajes(){
    	String notificacion = "Alerta alerta, estas por romper la dieta";
    	
    	usuario1.notificarEvento(new Mensaje(LocalDateTime.of(1,1,1,1,1), notificacion,0));
    	assertMensajeUnico(usuario1,notificacion);
    	
    	usuario1.vaciarBandeja();
    	assertTrue(usuario1.getBandejaDeMensajes().isEmpty());
    }
	
    @Test
    public void unaCompraSeValidaYNotificaATodosSusUsuarios(){
    	Usuario usuario1 = UsuariosFactory.usuarioStub();
    	Usuario usuario2 = UsuariosFactory.usuarioStub();    	
    	Compra compra = ComprasFactory.compraParaUsuario(usuario1);
    	compra.agregarUsuarioValidador(usuario2);
    	
    	ValidacionDeEgresos.validarCompra(compra);
    	
    	assertEquals(1, usuario2.getBandejaDeMensajes().size());
    	
    	String motivo = "No se ha seleccionado ningun presupuesto";
    	assertMensajeUnico(usuario1, MensajeFactory.mensajeDeRechazo(compra, motivo).getValue());
    }
    
    @Test
    public void usuarioRecibeMasDeUnaNotificacion(){
    	ValidacionDeEgresos.validarCompra(ComprasFactory.compraParaUsuario(usuario1));
    	ValidacionDeEgresos.validarCompra(ComprasFactory.compraParaUsuario(usuario1));
    	
    	assertEquals(2, usuario1.getBandejaDeMensajes().size());
    }
    
    
    //************ Auxiliares **************
    
    public void assertMensajeUnico(Usuario usuario, String contenido){
    	assertEquals(1,usuario.getBandejaDeMensajes().size());
    	assertEquals(contenido,usuario.getBandejaDeMensajes().get(0).getValue());
    }
}
