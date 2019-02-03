/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.TipoEntidadDAO;
import org.ec.jap.entiti.saap.TipoActividad;
import org.ec.jap.entiti.sistema.TipoEntidad;

/**
 * Clase de Acceso a Datos de {@link TipoActividad}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class TipoEntidadDAOImpl extends SistemaImple<TipoEntidad, String> implements TipoEntidadDAO {

	public TipoEntidadDAOImpl() {
		super(TipoEntidad.class);
	}

}
