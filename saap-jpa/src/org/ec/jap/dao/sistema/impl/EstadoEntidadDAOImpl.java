/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.EstadoEntidadDAO;
import org.ec.jap.entiti.sistema.EstadoEntidad;

/**
 * Clase de Acceso a Datos de {@link EstadoEntidad}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class EstadoEntidadDAOImpl extends SistemaImple<EstadoEntidad, Integer> implements EstadoEntidadDAO {

	public EstadoEntidadDAOImpl() {
		super(EstadoEntidad.class);
	}

}
