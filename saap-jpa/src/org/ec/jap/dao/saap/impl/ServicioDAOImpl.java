/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.ServicioDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Servicio;

/**
 * Clase de Acceso a Datos de {@link Servicio}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class ServicioDAOImpl extends SistemaImple<Servicio, Integer> implements ServicioDAO {

	public ServicioDAOImpl() {
		super(Servicio.class);
	}

}
