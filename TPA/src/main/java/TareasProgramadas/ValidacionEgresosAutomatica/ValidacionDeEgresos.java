package TareasProgramadas.ValidacionEgresosAutomatica;

import java.util.List;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import Compra.Compra;
import Compra.ValidadorDeCompra;
import Factory.ComprasFactory;
import Repositorios.RepositorioDeCompras.RepoComprasDB;

public class ValidacionDeEgresos implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    private static ValidadorDeCompra validadorDeCompra = new ValidadorDeCompra();

    public static RepoComprasDB repoCompras = new RepoComprasDB();

    public void execute()  {
//    	agregarDatosADB(); //

        withTransaction(() -> {

            System.out.println("\n***************************** INICIO VALIDADOR DE COMPRAS DIARIO *****************************\n");
            List<Compra> comprasAValidar = repoCompras.getComprasPendientesDeAprobacion();
            comprasAValidar.forEach(c -> validarCompra(c));
            System.out.println("\n***************************** " + comprasAValidar.size() + " COMPRAS FUERON VALIDADAS *****************************\n");

        });

    }

    private void agregarDatosADB() {
        withTransaction(() -> {
            repoCompras.salvar(ComprasFactory.getCompra19Julio2020Amoblamiento());
            repoCompras.salvar(ComprasFactory.getCompraFebrero2017SinEtiqueta());
        });
    }

    public static void validarCompra(Compra unaCompra) {

        String mensaje;
        try {
            validadorDeCompra.validar(unaCompra);
            unaCompra.aprobar();

            System.out.println("-----------<La Compra " + unaCompra.getId() + " ha sido aprobada>----------\n");
        } catch (RuntimeException unaExcepcion) {
            unaCompra.rechazar(unaExcepcion.getMessage());
            System.out.println("-----------<La Compra " + unaCompra.getId() + " ha sido rechazada>----------\n"
                    + "[Motivo: " + unaExcepcion.getMessage() + "]");
         
        }
    }
}