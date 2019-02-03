package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.dao.saap.impl.PeriodoPagoDAOImpl;

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
}
