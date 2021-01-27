package org.ec.jap.bo.saap.impl;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.dao.saap.impl.TarifaDAOImpl;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TarifaBOImpl extends TarifaDAOImpl implements TarifaBO {

	/**
	 * Default constructor.
	 */
	public TarifaBOImpl() {
	}

	@Override
	public Double getValorMulta(Usuario user) throws Exception {
		map= new HashMap<String, Object>();
		map.put("idUsuario", user);
		Double tarifa=(Double) findObjectByNamedQuery("Tarifa.findMaxMulta", map);
		return tarifa;
	}
}
