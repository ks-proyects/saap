package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.AccionNegocioBO;
import org.ec.jap.dao.sistema.impl.AccionNegocioDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class AccionNegocioBOImpl extends AccionNegocioDAOImpl implements
		AccionNegocioBO {

	/**
	 * Default constructor.
	 */
	public AccionNegocioBOImpl() {
	}
}
