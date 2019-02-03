/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.PerfilDAO;
import org.ec.jap.entiti.sistema.Perfil;

/**
 * Clase de Acceso a Datos de {@link Perfil}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class PerfilDAOImpl extends SistemaImple<Perfil, Integer> implements PerfilDAO {

	public PerfilDAOImpl() {
		super(Perfil.class);
	}

}
