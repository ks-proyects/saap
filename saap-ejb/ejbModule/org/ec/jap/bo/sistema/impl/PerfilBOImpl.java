package org.ec.jap.bo.sistema.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.PerfilBO;
import org.ec.jap.bo.sistema.PerfilElementoSistemaBO;
import org.ec.jap.bo.sistema.UsuarioPerfilBO;
import org.ec.jap.dao.sistema.impl.PerfilDAOImpl;
import org.ec.jap.entiti.sistema.Perfil;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class PerfilBOImpl extends PerfilDAOImpl implements PerfilBO {

	/**
	 * Default constructor.
	 */
	@EJB
	UsuarioPerfilBO usuarioPerfilBO;
	@EJB
	PerfilElementoSistemaBO perfilElementoSistemaBO;

	public PerfilBOImpl() {
	}

	@Override
	public void existenUsuariosAsignados(Perfil perfil) throws Exception {
		map.clear();
		map.put("idPerfil", perfil);
		if (usuarioPerfilBO.findAllByNamedQuery("UsuarioPerfil.fyndByPerfil", map).size() > 0)
			throw new Exception("Existen usuarios que ya poseen este perfil no se puede eliminar.");

	}

	@Override
	public void existenOpcionesAsignados(Perfil perfil) throws Exception {
		map.clear();
		map.put("idPerfil", perfil);
		if (perfilElementoSistemaBO.findAllByNamedQuery("PerfilElementoSistema.fyndByPerfil", map).size() > 0)
			throw new Exception("Existen usuarios que ya poseen este perfil no se puede eliminar.");

	}
}
