/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.AccionDAO;
import org.ec.jap.entiti.saap.Actividad;
import org.ec.jap.entiti.sistema.Accion;

/**
 * Clase de Acceso a Datos de {@link Actividad}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class AccionDAOImpl extends
		SistemaImple<Accion, Integer> implements AccionDAO {

	public AccionDAOImpl() {
		super(Accion.class);
	}
	

}
