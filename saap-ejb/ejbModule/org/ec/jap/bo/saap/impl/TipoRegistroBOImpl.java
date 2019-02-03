package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.dao.saap.impl.TipoRegistroDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TipoRegistroBOImpl extends TipoRegistroDAOImpl implements TipoRegistroBO {

	/**
	 * Default constructor.
	 */
	public TipoRegistroBOImpl() {
	}
}
