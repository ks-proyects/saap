package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.EntidadCambioEstadoBO;
import org.ec.jap.dao.sistema.impl.EntidadCambioEstadoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class EntidadCambioEstadoBOImpl extends EntidadCambioEstadoDAOImpl implements EntidadCambioEstadoBO {

	/**
	 * Default constructor.
	 */
	public EntidadCambioEstadoBOImpl() {
	}
}
