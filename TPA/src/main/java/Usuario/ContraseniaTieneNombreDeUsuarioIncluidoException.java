package Usuario;

public class ContraseniaTieneNombreDeUsuarioIncluidoException extends RuntimeException {

    public ContraseniaTieneNombreDeUsuarioIncluidoException() {
    	super("La contraseņa ingresada no debe incluir el nombre de usuario");
    }
}