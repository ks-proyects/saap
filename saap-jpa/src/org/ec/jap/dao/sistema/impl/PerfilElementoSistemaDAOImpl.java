/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.PerfilElementoSistemaDAO;
import org.ec.jap.entiti.sistema.PerfilElementoSistema;

/**
 * Clase de Acceso a Datos de {@link PerfilElementoSistema}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class PerfilElementoSistemaDAOImpl extends SistemaImple<PerfilElementoSistema, Integer> implements PerfilElementoSistemaDAO {

	public PerfilElementoSistemaDAOImpl() {
		super(PerfilElementoSistema.class);
	}

}
