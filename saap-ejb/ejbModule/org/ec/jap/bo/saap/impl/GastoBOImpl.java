package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.GastoBO;
import org.ec.jap.dao.saap.impl.GastoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class GastoBOImpl extends GastoDAOImpl implements GastoBO {

	/**
	 * Default constructor.
	 */
	public GastoBOImpl() {
	}
}
