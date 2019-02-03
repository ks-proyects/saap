package org.ec.jap.bo.sistema.impl;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.DocumentoAdjuntoBO;
import org.ec.jap.dao.sistema.impl.DocumentoAdjuntoDAOImpl;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class DocumentoAdjuntoBOImpl extends DocumentoAdjuntoDAOImpl implements DocumentoAdjuntoBO {

	/**
	 * Default constructor.
	 */
	public DocumentoAdjuntoBOImpl() {
	}
}
