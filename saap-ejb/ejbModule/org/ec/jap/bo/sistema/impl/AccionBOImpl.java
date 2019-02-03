package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.AccionBO;
import org.ec.jap.dao.sistema.impl.AccionDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class AccionBOImpl extends AccionDAOImpl implements AccionBO {

	/**
	 * Default constructor.
	 */
	public AccionBOImpl() {

	}
}
