package Categoria;

import Entidad.EntidadJuridica;

import java.math.BigDecimal;
import java.util.List;

public class Categoria {

    String nombre;
    private List<Validador> validadores;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    void agregarValidador(Validador validador){
        validadores.add(validador);
    }

    void eliminarValidador(Validador validador) {
        validadores.remove(validador);
    }

    public void notificarCompraAgregada(BigDecimal montoCompra, BigDecimal montoAcumuladoEntidad){
        validadores.stream().forEach(validador -> validador.validarGasto(montoCompra, montoAcumuladoEntidad));
    }

    public void notificarEntidadBaseAgregada(){
        validadores.stream().forEach(validador -> validador.validarBloqueoDeAgregarEntidadesBase());
    }

    public void notificarMeAgregueAUnaJuridica(EntidadJuridica entidad){
        validadores.stream().forEach(validador -> validador.validarSiEntidadJuridicaEstaBloqueada(entidad));
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Validador> getValidadores() {
        return validadores;
    }

    public void setValidadores(List<Validador> validadores) {
        this.validadores = validadores;
    }
}
