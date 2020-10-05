package Repositorios;

import java.time.LocalDate;
import java.util.List;
import Compra.Compra;
import Compra.Estado;

public class RepoComprasDB extends RepoDB<Compra>{	

	@Override
	protected String className(){
		return "Compra";
	}
	 
    @SuppressWarnings("unchecked")
	public List<Compra> getComprasPendientesDeAprobacion() {
        return createQuery("where indicadorDeAprobacion = :estado")
        		.setParameter("estado", Estado.PENDIENTEDEAPROBACION)
        		.getResultList();
    }

   //TODO
//    public RepositorioDeCompras repositorioDelMes(LocalDate fecha){
//    	return new RepositorioDeCompras(comprasDelMes(fecha));
//    }
//    
//    @SuppressWarnings("unchecked")
//	public List<Compra> comprasDelMes(LocalDate fechaInicio) {
//        return createQuery("where month(fechaOperacion) = :mes")
//        		.setParameter("mes", new Integer(fechaInicio.getMonthValue()))
//        		.getResultList();
//    }
}
