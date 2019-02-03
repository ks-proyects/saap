package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.TipoEntidadBO;
import org.ec.jap.dao.sistema.impl.TipoEntidadDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TipoEntidadBOImpl extends TipoEntidadDAOImpl implements TipoEntidadBO {

	/**
	 * Default constructor.
	 */
	public TipoEntidadBOImpl() {
	}
}
