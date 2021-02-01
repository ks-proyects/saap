package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.dao.saap.impl.ServicioDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ServicioBOImpl extends ServicioDAOImpl implements ServicioBO {

	/**
	 * Default constructor.
	 */
	public ServicioBOImpl() {
	}
}
