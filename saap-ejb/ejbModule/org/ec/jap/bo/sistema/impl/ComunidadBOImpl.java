package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.ComunidadBO;
import org.ec.jap.dao.sistema.impl.ComunidadDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ComunidadBOImpl extends ComunidadDAOImpl implements ComunidadBO {

	/**
	 * Default constructor.
	 */
	public ComunidadBOImpl() {
	}
}
