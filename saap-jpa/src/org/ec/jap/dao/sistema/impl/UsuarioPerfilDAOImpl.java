/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.UsuarioPerfilDAO;
import org.ec.jap.entiti.sistema.UsuarioPerfil;

/**
 * Clase de Acceso a Datos de {@link UsuarioPerfil}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class UsuarioPerfilDAOImpl extends SistemaImple<UsuarioPerfil, Integer> implements UsuarioPerfilDAO {

	public UsuarioPerfilDAOImpl() {
		super(UsuarioPerfil.class);
	}

}
