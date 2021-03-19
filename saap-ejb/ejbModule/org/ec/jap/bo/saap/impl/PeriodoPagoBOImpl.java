package org.ec.jap.bo.saap.impl;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.dao.saap.impl.PeriodoPagoDAOImpl;
import org.ec.jap.entiti.saap.PeriodoPago;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class PeriodoPagoBOImpl extends PeriodoPagoDAOImpl implements PeriodoPagoBO {

	/**
	 * Default constructor.
	 */
	public PeriodoPagoBOImpl() {
	}

	@Override
	public PeriodoPago findByIdCustom(Integer idPeriodo) throws Exception {
		map = new HashMap<>(0);
		map.put("idPeriodoPago",idPeriodo);
		return findByNamedQuery("PeriodoPago.findById", map);
	}
}
