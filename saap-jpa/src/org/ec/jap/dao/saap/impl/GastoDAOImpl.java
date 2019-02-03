/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.GastoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Gasto;

/**
 * Clase de Acceso a Datos de {@link Gasto}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class GastoDAOImpl extends SistemaImple<Gasto, Integer> implements GastoDAO {

	public GastoDAOImpl() {
		super(Gasto.class);
	}

}
