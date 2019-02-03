/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.TipoRegistroDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.TipoRegistro;

/**
 * /** Clase de Acceso a Datos de {@link TipoRegistro}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class TipoRegistroDAOImpl extends SistemaImple<TipoRegistro, String> implements TipoRegistroDAO {

	public TipoRegistroDAOImpl() {
		super(TipoRegistro.class);
	}

}
