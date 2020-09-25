package Repositorios;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import Factory.DataSourceFactory;
import Factory.UsuariosFactory;
import Organizacion.IngresoFallidoException;
import Repositorios.RepositorioDeUsuarios.RepoUsuariosDB;
import Usuario.Usuario;

public class TestPersistenciaRepoUsuario extends AbstractPersistenceTest implements WithGlobalEntityManager {
	RepoUsuariosDB repo;
	Usuario usuario = UsuariosFactory.usuarioStub();
	
	@Before
	public void init(){
		repo = new RepoUsuariosDB(DataSourceFactory.createDatasource("schema.sql"));
		repo.add(usuario);
	}

	@After
	public void deleteAll(){
		repo.delete(usuario);
	}
	
	@Test (expected = IngresoFallidoException.class)
	public void seIngresanDatosIncorrectosYNoEncuentraNingunUsuario(){
		repo.getUsuario("Juan Carlos","intento 34.561 de fuerza bruta");
	}
	
	@Test
    public void usuarioSeRecuperaDeCache(){
		Usuario recuperado = repo.getUsuario(usuario.getUsername(),usuario.getContrasenia());
		assertSame(usuario, recuperado);
	}

	@Test
	public void usuarioSeRecuperaDeBD(){

		Usuario recuperado = repo.findByUsernameYContrasenia(usuario.getUsername(),usuario.getContrasenia());
	
		assertNotNull(recuperado);
		assertEquals(usuario.getUsername(), recuperado.getUsername());
		assertEquals(usuario.getContrasenia(), recuperado.getContrasenia());
	}
}
