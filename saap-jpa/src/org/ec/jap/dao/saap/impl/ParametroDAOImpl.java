/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.ParametroDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Parametro;

/**
 * Clase de Acceso a Datos de {@link Parametro}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class ParametroDAOImpl extends SistemaImple<Parametro, String> implements ParametroDAO {

	public ParametroDAOImpl() {
		super(Parametro.class);
	}

}
