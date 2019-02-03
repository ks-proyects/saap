/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.RolAccionNegocioDAO;
import org.ec.jap.entiti.sistema.RolAccionNegocio;

/**
 * Clase de Acceso a Datos de {@link RolAccionNegocio}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class RolAccionNegocioDAOImpl extends SistemaImple<RolAccionNegocio, Integer> implements RolAccionNegocioDAO {

	public RolAccionNegocioDAOImpl() {
		super(RolAccionNegocio.class);
	}

}
