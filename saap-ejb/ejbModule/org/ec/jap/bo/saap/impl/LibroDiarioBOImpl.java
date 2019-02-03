package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.LibroDiarioBO;
import org.ec.jap.dao.saap.impl.LibroDiarioDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class LibroDiarioBOImpl extends LibroDiarioDAOImpl implements LibroDiarioBO {

	/**
	 * Default constructor.
	 */
	public LibroDiarioBOImpl() {
	}
}
