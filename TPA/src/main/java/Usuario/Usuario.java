package Usuario;

import java.util.List;

import Repositorios.RepositorioDeMensajes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Usuario {
    //private Tipo tipo;
    //private List<String> mensajes = new LinkedList<String>();

    @Id
    @GeneratedValue
    private long id;

    private String username;
    private String contrasenia;

    @Transient
    private RepositorioDeMensajes mensajes = new RepositorioDeMensajes();

    public String getContrasenia() {
        return contrasenia;
    }

    public String getUsername() {
        return username;
    }

    public Usuario(String username, String contrasenia) {

        ValidadorUsuario validacion = new ValidadorUsuario();

        this.username = username;

        validacion.validarContrasenia(contrasenia, username);
        this.contrasenia = contrasenia;

    }

    public void cambiarContrasenia(String contrasenia) {

        ValidadorUsuario validacion = new ValidadorUsuario();

        validacion.validarContrasenia(contrasenia, username);
        this.contrasenia = contrasenia;

    }
    
    public void notificarEvento(String mensaje){
    	mensajes.logMensaje(this,mensaje);
    	//    	mensajes.add(mensaje);
    }
    
    public List<String> getMensajes(){
    	return mensajes.getMensajes(this);
    	//    	return mensajes;
    }

    public boolean equals(Usuario otroUsuario){
        String username = otroUsuario.getUsername();
        String password = otroUsuario.getContrasenia();
        return this.autentica(username,password);
    }

    public boolean autentica(String username, String password){
        return this.username.equals(username) && this.contrasenia.equals(password);
    }

}
