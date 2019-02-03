/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.EntidadCambioEstadoDAO;
import org.ec.jap.entiti.sistema.EntidadCambioEstado;

/**
 * Clase de Acceso a Datos de {@link EntidadCambioEstado}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class EntidadCambioEstadoDAOImpl extends SistemaImple<EntidadCambioEstado, Integer> implements EntidadCambioEstadoDAO {

	public EntidadCambioEstadoDAOImpl() {
		super(EntidadCambioEstado.class);
	}

}
