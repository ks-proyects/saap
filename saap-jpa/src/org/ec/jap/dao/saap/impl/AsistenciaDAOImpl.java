/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.AsistenciaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Asistencia;

/**
 * Clase de Acceso a Datos de {@link Asistencia}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class AsistenciaDAOImpl extends SistemaImple<Asistencia, Integer> implements AsistenciaDAO {

	public AsistenciaDAOImpl() {
		super(Asistencia.class);
	}

}
