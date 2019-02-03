/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.CambioEstadoDAO;
import org.ec.jap.entiti.sistema.CambioEstado;

/**
 * Clase de Acceso a Datos de {@link CambioEstado}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class CambioEstadoDAOImpl extends SistemaImple<CambioEstado, Integer> implements CambioEstadoDAO {

	public CambioEstadoDAOImpl() {
		super(CambioEstado.class);
	}

}
