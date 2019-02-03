package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.UsuarioRolDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.UsuarioRol;

@Local
public interface UsuarioRolBO extends UsuarioRolDAO {

	List<UsuarioRol> getByUser(Usuario findByPk) throws Exception;

	void savePerfil(List<UsuarioRol> usuarioRols, Usuario usuarioCurrent) throws Exception;

}
