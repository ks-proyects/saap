/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.RangoConsumoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.RangoConsumo;

/**
 * Clase de Acceso a Datos de {@link RangoConsumo}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class RangoConsumoDAOImpl extends SistemaImple<RangoConsumo, Integer> implements RangoConsumoDAO {

	public RangoConsumoDAOImpl() {
		super(RangoConsumo.class);
	}

}
