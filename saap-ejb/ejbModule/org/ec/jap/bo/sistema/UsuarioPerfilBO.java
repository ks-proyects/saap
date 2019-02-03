package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.UsuarioPerfilDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.UsuarioPerfil;

@Local
public interface UsuarioPerfilBO extends UsuarioPerfilDAO {

	public List<UsuarioPerfil> getByUser(Usuario usuario) throws Exception;

	void savePerfil(List<UsuarioPerfil> usuarioPerfils, Usuario usuario) throws Exception;

}
