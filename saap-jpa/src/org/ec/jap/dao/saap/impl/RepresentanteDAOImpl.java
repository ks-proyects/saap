/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.RepresentanteDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Representante;

/**
 * Clase de Acceso a Datos de {@link Representante}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class RepresentanteDAOImpl extends SistemaImple<Representante, Integer> implements RepresentanteDAO {

	public RepresentanteDAOImpl() {
		super(Representante.class);
	}

}
