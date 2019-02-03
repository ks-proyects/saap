/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.EstadoCivilDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.EstadoCivil;

/**
 * Clase de Acceso a Datos de {@link EstadoCivil}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class EstadoCivilDAOImpl extends SistemaImple<EstadoCivil, Integer> implements EstadoCivilDAO {

	public EstadoCivilDAOImpl() {
		super(EstadoCivil.class);
	}

}
