package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.EstadoCivilBO;
import org.ec.jap.dao.saap.impl.EstadoCivilDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class EstadoCivilBOImpl extends EstadoCivilDAOImpl implements EstadoCivilBO {

	/**
	 * Default constructor.
	 */
	public EstadoCivilBOImpl() {
	}
}
