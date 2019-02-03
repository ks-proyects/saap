package org.ec.jap.bo.saap.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.DestinoBO;
import org.ec.jap.dao.saap.impl.DestinoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class DestinoBOImpl extends DestinoDAOImpl implements DestinoBO {

	/**
	 * Default constructor.
	 */
	public DestinoBOImpl() {
	}
}
