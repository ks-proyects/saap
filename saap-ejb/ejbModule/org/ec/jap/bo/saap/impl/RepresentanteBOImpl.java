package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.RepresentanteBO;
import org.ec.jap.dao.saap.impl.RepresentanteDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class RepresentanteBOImpl extends RepresentanteDAOImpl implements RepresentanteBO {

	/**
	 * Default constructor.
	 */
	public RepresentanteBOImpl() {
	}
}
