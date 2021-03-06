package Main;

import Categoria.Categoria;
import Compra.Compra;
import Entidad.Entidad;
import Entidad.EntidadJuridica;
import Factory.ComprasFactory;
import Factory.EntidadesFactory;
import Factory.MedioDePagoFactory;
import Factory.ProveedoresFactory;
import Factory.UsuariosFactory;
import MedioDePago.PagoEnEfectivo;
import Moneda.CodigoMoneda;
import Moneda.Moneda;
import Repositorios.RepositorioDeEntidades;
import Repositorios.RepositorioDeCompras.RepoComprasDB;
import Repositorios.RepositorioDeMonedas.RepositorioDeMonedasMeli;
import Repositorios.RepositorioDeUsuarios.RepoUsuariosDB;

import java.time.LocalDate;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

	public static void main(String[] args) {
        new Bootstrap().run();
    }

    public void run() {

        withTransaction(() -> {
            crearProveedores();
            crearMediosDePago();
            crearEntidadesYCategorias();
            crearCompras();
            crearUsuarios();
        });

        System.out.println("Boostrap complete");
        System.exit(0);
    }

    private void crearUsuarios() {
        RepoUsuariosDB repoUsuarios = new RepoUsuariosDB();
        repoUsuarios.salvar(UsuariosFactory.usuarioStub());
        repoUsuarios.salvar(UsuariosFactory.usuarioConMensajes());
    }
    
    private void crearCompras(){
    	RepoComprasDB compras = new RepoComprasDB();
    	
    	Entidad unaEntidad = RepositorioDeEntidades.instancia.getEntidades().get(0);
    	
    	if(unaEntidad!=null){
    		Compra compra = ComprasFactory.compraParaEntidad(unaEntidad);
    		compras.salvar(compra);
    	}
    }

    private void crearEntidadesYCategorias() {
	    Categoria corpo = new Categoria("Corporacion");
	    Categoria alimentos = new Categoria("Industria Alimenticia");

        persist(new Categoria("ONG"));
        persist(corpo);
        persist(alimentos);

	    EntidadJuridica mc = EntidadesFactory.empresaMedianaTramo2();
	    mc.agregarCategoria(corpo);
	    mc.agregarCategoria(alimentos);

        persist(mc);
        persist(EntidadesFactory.getEntidadJuridica());
    }

    private void crearProveedores() {
        persist(ProveedoresFactory.ProveedorJuanPerez());
        persist(ProveedoresFactory.ProveedorMaster());
        persist(ProveedoresFactory.ProveedorOne());
    }

    private void crearMediosDePago() {
        persist(MedioDePagoFactory.effectivo());
        persist(MedioDePagoFactory.tarjetaDeCreditoVisa());
        persist(MedioDePagoFactory.tarjetaDeDebito());
    }
}