package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.dao.saap.impl.LlaveDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class LlaveBOImpl extends LlaveDAOImpl implements ServicioBO {

	/**
	 * Default constructor.
	 */
	public LlaveBOImpl() {
	}
}
