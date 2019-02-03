package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.ControladorBO;
import org.ec.jap.dao.sistema.impl.ControladorDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ControladorBOImpl extends ControladorDAOImpl implements
		ControladorBO {

	/**
	 * Default constructor.
	 */
	public ControladorBOImpl() {
	}
}
