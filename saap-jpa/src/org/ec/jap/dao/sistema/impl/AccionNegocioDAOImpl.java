/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.AccionNegocioDAO;
import org.ec.jap.entiti.sistema.AccionNegocio;

/**
 * Clase de Acceso a Datos de {@link AccionNegocio}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class AccionNegocioDAOImpl extends SistemaImple<AccionNegocio, Integer> implements AccionNegocioDAO {

	public AccionNegocioDAOImpl() {
		super(AccionNegocio.class);
	}

}
