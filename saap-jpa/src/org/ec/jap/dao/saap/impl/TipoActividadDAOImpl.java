/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.TipoActividadDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.TipoActividad;

/**
 * Clase de Acceso a Datos de {@link TipoActividad}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class TipoActividadDAOImpl extends SistemaImple<TipoActividad, Integer> implements TipoActividadDAO {

	public TipoActividadDAOImpl() {
		super(TipoActividad.class);
	}

}
