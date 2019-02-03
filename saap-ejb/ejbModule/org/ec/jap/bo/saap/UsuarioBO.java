package org.ec.jap.bo.saap;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.UsuarioDAO;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface UsuarioBO extends UsuarioDAO {

	public Usuario findUserByUsername(String username) throws Exception;

	public List<Usuario> findBenefeciarios(String namedQuerry, HashMap<String, Object> map) throws Exception;

	List<Usuario> findBenefeciariosAsistencias(String namedQuery, HashMap<String, Object> map) throws Exception;
}
