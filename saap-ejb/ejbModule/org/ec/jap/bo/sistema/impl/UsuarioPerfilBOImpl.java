package org.ec.jap.bo.sistema.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.PerfilBO;
import org.ec.jap.bo.sistema.UsuarioPerfilBO;
import org.ec.jap.dao.sistema.impl.UsuarioPerfilDAOImpl;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Perfil;
import org.ec.jap.entiti.sistema.UsuarioPerfil;
import org.ec.jap.enumerations.PerfilEstado;
import org.ec.jap.enumerations.ValorSiNo;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class UsuarioPerfilBOImpl extends UsuarioPerfilDAOImpl implements UsuarioPerfilBO {

	@EJB
	PerfilBO perfilBO;

	/**
	 * Default constructor.
	 */
	public UsuarioPerfilBOImpl() {
	}

	@Override
	public List<UsuarioPerfil> getByUser(Usuario usuario) throws Exception {
		List<Perfil> perfils = new ArrayList<>(0);
		List<UsuarioPerfil> usuarioPerfils = new ArrayList<>(0);
		map.clear();
		map.put("activo", PerfilEstado.S);
		perfils = perfilBO.findAllByNamedQuery("Perfil.findActivos",map);
		map.clear();
		map.put("idUsuario", usuario);
		for (Perfil perfil : perfils) {
			map.put("idPerfil", perfil);
			UsuarioPerfil usuarioPerfil = findByNamedQuery("UsuarioPerfil.fyndByUserAndPerfil", map);
			if (usuarioPerfil != null)
				usuarioPerfils.add(usuarioPerfil);
			else {
				usuarioPerfil = new UsuarioPerfil();
				usuarioPerfil.setIdPerfil(perfil);
				usuarioPerfil.setIdUsuario(usuario);
				usuarioPerfils.add(usuarioPerfil);
			}
		}

		return usuarioPerfils;
	}

	@Override
	public void savePerfil(List<UsuarioPerfil> usuarioPerfils, Usuario usuario) throws Exception {
		for (UsuarioPerfil usuarioPerfil : usuarioPerfils) {
			if (usuarioPerfil.getIdUsuarioPerfil() == null && ValorSiNo.S.equals(usuarioPerfil.getActivo())) {
				save(usuario, usuarioPerfil);
			} else if (ValorSiNo.S.equals(usuarioPerfil.getActivo())) {
				update(usuario, usuarioPerfil);
			} else if (ValorSiNo.N.equals(usuarioPerfil.getActivo())) {
				delete(usuario, usuarioPerfil);
			}
		}

	}
}
