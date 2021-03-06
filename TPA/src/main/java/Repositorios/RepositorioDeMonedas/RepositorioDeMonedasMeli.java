package Repositorios.RepositorioDeMonedas;

import Moneda.CodigoMoneda;
import Moneda.Moneda;
import Repositorios.MeliApi;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.sun.jersey.api.client.ClientResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioDeMonedasMeli implements RepositorioDeMonedas {

	private static final RepositorioDeMonedasMeli self = new RepositorioDeMonedasMeli();
    private final MeliApi meliApi;
    private List<Moneda> monedasCache = new ArrayList<>();

    public RepositorioDeMonedasMeli() {
        this.meliApi = new MeliApi();
    }

    public static RepositorioDeMonedasMeli getInstance(){
    	return self;
    }
    
    @Override
    public Moneda getMoneda(CodigoMoneda codigoMoneda) {

        Optional<Moneda> moneda = this.buscarEnCache(codigoMoneda);
        if (moneda.isPresent())
            return moneda.get();

        ClientResponse response;

        try {
            response = meliApi.obtenerMoneda(codigoMoneda);

        } catch (Exception e) {
            throw new MonedaNoEncontradaException(codigoMoneda);
        }

        return parseGetMonedaResponse(codigoMoneda, response.getEntity(String.class));
    }

    private Optional<Moneda> buscarEnCache(CodigoMoneda codigoMoneda) {

        return this.monedasCache.stream().filter(m -> m.getCodigo() == codigoMoneda).findFirst();
    }

    private Moneda parseGetMonedaResponse(CodigoMoneda codigoMoneda, String json) {

        ReadContext ctx = JsonPath.parse(json);
        return new Moneda(codigoMoneda, ctx.read("$.description"));
    }

    public List<Moneda> getMonedas(List<CodigoMoneda> codigoMonedas) {

        List<Moneda> monedas = new ArrayList<>();

        if (monedasCache.isEmpty())
            codigoMonedas.forEach(c -> {

                Optional<Moneda> moneda = this.buscarEnCache(c);
                if (!moneda.isPresent()) {
                    moneda = Optional.ofNullable(this.getMoneda(c));
                    monedasCache.add(moneda.get());
                }

                monedas.add(moneda.get());
            });

        return monedasCache;
    }
}
