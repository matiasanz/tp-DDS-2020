package Main;

import Factory.DireccionesFactory;
import Factory.EntidadesFactory;
import Factory.MedioDePagoFactory;
import Factory.ProveedoresFactory;
import Proveedor.Proveedor;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import Factory.UsuariosFactory;
import Repositorios.RepositorioDeUsuarios.RepoUsuariosDB;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
    public static void main(String[] args) {
        new Bootstrap().run();
    }

    public void run() {
        withTransaction(() -> {
            mockearUsuarios();
            crearProveedores();
            crearMediosDePago();
            crearEntidades();
        });
    }

    private void mockearUsuarios() {
        RepoUsuariosDB repoUsuarios = new RepoUsuariosDB();
        repoUsuarios.agregar(UsuariosFactory.usuarioStub());
        repoUsuarios.agregar(UsuariosFactory.sinValidaciones("beto", "123"));
    }

    private void crearEntidades() {
        persist(EntidadesFactory.empresaMedianaTramo2());
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