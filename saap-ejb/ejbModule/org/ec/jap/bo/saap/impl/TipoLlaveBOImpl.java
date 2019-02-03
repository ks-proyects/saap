package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TipoLlaveBO;
import org.ec.jap.dao.saap.impl.TipoLlaveDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class TipoLlaveBOImpl extends TipoLlaveDAOImpl implements TipoLlaveBO {

	/**
	 * Default constructor.
	 */
	public TipoLlaveBOImpl() {
	}
}
