package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TipoFiltroBO;
import org.ec.jap.dao.sistema.impl.TipoFiltroDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TipoFiltroBOImpl extends TipoFiltroDAOImpl implements TipoFiltroBO {

	/**
	 * Default constructor.
	 */
	public TipoFiltroBOImpl() {
	}
}
