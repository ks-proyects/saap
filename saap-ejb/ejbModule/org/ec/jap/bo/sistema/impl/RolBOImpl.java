package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.RolBO;
import org.ec.jap.dao.sistema.impl.RolDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class RolBOImpl extends RolDAOImpl implements RolBO {

	/**
	 * Default constructor.
	 */
	public RolBOImpl() {
	}
}
