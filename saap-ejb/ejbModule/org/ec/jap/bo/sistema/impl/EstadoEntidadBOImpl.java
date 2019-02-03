package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.EstadoEntidadBO;
import org.ec.jap.dao.sistema.impl.EstadoEntidadDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class EstadoEntidadBOImpl extends EstadoEntidadDAOImpl implements EstadoEntidadBO {

	/**
	 * Default constructor.
	 */
	public EstadoEntidadBOImpl() {
	}
}
