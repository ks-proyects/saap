/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.ActividadDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Actividad;

/**
 * Clase de Acceso a Datos de {@link Actividad}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class ActividadDAOImpl extends SistemaImple<Actividad, Integer> implements ActividadDAO {

	public ActividadDAOImpl() {
		super(Actividad.class);
	}

}
