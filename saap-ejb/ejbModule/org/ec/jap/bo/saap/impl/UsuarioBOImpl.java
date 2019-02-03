package org.ec.jap.bo.saap.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.dao.saap.impl.UsuarioDAOImpl;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class UsuarioBOImpl extends UsuarioDAOImpl implements UsuarioBO {

	/**
	 * Default constructor.
	 */
	public UsuarioBOImpl() {
	}
	
	public Usuario findUserByUsername(String username)throws Exception {
		HashMap<String, Object> p= new HashMap<>();
		p.put("username", username);
		return findByNamedQuery("Usuario.findByUsername", p);
	}

	@Override
	public List<Usuario> findBenefeciarios(String namedQuery, HashMap<String, Object> map) throws Exception{
		List<Usuario> usuarios=findAllByNamedQuery(namedQuery, map);
		HashMap<String, Object> hashMap= new HashMap<String,Object>();
		for (Usuario usuario : usuarios) {
			hashMap.clear();
			hashMap.put("idUsuario", usuario);
			Object object=findObjectByNamedQuery("Asistencia.findByUsuario", hashMap);
			Double cantidadRayas=Double.valueOf(object!=null?object.toString():"0");
			usuario.setCantidadRayas(cantidadRayas);
		}
		return usuarios;
	}
	@Override
	public List<Usuario> findBenefeciariosAsistencias(String namedQuery, HashMap<String, Object> map) throws Exception{
		List<Object[]> objestos=findObjects(namedQuery, map);
		List<Usuario> usuarios=new ArrayList<>(0);
		for (Object[] objects : objestos) {
			Usuario usuario=(Usuario)objects[0];
			Double cantidadRayas=Double.valueOf(objects[1]!=null?objects[1].toString():"0");
			usuario.setCantidadRayas(cantidadRayas);
			usuarios.add(usuario);
		}
		
		
		return usuarios;
	}
}
