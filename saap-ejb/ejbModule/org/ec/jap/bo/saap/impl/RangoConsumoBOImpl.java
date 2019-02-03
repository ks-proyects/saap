package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.dao.saap.impl.RangoConsumoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class RangoConsumoBOImpl extends RangoConsumoDAOImpl implements RangoConsumoBO {

	/**
	 * Default constructor.
	 */
	public RangoConsumoBOImpl() {
	}
}
