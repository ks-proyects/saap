package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.dao.saap.impl.LlaveDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class LlaveBOImpl extends LlaveDAOImpl implements LlaveBO {

	/**
	 * Default constructor.
	 */
	public LlaveBOImpl() {
	}
}
