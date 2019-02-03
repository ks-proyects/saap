/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.AuditoriaDAO;
import org.ec.jap.entiti.sistema.Auditoria;

/**
 * Clase de Acceso a Datos de {@link Auditoria}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class AuditoriaDAOImpl extends SistemaImple<Auditoria, Integer> implements AuditoriaDAO {

	public AuditoriaDAOImpl() {
		super(Auditoria.class);
	}

}
