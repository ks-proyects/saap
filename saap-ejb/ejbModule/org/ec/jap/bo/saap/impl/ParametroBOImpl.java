package org.ec.jap.bo.saap.impl;

import java.util.Date;
import java.util.HashMap;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.dao.saap.impl.ParametroDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ParametroBOImpl extends ParametroDAOImpl implements ParametroBO {

	/**
	 * Default constructor.
	 */
	public ParametroBOImpl() {
	}

	public Integer getInteger(String tipoLlave, Integer idComunidad, String parametro) throws Exception {

		return (Integer) findObjectByNamedQuery("Parametro.findValorEntero", initParam(tipoLlave, idComunidad, parametro));
	}

	public Double getNumerico(String tipoLlave, Integer idComunidad, String parametro) throws Exception {
		return (Double) findObjectByNamedQuery("Parametro.findValorNumerico", initParam(tipoLlave, idComunidad, parametro));
	}

	public Date getFecha(String tipoLlave, Integer idComunidad, String parametro) throws Exception {
		return (Date) findObjectByNamedQuery("Parametro.findValorFecha", initParam(tipoLlave, idComunidad, parametro));
	}

	public String getString(String tipoLlave, Integer idComunidad, String parametro) throws Exception {
		return (String) findObjectByNamedQuery("Parametro.findValorString", initParam(tipoLlave, idComunidad, parametro));
	}

	public HashMap<String, Object> initParam(String tipoLlave, Integer idComunidad, String parametro) {

		HashMap<String, Object> map = new HashMap<>();

		map.put("tipoLlave", tipoLlave);
		map.put("idComunidad", idComunidad);
		map.put("parametro", parametro);
		return map;

	}
}
