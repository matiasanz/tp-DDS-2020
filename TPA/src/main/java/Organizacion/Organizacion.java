package Organizacion;

import java.util.List;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import Compra.Compra;
import Entidad.Entidad;
import Entidad.EntidadJuridica;
import Repositorios.RepositorioDeCategorias.RepositorioDeCategorias;
import Repositorios.RepositorioDeEntidades.RepositorioDeEntidades;
import Repositorios.RepositorioDeEtiquetas.RepositorioEtiquetas;
import Repositorios.RepositorioDeUsuarios.RepositorioDeUsuarios;
import Usuario.Usuario;

public class Organizacion {
    private RepositorioDeCategorias repositorioDeCategorias;
    private RepositorioDeUsuarios repositorioDeUsuarios;
    private final RepositorioEtiquetas repositorioDeEtiquetas;
    private RepositorioDeEntidades repositorioDeEntidades;

    public Organizacion(RepositorioDeUsuarios repoDeUsuarios, RepositorioDeCategorias repoDeCategorias, RepositorioEtiquetas repoDeEtiquetas, RepositorioDeEntidades repoDeEntidades) {
        this.repositorioDeUsuarios = repoDeUsuarios;
        this.repositorioDeCategorias = repoDeCategorias;
        this.repositorioDeEtiquetas = repoDeEtiquetas;
        this.repositorioDeEntidades = repoDeEntidades;
    }

    public List<Usuario> getUsuarios() {
        return repositorioDeUsuarios.getUsuarios();
    }

    public Usuario getUsuarioEspecifico(String nombreDeUsuario) {
    	return repositorioDeUsuarios.getUsuarios().stream().filter(usuario -> usuario.getUsername().equalsIgnoreCase(nombreDeUsuario)).collect(Collectors.toList()).get(0);
    }
    
    public List<Entidad> getEntidades() {
        return repositorioDeEntidades.getEntidades();
    }

    public void agregarEntidad(EntidadJuridica entidad) {
        repositorioDeEntidades.nuevaEntidad(entidad);
    }

    public void crearUsuario(String username, String password) {
        repositorioDeUsuarios.agregarUsuario(username, password);
    }

    public void autenticarUsuario(String username, String password) {
        repositorioDeUsuarios.autenticarUsuario(username, password);
    }

    public List<Compra> getCompras() {
        return this.getEntidades().stream().map(entidad -> entidad.getCompras()).flatMap(List::stream).collect(Collectors.toList());
    }

    public BigDecimal getValorTodasLasCompras() {
        return getCompras().stream().map(Compra::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validarCompras() {
        this.comprasPendientesDeValidacion().forEach(unaCompra -> unaCompra.validar());
    }

    public List<Compra> comprasPendientesDeValidacion() {
        return this.getEntidades().stream().map(entidad -> entidad.getComprasPendientesDeValidacion()).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Compra> comprasQuePuedeValidar(Usuario miUsuario) {
        List<Compra> comprasPorValidar = this.comprasPendientesDeValidacion();
        return comprasPorValidar.stream().filter(unaCompra -> unaCompra.puedeSerValidadaPor(miUsuario)).collect(Collectors.toList());
    }

}
