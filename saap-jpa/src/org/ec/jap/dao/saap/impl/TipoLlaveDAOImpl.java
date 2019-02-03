/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.TipoLlaveDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.TipoLlave;

/**
 * Clase de Acceso a Datos de {@link TipoLlave}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class TipoLlaveDAOImpl extends SistemaImple<TipoLlave, String> implements TipoLlaveDAO {

	public TipoLlaveDAOImpl() {
		super(TipoLlave.class);
	}

}
