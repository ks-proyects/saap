package org.ec.jap.bo.sistema.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.RolAccionNegocioBO;
import org.ec.jap.dao.sistema.impl.RolAccionNegocioDAOImpl;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.AccionNegocio;
import org.ec.jap.entiti.sistema.Rol;
import org.ec.jap.entiti.sistema.RolAccionNegocio;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class RolAccionNegocioBOImpl extends RolAccionNegocioDAOImpl implements RolAccionNegocioBO {

	/**
	 * Default constructor.
	 */
	public RolAccionNegocioBOImpl() {
	}

	@Override
	public List<RolAccionNegocio> generateTree(Rol rol) throws Exception {
		List<RolAccionNegocio> rolAccionNegocios = new ArrayList<>(0);
		map.clear();
		map.put("idRol", rol);
		List<Object[]> idRols = findObjects("RolAccionNegocio.fyndAllByRol", map);

		for (Object[] objects : idRols) {
			RolAccionNegocio rolAccionNegocio = (RolAccionNegocio) objects[0];
			AccionNegocio accionNegocio = (AccionNegocio) objects[1];
			if (rolAccionNegocio == null) {
				rolAccionNegocio = new RolAccionNegocio();
				rolAccionNegocio.setIdRol(rol);
				rolAccionNegocio.setIdAccionNegocio(accionNegocio);
				rolAccionNegocio.setSeleccionado(false);
			} else
				rolAccionNegocio.setSeleccionado(true);

			rolAccionNegocios.add(rolAccionNegocio);

		}
		return rolAccionNegocios;
	}

	@Override
	public void saveAccions(Usuario usuarioCurrent, List<RolAccionNegocio> rolAccionNegociosSeleccionado, Rol rol) throws Exception {
		em().createQuery("DELETE FROM RolAccionNegocio WHERE idRol=:idRol").setParameter("idRol", rol).executeUpdate();

		for (RolAccionNegocio rolAccionNegocio : rolAccionNegociosSeleccionado) {

			List<?> name = em().createQuery("SELECT COUNT(rola.idRolAccionNegocio) FROM RolAccionNegocio rola WHERE rola.idAccionNegocio=:idAccionNegocio AND rola.idRol=:idRol )").setParameter("idRol", rolAccionNegocio.getIdRol()).setParameter("idAccionNegocio", rolAccionNegocio.getIdAccionNegocio()).getResultList();
			if (Long.valueOf(name.get(0).toString()).intValue() == 0) {
				RolAccionNegocio rolAccionNegocioNew = new RolAccionNegocio();
				rolAccionNegocioNew.setIdAccionNegocio(rolAccionNegocio.getIdAccionNegocio());
				rolAccionNegocioNew.setIdRol(rolAccionNegocio.getIdRol());
				save(usuarioCurrent, rolAccionNegocioNew);
			}
		}

	}
}
