/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.PeriodoPagoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.PeriodoPago;

/**
 * Clase de Acceso a Datos de {@link PeriodoPago}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class PeriodoPagoDAOImpl extends SistemaImple<PeriodoPago, Integer> implements PeriodoPagoDAO {

	public PeriodoPagoDAOImpl() {
		super(PeriodoPago.class);
	}

}
