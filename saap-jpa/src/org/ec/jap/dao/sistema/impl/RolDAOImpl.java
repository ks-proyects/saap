/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.RolDAO;
import org.ec.jap.entiti.sistema.Rol;

/**
 * Clase de Acceso a Datos de {@link R Rol}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class RolDAOImpl extends SistemaImple<Rol, Integer> implements RolDAO {

	public RolDAOImpl() {
		super(Rol.class);
	}

}
