/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.UsuarioRolDAO;
import org.ec.jap.entiti.sistema.UsuarioRol;

/**
 * Clase de Acceso a Datos de {@link UsuarioRol}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class UsuarioRolDAOImpl extends SistemaImple<UsuarioRol, Integer> implements UsuarioRolDAO {

	public UsuarioRolDAOImpl() {
		super(UsuarioRol.class);
	}

}
