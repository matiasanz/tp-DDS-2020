package Repositorios.RepositorioDeUsuarios;
import javax.persistence.NoResultException;

import Exceptions.UsuarioNoExisteException;
import Exceptions.UsuarioYaExisteException;
import Repositorios.RepoDB;
import Usuario.Usuario;

public class RepoUsuariosDB extends RepoDB<Usuario>{
	
	@Override
	protected String className(){
		return "Usuario";
	}
	
	public Usuario getUsuario(String nombre){
		Usuario usuario; 
		try{
			usuario = (Usuario) query("where username = :user")
				.setParameter("user", nombre)
				.getSingleResult();
		} 
		
		catch(NoResultException e){			
			throw new UsuarioNoExisteException(nombre);
		}
		
		return usuario;
	}
	
	public Usuario getUsuario(Long id){
		Usuario usuario; 
		try{
			usuario = (Usuario) query("where id = :id_user")
				.setParameter("id_user", id)
				.getSingleResult();
		} 
		
		catch(NoResultException e){			
			throw new UsuarioNoExisteException(id);
		}
		
		return usuario;	
	}
	
	@Override
	public void agregar(Usuario usuario){
		validarNoRepetido(usuario.getUsername());
		super.agregar(usuario);
	}
	
	public boolean nombreOcupado(String username){
        try{
        	getUsuario(username);
        	return true;
        } catch (UsuarioNoExisteException e){
        	return false;
        }
    }
	
    public void validarNoRepetido(String username){
    	if (this.nombreOcupado(username)) {
			throw new UsuarioYaExisteException();
		}
    }
}

