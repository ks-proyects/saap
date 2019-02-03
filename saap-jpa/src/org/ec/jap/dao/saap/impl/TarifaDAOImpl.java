/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.TarifaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Tarifa;

/**
 * Clase de Acceso a Datos de {@link Tarifa}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class TarifaDAOImpl extends SistemaImple<Tarifa, Integer> implements TarifaDAO {

	public TarifaDAOImpl() {
		super(Tarifa.class);
	}

}
