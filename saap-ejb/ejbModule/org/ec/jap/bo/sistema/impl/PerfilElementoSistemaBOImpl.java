package org.ec.jap.bo.sistema.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.ElementoSistemaBO;
import org.ec.jap.bo.sistema.PerfilElementoSistemaBO;
import org.ec.jap.dao.sistema.impl.PerfilElementoSistemaDAOImpl;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.entiti.sistema.Perfil;
import org.ec.jap.entiti.sistema.PerfilElementoSistema;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class PerfilElementoSistemaBOImpl extends PerfilElementoSistemaDAOImpl implements PerfilElementoSistemaBO {

	@EJB
	ElementoSistemaBO elementoSistemaBO;

	/**
	 * Default constructor.
	 */
	public PerfilElementoSistemaBOImpl() {
	}

	@Override
	public List<PerfilElementoSistema> generateTree(Perfil perfil) throws Exception {

		List<PerfilElementoSistema> perfilElementoSistemas = new ArrayList<>(0);
		map.clear();
		map.put("idPerfil", perfil);
		List<Object[]> idPerfil = findObjects("PerfilElementoSistema.fyndAllByPerfil", map);

		for (Object[] objects : idPerfil) {
			PerfilElementoSistema perfilElementoSistema = (PerfilElementoSistema) objects[0];
			ElementoSistema elementoSistema = (ElementoSistema) objects[1];
			if (perfilElementoSistema == null) {
				perfilElementoSistema = new PerfilElementoSistema();
				perfilElementoSistema.setIdElementoSistema(elementoSistema);
				perfilElementoSistema.setIdPerfil(perfil);
				perfilElementoSistema.setSeleccionado(false);
			} else
				perfilElementoSistema.setSeleccionado(true);

			perfilElementoSistemas.add(perfilElementoSistema);

		}
		return perfilElementoSistemas;
	}

	@Override
	public void saveOptions(Usuario usuario, List<PerfilElementoSistema> perfilElementoSistemas, Perfil perfil) throws Exception {

		em().createQuery("DELETE FROM PerfilElementoSistema WHERE idPerfil=:idPerfil").setParameter("idPerfil", perfil).executeUpdate();

		for (PerfilElementoSistema perfilElementoSistema : perfilElementoSistemas) {

			List<?> name = em().createQuery("SELECT COUNT(els.idPerfilElementoSistema) FROM PerfilElementoSistema els WHERE els.idElementoSistema=:idElementoSistema AND els.idPerfil=:idPerfil )").setParameter("idPerfil", perfilElementoSistema.getIdPerfil()).setParameter("idElementoSistema", perfilElementoSistema.getIdElementoSistema()).getResultList();
			if (Long.valueOf(name.get(0).toString()).intValue() == 0) {
				PerfilElementoSistema sistema = new PerfilElementoSistema();
				sistema.setAcctivo("S");
				sistema.setIdElementoSistema(perfilElementoSistema.getIdElementoSistema());
				sistema.setIdPerfil(perfilElementoSistema.getIdPerfil());
				save(usuario, sistema);
				savePadre(usuario, perfilElementoSistema.getIdElementoSistema().getIdElementoSistemaPadre(), perfilElementoSistema);
			}
		}
	}

	public void savePadre(Usuario usuario, Integer idElementoSistemaPadre, PerfilElementoSistema perfilElementoSistema) throws Exception {
		if (idElementoSistemaPadre != -1) {
			ElementoSistema elementoSistema = elementoSistemaBO.findByPk(idElementoSistemaPadre);
			List<?> name = em().createQuery("SELECT COUNT(els.idPerfilElementoSistema) FROM PerfilElementoSistema els WHERE els.idElementoSistema=:idElementoSistema AND els.idPerfil=:idPerfil )").setParameter("idPerfil", perfilElementoSistema.getIdPerfil()).setParameter("idElementoSistema", elementoSistema).getResultList();
			if (Long.valueOf(name.get(0).toString()).intValue() == 0) {
				PerfilElementoSistema sistema = new PerfilElementoSistema();
				sistema.setAcctivo("S");
				sistema.setIdElementoSistema(elementoSistema);
				sistema.setIdPerfil(perfilElementoSistema.getIdPerfil());
				save(usuario, sistema);
				savePadre(usuario, elementoSistema.getIdElementoSistemaPadre(), perfilElementoSistema);
			}
		} else{
			return;
		}
			
	}
}
