package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.dao.saap.impl.TarifaDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TarifaBOImpl extends TarifaDAOImpl implements TarifaBO {

	/**
	 * Default constructor.
	 */
	public TarifaBOImpl() {
	}
}
