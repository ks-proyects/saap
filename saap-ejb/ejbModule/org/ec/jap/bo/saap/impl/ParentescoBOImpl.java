package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ParentescoBO;
import org.ec.jap.dao.saap.impl.ParentescoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ParentescoBOImpl extends ParentescoDAOImpl implements ParentescoBO {

	/**
	 * Default constructor.
	 */
	public ParentescoBOImpl() {
	}
}
