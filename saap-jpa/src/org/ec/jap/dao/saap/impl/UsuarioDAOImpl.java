/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.UsuarioDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Clase de Acceso a Datos de {@link Usuario}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class UsuarioDAOImpl extends SistemaImple<Usuario, Integer> implements UsuarioDAO {

	public UsuarioDAOImpl() {
		super(Usuario.class);
	}

}
