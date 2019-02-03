package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.AuditoriaBO;
import org.ec.jap.dao.sistema.impl.AuditoriaDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class AuditoriaBOImpl extends AuditoriaDAOImpl implements AuditoriaBO {

	/**
	 * Default constructor.
	 */
	public AuditoriaBOImpl() {
	}
}
