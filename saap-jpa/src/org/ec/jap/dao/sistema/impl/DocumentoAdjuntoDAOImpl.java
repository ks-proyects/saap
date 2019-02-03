/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.DocumentoAdjuntoDAO;
import org.ec.jap.entiti.sistema.DocumentoAdjunto;

/**
 * Clase de Acceso a Datos de {@link DocumentoAdjunto}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class DocumentoAdjuntoDAOImpl extends SistemaImple<DocumentoAdjunto, Integer> implements DocumentoAdjuntoDAO {

	public DocumentoAdjuntoDAOImpl() {
		super(DocumentoAdjunto.class);
	}

}
