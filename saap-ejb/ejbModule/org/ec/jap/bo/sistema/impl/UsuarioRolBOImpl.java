package org.ec.jap.bo.sistema.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.UsuarioRolBO;
import org.ec.jap.dao.sistema.impl.UsuarioRolDAOImpl;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Rol;
import org.ec.jap.entiti.sistema.UsuarioRol;
import org.ec.jap.enumerations.ValorSiNo;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class UsuarioRolBOImpl extends UsuarioRolDAOImpl implements UsuarioRolBO {

	/**
	 * Default constructor.
	 */
	public UsuarioRolBOImpl() {
	}

	@Override
	public List<UsuarioRol> getByUser(Usuario usuario) throws Exception {
		List<UsuarioRol> usuarioRols = new ArrayList<>(0);
		map.clear();
		map.put("idUsuario", usuario);
		map.put("activo", ValorSiNo.valueOf("S"));
		List<Object[]> idPerfil = findObjects("UsuarioRol.findByRol", map);

		for (Object[] objects : idPerfil) {
			UsuarioRol usuarioRol = (UsuarioRol) objects[0];
			Rol rol = (Rol) objects[1];
			if (usuarioRol == null) {
				usuarioRol = new UsuarioRol();
				usuarioRol.setActivo(ValorSiNo.valueOf("N"));
				usuarioRol.setIdRol(rol);
				usuarioRol.setIdUsuario(usuario);
			} else {
				usuarioRol.setActivo(ValorSiNo.valueOf("S"));
			}
			usuarioRols.add(usuarioRol);
		}
		return usuarioRols;
	}

	@Override
	public void savePerfil(List<UsuarioRol> usuarioRols, Usuario usuarioCurrent)throws Exception {
		for (UsuarioRol usuarioRol : usuarioRols) {
			if (usuarioRol.getIdUsuarioRol() == null && ValorSiNo.S.equals(usuarioRol.getActivo())) {
				save(usuarioCurrent, usuarioRol);
			} else if (usuarioRol.getIdUsuarioRol() != null && ValorSiNo.N.equals(usuarioRol.getActivo())) {
				delete(usuarioCurrent, usuarioRol);
			}
		}
	}
}
